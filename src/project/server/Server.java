package server;

import com.google.gson.Gson;
import server.handlers.*;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Map;

public class Server {
    private static final int PORT = 8080;
    private static final ClearApplicationHandler CLEAR_APPLICATION_HANDLER = new ClearApplicationHandler();
    private static final CreateGameHandler CREATE_GAME_HANDLER = new CreateGameHandler();
    private static final JoinGameHandler JOIN_GAME_HANDLER = new JoinGameHandler();
    private static final ListGamesHandler LIST_GAMES_HANDLER = new ListGamesHandler();
    private static final LoginHandler LOGIN_HANDLER = new LoginHandler();
    private static final LogoutHandler LOGOUT_HANDLER = new LogoutHandler();
    private static final RegisterHandler REGISTER_HANDLER = new RegisterHandler();

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

    private static void createRoutes() {
        createErrorRoutes();
        createBeforeRoutes();
        createServiceRoutes();
        createAfterRoutes();
    }

    /**
     * Create a shutdown hook to ensure the server has a chance to complete any routes in progress and release resources
     * before shutting down.
     */
    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping the server...");
            Spark.stop();
            System.out.println("Server stopped");
        }));
    }

    private static void createErrorRoutes() {
        // See web-api/example-code/.../Custom...Server2
        Spark.notFound((req, res) -> {
            String errMsg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            return errorHandler(new Exception(errMsg), req, res);
        });
        Spark.exception(Exception.class, Server::errorHandler);
    }

    private static void createBeforeRoutes() {
        Spark.before((req, res) -> System.out.println("Executing route: " + req.pathInfo()));
        // Filters take an optional pattern to restrict the routes to which they are applied:
        // before("/protected/*", (req, res) -> {…});
    }

    private static void createServiceRoutes() {
        Spark.delete("/db", CLEAR_APPLICATION_HANDLER::handleRequest);
        Spark.post("/user", REGISTER_HANDLER::handleRequest);
        Spark.post("/session", LOGIN_HANDLER::handleRequest);
        Spark.delete("/session", LOGOUT_HANDLER::handleRequest);
        Spark.get("/game", LIST_GAMES_HANDLER::handleRequest);
        Spark.post("/game", CREATE_GAME_HANDLER::handleRequest);
        Spark.put("/game", JOIN_GAME_HANDLER::handleRequest);
    }

    private static void createAfterRoutes() {
        Spark.after((req, res) -> System.out.println("Finished executing route: " + req.pathInfo()));
    }

    private static Object errorHandler(Exception e, Request req, Response res) {
        String body =
                new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "origin", "Server"));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }

}

