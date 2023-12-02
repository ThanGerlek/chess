package server.webSocket;

import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.concurrent.ConcurrentHashMap;

public class GameSessionManager {
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Session>> gameSessions;
    private final WebSocketServer wsServer;

    // TODO Change to use GameSession instead

    public GameSessionManager(WebSocketServer wsServer) {
        this.gameSessions = new ConcurrentHashMap<>();
        this.wsServer = wsServer;
    }

    public void createGameIfNeeded(int gameID) {
        if (!gameSessions.containsKey(gameID)) {
            gameSessions.put(gameID, new ConcurrentHashMap<>());
        }
    }

    public void addUser(int gameID, String username, Session session) throws DataAccessException {
        createGameIfNeeded(gameID);
        var gameSession = gameSessions.get(gameID);
        if (gameSession == null) {
            throw new DataAccessException("Tried to add user to a GameSession that doesn't exist");
        }
        gameSession.put(username, session);
    }

    public void removeUser(int gameID, String username) throws DataAccessException {
        var gameSession = gameSessions.get(gameID);
        if (gameSession == null) {
            throw new DataAccessException("Tried to add user to a GameSession that doesn't exist");
        }
        gameSession.get(username).close();
        gameSession.remove(username);
    }

    public void message(int gameID, String username, ServerMessage message) {
        Session session = gameSessions.get(gameID).get(username);
        wsServer.send(session, message);
    }

    public void broadcast(int gameID, String excludedUsername, ServerMessage message) {
        var gameSession = gameSessions.get(gameID);
        for (String username : gameSession.keySet()) {
            if (!username.equals(excludedUsername)) {
                Session session = gameSession.get(username);
                wsServer.send(session, message);
            }
        }
    }

    public void broadcastAll(int gameID, ServerMessage message) {
        var gameSession = gameSessions.get(gameID);
        for (String username : gameSession.keySet()) {
            Session session = gameSession.get(username);
            wsServer.send(session, message);
        }
    }

    public void clearGameSessions() {
        gameSessions.clear();
    }
}
