package client.connection;

import chess.ChessGame;
import http.*;

import java.util.ArrayList;

public class ChessServerFacade {
    private final ServerFacade serverFacade;

    public ChessServerFacade(String serverURL) {
        this.serverFacade = new ServerFacade(serverURL);
    }

    public void clearApplication() throws FailedConnectionException, FailedResponseException {
        serverFacade.makeRequest(new RequestData("DELETE", "/db"));
    }

    public AuthResponse register(String username, String password, String email)
            throws FailedConnectionException, FailedResponseException {
        RegisterRequest request = new RegisterRequest(username, password, email);

        RequestData rd = new RequestData("POST", "/user", request);
        return serverFacade.makeRequest(rd, AuthResponse.class);
    }

    public AuthResponse login(String username, String password)
            throws FailedConnectionException, FailedResponseException {
        LoginRequest request = new LoginRequest(username, password);
        RequestData rd = new RequestData("POST", "/session", request);
        return serverFacade.makeRequest(rd, AuthResponse.class);
    }

    public void logout() throws FailedConnectionException, FailedResponseException {
        serverFacade.makeRequest(new RequestData("DELETE", "/session"));
    }

    public int createGame(String gameName) throws FailedConnectionException, FailedResponseException {
        CreateGameRequest request = new CreateGameRequest(gameName);
        RequestData rd = new RequestData("POST", "/game", request);
        CreateGameResponse response = serverFacade.makeRequest(rd, CreateGameResponse.class);
        return response.gameID();
    }

    public ArrayList<GameListItem> listGames() throws FailedConnectionException, FailedResponseException {
        RequestData rd = new RequestData("GET", "/game");
        ListGamesResponse response = serverFacade.makeRequest(rd, ListGamesResponse.class);
        return response.games();
    }

    public void joinGame(ChessGame.TeamColor playerColor, int gameID)
            throws FailedConnectionException, FailedResponseException {
        String colorString = playerColor == null ? null : playerColor.name();
        JoinGameRequest request = new JoinGameRequest(colorString, gameID);
        RequestData rd = new RequestData("PUT", "/game", request);
        serverFacade.makeRequest(rd);
    }
}