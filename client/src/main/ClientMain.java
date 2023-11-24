import client.REPL;

// Handles initial setup and non-UI command line
public class ClientMain {
    private static final String SERVER_URL = "localhost:8080";

    public static void main(String[] args) {
        System.out.println("Running chess client");
        new REPL(SERVER_URL).run();
        System.out.println("Chess client terminated");
    }

}
