package server.services;

import chess.ChessGame;
import dataAccess.*;
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
    public MessageResponse joinGame(JoinGameRequest request, String username) {
        try {
            ChessGame.PlayerRole role = switch (request.playerColor()) {
                case "WHITE" -> ChessGame.PlayerRole.WHITE_PLAYER;
                case "BLACK" -> ChessGame.PlayerRole.BLACK_PLAYER;
                default -> ChessGame.PlayerRole.SPECTATOR;
            };
            int gameID = request.gameID();
            gameDAO.assignPlayerRole(gameID, username, role);

            return new MessageResponse(200, "Okay!");

        } catch (NoSuchItemException e) {
            return errorResponse(400, e);
        } catch (UnauthorizedAccessException e) {
            return errorResponse(401, e);
        } catch (ValueAlreadyTakenException e) {
            return errorResponse(403, e);
        } catch (DataAccessException e) {
            return errorResponse(500, e);
        }
    }

    private MessageResponse errorResponse(int status, Exception e) {
        return new MessageResponse(status, String.format("Error: %s", e.getMessage()));
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
