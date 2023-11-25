package client;

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
    private String serverURL;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
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

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            HttpURLConnection http = setUpConnection(method, path);
            writeRequestBody(request, http);
            http.connect();
            throwIfUnsuccessful(http);
            return readResponseBody(http, responseClass);
        } catch (Exception e) {
            // TODO!
            System.out.println("Problem! Failed to make a request!");
            throw e;
        }
    }

    private HttpURLConnection setUpConnection(String method, String path) throws URISyntaxException, IOException {
        URL url = (new URI(serverURL + path)).toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(method);
        http.setDoOutput(true); // TODO What is this?
        return http;
    }

    private static void writeRequestBody(Object request, HttpURLConnection http) throws IOException {
        http.addRequestProperty("Content-type", "application/json");
        String requestData = new Gson().toJson(request);
        try (OutputStream reqBody = http.getOutputStream()) {
            reqBody.write(requestData.getBytes());
        }
    }

    private static void throwIfUnsuccessful(HttpURLConnection http) throws Exception {
        boolean successful = http.getResponseCode() / 100 == 2;
        if (!successful) {
            // TODO!
            throw new Exception("Problem! Got a failure response code!");
        }
    }

    private static <T> T readResponseBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream responseBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(responseBody);
                response = new Gson().fromJson(reader, responseClass);
            }
        }
        return response;
    }

}
