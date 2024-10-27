package server;

import dataaccess.*;
import http.MessageResponse;
import io.javalin.Javalin;
import io.javalin.http.Context;
import server.handlers.*;
import server.services.*;
import server.webSocket.GameSessionManager;
import server.webSocket.WebSocketServer;

public class Server {

    private Javalin app;

    private final WebSocketServer webSocketServer;

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    private final ClearApplicationHandler clearApplicationHandler;
    private final CreateGameHandler createGameHandler;
    private final JoinGameHandler joinGameHandler;
    private final ListGamesHandler listGamesHandler;
    private final LoginHandler loginHandler;
    private final LogoutHandler logoutHandler;
    private final RegisterHandler registerHandler;


    public Server() {
        // TODO Proper dependency injection

        userDAO = new DatabaseUserDAO();
        authDAO = new DatabaseAuthDAO(userDAO);
        gameDAO = new DatabaseGameDAO(userDAO);
//        userDAO = new MemoryUserDAO();
//        authDAO = new MemoryAuthDAO(userDAO);
//        gameDAO = new MemoryGameDAO(userDAO);

        webSocketServer = new WebSocketServer(authDAO, gameDAO);
        GameSessionManager sessionManager = new GameSessionManager(webSocketServer);

        ClearApplicationService clearApplicationService = new ClearApplicationService(authDAO, gameDAO, userDAO,
                sessionManager);
        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO);
        ListGamesService listGamesService = new ListGamesService(authDAO, gameDAO);
        LoginService loginService = new LoginService(authDAO, userDAO);
        LogoutService logoutService = new LogoutService(authDAO);
        RegisterService registerService = new RegisterService(authDAO, userDAO);

        clearApplicationHandler = new ClearApplicationHandler(clearApplicationService);
        createGameHandler = new CreateGameHandler(createGameService);
        joinGameHandler = new JoinGameHandler(joinGameService);
        listGamesHandler = new ListGamesHandler(listGamesService);
        loginHandler = new LoginHandler(loginService);
        logoutHandler = new LogoutHandler(logoutService);
        registerHandler = new RegisterHandler(registerService);

    }

    public void stop() {
        app.stop();
    }

    public int run(int desiredPort) {
        System.out.println("Starting the server");

        app = Javalin.create(config -> config.staticFiles.add("/web"))

                .exception(Exception.class, this::errorHandler)

                .ws("/ws", webSocketServer::setUpWebSocketHandlers)

                .before((ctx) -> System.out.println("Executing route: " + ctx.method().name() + " " + ctx.path()))

                .delete("/db", clearApplicationHandler::handleRequest)
                .post("/user", registerHandler::handleRequest)
                .post("/session", loginHandler::handleRequest)
                .delete("/session", logoutHandler::handleRequest)
                .get("/game", listGamesHandler::handleRequest)
                .post("/game", createGameHandler::handleRequest)
                .put("/game", joinGameHandler::handleRequest)

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