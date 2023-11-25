package client;

import client.connection.FailedConnectionException;
import client.connection.FailedResponseException;
import com.google.gson.Gson;
import http.CreateGameRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ServerFacade {
    private final String serverURL;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass)
            throws FailedResponseException, FailedConnectionException {
        HttpURLConnection http = setUpConnection(method, path);
        writeRequestBody(request, http);
        connect(http);
        throwIfFailureResponseCode(http);
        return readResponseBody(http, responseClass);
    }

    private HttpURLConnection setUpConnection(String method, String path) throws FailedConnectionException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
//            http.setDoOutput(true); // TODO Unneeded?
            return http;
        } catch (IOException | URISyntaxException e) {
            throw new FailedConnectionException("Failed to set up HTTP connection: " + e.getMessage());
        }
    }

    private static void writeRequestBody(Object request, HttpURLConnection http) throws FailedConnectionException {
        try {
            http.addRequestProperty("Content-type", "application/json");
            String requestData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(requestData.getBytes());
            }
        } catch (IOException e) {
            throw new FailedConnectionException("Could not write request body: " + e.getMessage());
        }
    }

    private static void connect(HttpURLConnection http) throws FailedConnectionException {
        try {
            http.connect();
        } catch (IOException e) {
            throw new FailedConnectionException("Failed to connect to server: " + e.getMessage());
        }
    }

    private static void throwIfFailureResponseCode(HttpURLConnection http) throws FailedResponseException {
        int responseCode;
        try {
            responseCode = http.getResponseCode() / 100;
        } catch (IOException e) {
            throw new FailedResponseException("Could not get response code: " + e.getMessage());
        }
        if (responseCode / 100 != 2) {
            String msg = String.format("Received a failure response code: %d", responseCode);
            throw new FailedResponseException(msg);
        }
    }

    private static <T> T readResponseBody(HttpURLConnection http, Class<T> responseClass)
            throws FailedResponseException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream responseBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(responseBody);
                response = new Gson().fromJson(reader, responseClass);
            } catch (IOException e) {
                throw new FailedResponseException("Failed to read response body: " + e.getMessage());
            }
        }
        return response;
    }

    public void clearApplication() {
        // TODO
        // delete /db
    }

    public void register() {
        // TODO
    }

    public void login() {
        // TODO
    }

    public void logout() {
        // TODO
    }

    public void createGame(String gameName) {
        // TODO
        CreateGameRequest request = new CreateGameRequest(gameName);
        String path = "/thing";

    }

    public void listGames() {
        // TODO
    }

    public void joinGame() {
        // TODO
    }

    public void observeGame() {
        // TODO
    }

}
