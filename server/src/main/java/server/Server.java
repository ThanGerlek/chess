package server;

import dataaccess.*;
import http.MessageResponse;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.handlers.*;
import server.webSocket.GameSessionManager;
import server.webSocket.WebSocketServer;

public class Server {

    private Javalin app;

    private final ClearApplicationHandler clearApplicationHandler;
    private final CreateGameHandler createGameHandler;
    private final JoinGameHandler joinGameHandler;
    private final ListGamesHandler listGamesHandler;
    private final LoginHandler loginHandler;
    private final LogoutHandler logoutHandler;
    private final RegisterHandler registerHandler;

    private final WebSocketServer webSocketServer;

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public Server() {
        // TODO Proper dependency injection

        userDAO = new DatabaseUserDAO();
        authDAO = new DatabaseAuthDAO(userDAO);
        gameDAO = new DatabaseGameDAO(userDAO);
//        userDAO = new MemoryUserDAO();
//        authDAO = new MemoryAuthDAO(userDAO);
//        gameDAO = new MemoryGameDAO(userDAO);

        createGameHandler = new CreateGameHandler(authDAO, gameDAO);
        joinGameHandler = new JoinGameHandler(authDAO, gameDAO);
        listGamesHandler = new ListGamesHandler(authDAO, gameDAO);
        loginHandler = new LoginHandler(authDAO, userDAO);
        logoutHandler = new LogoutHandler(authDAO);
        registerHandler = new RegisterHandler(authDAO, userDAO);

        webSocketServer = new WebSocketServer(authDAO, gameDAO);
        GameSessionManager sessionManager = new GameSessionManager(webSocketServer);

        clearApplicationHandler = new ClearApplicationHandler(authDAO, gameDAO, userDAO, sessionManager);
    }

    public void stop() {
        app.stop();
        // TODO? Spark.awaitStop();
    }

    public int run(int desiredPort) {
        System.out.println("Starting the server");

        Handler testHandler = (ctx) -> {
            System.out.println("ctx.method().name() = '" + ctx.method().name() + "'");
            System.out.println("ctx.contextPath() = '" + ctx.contextPath() + "'");
        };

        app = Javalin.create()

        // TODO? Spark.staticFiles.location("web");

        // TODO Spark.webSocket("/ws", webSocketServer);

        // TODO
//        Spark.notFound((req, res) -> {
//            String errMsg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
//            return errorHandler(new Exception(errMsg), req, res);
//        });

                .exception(Exception.class, this::errorHandler)

                .before(testHandler)

                .before((ctx) -> System.out.println("Executing route: " + ctx.method().name() + " " + ctx.contextPath()))

//                .delete("/db", clearApplicationHandler::handleRequest)
//                .post("/user", registerHandler::handleRequest)
//                .post("/session", loginHandler::handleRequest)
//                .delete("/session", logoutHandler::handleRequest)
//                .get("/game", listGamesHandler::handleRequest)
//                .post("/game", createGameHandler::handleRequest)
//                .put("/game", joinGameHandler::handleRequest)

                .after((ctx) -> System.out.println("Finished executing route: " + ctx.contextPath()))

                .start(desiredPort);

        addShutdownHook();

        try {
            setupDataAccess();
        } catch (DataAccessException e) {
            System.out.println("Failed to setup data access!");
            stop();
            System.exit(1);
        }

        // TODO?  Spark.awaitInitialization();
        int port = app.port();
        System.out.println("Listening on port " + port);
        return port;
    }

    private void setupDataAccess() throws DataAccessException {
        ChessDatabaseManager.configureDatabase();
        userDAO.initialize();
        authDAO.initialize();
        gameDAO.initialize();
    }

    /**
     * Create a shutdown hook to ensure the server has a chance to complete any routes in progress and release resources
     * before shutting down.
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping the server...");
            stop();
            System.out.println("Server stopped");
        }));
    }

    private void errorHandler(Exception e, Context ctx) {
        String msg = String.format("[ERROR] Unknown server error: %s", e.getMessage());
        System.out.println(msg);
        MessageResponse response = new MessageResponse(msg);
        HttpHandler.parseToBody(ctx, response, 500);
    }

}