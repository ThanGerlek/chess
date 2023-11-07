package server;

import dataAccess.*;
import server.handlers.*;
import server.http.MessageResponse;
import spark.Request;
import spark.Response;
import spark.Spark;

public class Server {
    private static final int PORT = 8080;

    private final ClearApplicationHandler CLEAR_APPLICATION_HANDLER;
    private final CreateGameHandler CREATE_GAME_HANDLER;
    private final JoinGameHandler JOIN_GAME_HANDLER;
    private final ListGamesHandler LIST_GAMES_HANDLER;
    private final LoginHandler LOGIN_HANDLER;
    private final LogoutHandler LOGOUT_HANDLER;
    private final RegisterHandler REGISTER_HANDLER;

    private final ChessDatabase database;

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public Server() {
        // TODO Instead of packing everything into Server, write a database handler or something?
        database = new ChessDatabase();

        userDAO = new DatabaseUserDAO(database);
        authDAO = new DatabaseAuthDAO(database, userDAO);
        gameDAO = new DatabaseGameDAO(database, userDAO);

        CLEAR_APPLICATION_HANDLER = new ClearApplicationHandler(authDAO, gameDAO, userDAO);
        CREATE_GAME_HANDLER = new CreateGameHandler(authDAO, gameDAO);
        JOIN_GAME_HANDLER = new JoinGameHandler(authDAO, gameDAO);
        LIST_GAMES_HANDLER = new ListGamesHandler(authDAO, gameDAO);
        LOGIN_HANDLER = new LoginHandler(authDAO, userDAO);
        LOGOUT_HANDLER = new LogoutHandler(authDAO);
        REGISTER_HANDLER = new RegisterHandler(authDAO, userDAO);
    }

    public static void main(String[] args) throws DataAccessException {
        new Server().run();
    }

    private void run() throws DataAccessException {
        System.out.println("Starting the server");
        setupHttpRouting();
        try {
            setupDataAccess();
        } catch (DataAccessException e) {
            System.out.println("Failed to setup data access!");
            Spark.stop();
            throw e;
        }
    }

    private void setupHttpRouting() {
        Spark.port(PORT);
        Spark.externalStaticFileLocation("web");
        createRoutes();
        addShutdownHook();
        Spark.awaitInitialization();
        System.out.println("Listening on port " + PORT);
    }

    private void setupDataAccess() throws DataAccessException {
        database.initialize();
        userDAO.initialize();
        authDAO.initialize();
        gameDAO.initialize();
    }

    private void createRoutes() {
        createErrorRoutes();
        createBeforeRoutes();
        createServiceRoutes();
        createAfterRoutes();
    }

    /**
     * Create a shutdown hook to ensure the server has a chance to complete any routes in progress and release resources
     * before shutting down.
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping the server...");
            Spark.stop();
            System.out.println("Server stopped");
        }));
    }

    private void createErrorRoutes() {
        // See web-api/example-code/.../Custom...Server2
        Spark.notFound((req, res) -> {
            String errMsg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            return errorHandler(new Exception(errMsg), req, res);
        });
        Spark.exception(Exception.class, this::errorHandler);
    }

    private void createBeforeRoutes() {
        Spark.before((req, res) -> System.out.println("Executing route: " + req.pathInfo()));
        // Filters take an optional pattern to restrict the routes to which they are applied:
        // before("/protected/*", (req, res) -> {…});
    }

    private void createServiceRoutes() {
        Spark.delete("/db", CLEAR_APPLICATION_HANDLER::handleRequest);
        Spark.post("/user", REGISTER_HANDLER::handleRequest);
        Spark.post("/session", LOGIN_HANDLER::handleRequest);
        Spark.delete("/session", LOGOUT_HANDLER::handleRequest);
        Spark.get("/game", LIST_GAMES_HANDLER::handleRequest);
        Spark.post("/game", CREATE_GAME_HANDLER::handleRequest);
        Spark.put("/game", JOIN_GAME_HANDLER::handleRequest);
    }

    private void createAfterRoutes() {
        Spark.after((req, res) -> System.out.println("Finished executing route: " + req.pathInfo()));
    }

    private Object errorHandler(Exception e, Request req, Response res) {
        String msg = String.format("[ERROR] Unknown server error: %s", e.getMessage());
        System.out.println(msg);
        MessageResponse response = new MessageResponse(msg);
        return Handler.parseToBody(res, response, 500);
    }

}

