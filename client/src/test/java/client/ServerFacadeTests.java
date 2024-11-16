package client;

import http.AuthResponse;
import httpconnection.ChessServerFacade;
import httpconnection.FailedConnectionException;
import httpconnection.FailedResponseException;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ChessServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(0);

        String serverURL = "http://localhost:" + port;
        facade = new ChessServerFacade(serverURL);

        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void setUp() throws FailedConnectionException, FailedResponseException {
        facade.clearApplication();
    }

    @Test
    public void registerReturnsNonNullAuthToken() throws FailedConnectionException, FailedResponseException {
        AuthResponse response = facade.register("testUser", "testPass", "testEmail");
        Assertions.assertNotNull(response.authToken());
    }
}
