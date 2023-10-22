package server;

import spark.Spark;

public class Server {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        System.out.println("Starting the server");

        Spark.port(PORT);

        // TODO Move /web to /src/main/resources? See Server Wep API powerpoint
        // https://docs.google.com/presentation/d/1Nvb0fUObt-An0nMOFEhgIufnSN6qpixF/edit#slide=id.p13
        Spark.externalStaticFileLocation("web");

        createRoutes();

        addShutdownHook();

        Spark.awaitInitialization();
        System.out.println("Listening on port " + PORT);
    }

    private void createRoutes() {
        createNotFoundRoutes();
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

    private void createNotFoundRoutes() {
        // TODO? See web-api/example-code/.../Custom...Server2
        Spark.notFound((req, res) -> {
            res.type("text/html");
            return "<html><body>My custom 404 page</body></html>";
            // Replace string with text read from a file ^^^
            // (there's code that does this in the above example-code)
        });
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

}

