package server.services;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import server.http.JoinGameRequest;
import server.http.MessageResponse;

/**
 * Provides the Join Game service, which connects a user to an existing game as either a player or spectator. This
 * request is idempotent.
 */
public class JoinGameService {
    private static final GameDAO gameDAO = new MemoryGameDAO();

    /**
     * Connect a user to an existing game as either a player or spectator. Verifies that the specified game exists, and,
     * if a color is specified, adds the caller as the requested color to the game. If no color is specified the user is
     * joined as an observer. This request is idempotent.
     *
     * @param request  a JoinGameRequest representing the HTTP request.
     * @param username the username to assign to the role.
     * @return a MessageResponse representing the resulting HTTP response.
     */
    public MessageResponse joinGame(JoinGameRequest request, String username) throws DataAccessException {
        ChessGame.PlayerRole role = switch (request.playerColor()) {
            case "WHITE" -> ChessGame.PlayerRole.WHITE_PLAYER;
            case "BLACK" -> ChessGame.PlayerRole.BLACK_PLAYER;
            default -> ChessGame.PlayerRole.SPECTATOR;
        };
        int gameID = request.gameID();
        gameDAO.assignPlayerRole(gameID, username, role);

        return new MessageResponse(200, "Okay!");
    }

/*

| **Headers**          | `authorization: <authToken>`
| **Body**             | `{ "playerColor":"WHITE/BLACK", "gameID": 1234 }`
| **Success response** | [200]
| **Failure response** | [400] `{ "message": "Error: bad request" }`
| **Failure response** | [401] `{ "message": "Error: unauthorized" }`
| **Failure response** | [403] `{ "message": "Error: already taken" }`
| **Failure response** | [500] `{ "message": "Error: description" }`
     */

}
