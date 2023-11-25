package client;

import http.CreateGameRequest;

public class ServerFacade {
    private String serverURL;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
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
