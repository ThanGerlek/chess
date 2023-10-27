package server;

import com.google.gson.Gson;
import dataAccess.*;
import server.handlers.*;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Map;

public class Server {
    private static final int PORT = 8080;

    private final ClearApplicationHandler CLEAR_APPLICATION_HANDLER;
    private final CreateGameHandler CREATE_GAME_HANDLER;
    private final JoinGameHandler JOIN_GAME_HANDLER;
    private final ListGamesHandler LIST_GAMES_HANDLER;
    private final LoginHandler LOGIN_HANDLER;
    private final LogoutHandler LOGOUT_HANDLER;
    private final RegisterHandler REGISTER_HANDLER;

    public Server() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO(userDAO);
        GameDAO gameDAO = new MemoryGameDAO(userDAO);

        CLEAR_APPLICATION_HANDLER = new ClearApplicationHandler(authDAO, gameDAO, userDAO);
        CREATE_GAME_HANDLER = new CreateGameHandler(authDAO, gameDAO);
        JOIN_GAME_HANDLER = new JoinGameHandler(authDAO, gameDAO);
        LIST_GAMES_HANDLER = new ListGamesHandler(authDAO, gameDAO);
        LOGIN_HANDLER = new LoginHandler(authDAO, userDAO);
        LOGOUT_HANDLER = new LogoutHandler(authDAO);
        REGISTER_HANDLER = new RegisterHandler(authDAO, userDAO);
    }

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        System.out.println("Starting the server");
        Spark.port(PORT);
        Spark.externalStaticFileLocation("web");
        createRoutes();
        addShutdownHook();
        Spark.awaitInitialization();
        System.out.println("Listening on port " + PORT);
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
        String body =
                new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "origin", "Server"));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }

}

