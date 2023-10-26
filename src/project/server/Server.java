package server;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Map;

public class Server {
    private static final int PORT = 8080;

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
    private static void addShutdownHook() {
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
            Exception e = new Exception(errMsg);
            return errorHandler(e, req, res);
        });
        Spark.exception(Exception.class, this::errorHandler);
    }

    private void createBeforeRoutes() {
        Spark.before((req, res) -> System.out.println("Executing route: " + req.pathInfo()));
        // Filters take an optional pattern to restrict the routes to which they are applied:
        // before("/protected/*", (req, res) -> {…});
    }

    private void createServiceRoutes() {
        // TODO Register handlers for each endpoint

        Spark.get("/hello", (req, res) -> "Hello BYU!");
        // Spark.post("/user", (req, res) -> (new RegisterHandler()).handleRequest(req, res));
    }

    private void createAfterRoutes() {
        Spark.after((req, res) -> System.out.println("Finished executing route: " + req.pathInfo()));
    }

    private Object errorHandler(Exception e, Request req, Response res) {
        String body =
                new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }

}

