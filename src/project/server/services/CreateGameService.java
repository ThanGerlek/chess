package server.services;

import chess.ChessGameImpl;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import server.Game;
import server.http.CreateGameRequest;
import server.http.CreateGameResponse;

/**
 * Provides the Create New Game service, which registers and initializes a new empty game.
 */
public class CreateGameService {
    private static final GameDAO gameDAO = new MemoryGameDAO();

    /**
     * Create a new game from the given CreateGameRequest.
     *
     * @param request a CreateGameRequest representing the HTTP request.
     * @return a CreateGameResponse representing the resulting HTTP response.
     */
    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        int gameID = registerNewGame(request.gameName());
        return new CreateGameResponse(200, gameID, "Okay!");
    }

    private int registerNewGame(String gameName) throws DataAccessException {
        int gameID = gameDAO.generateNewGameID();
        Game game = new Game(gameID, gameName, new ChessGameImpl());
        gameDAO.insertNewGame(game);
        return gameID;
    }

/*

| **Headers**          | `authorization: <authToken>`                 |
| **Body**             | `{ "gameName":"" }`                          |
| **Success response** | [200] `{ "gameID": 1234 }`                   |
| **Failure response** | [400] `{ "message": "Error: bad request" }`  |
| **Failure response** | [401] `{ "message": "Error: unauthorized" }` |
| **Failure response** | [500] `{ "message": "Error: description" }`  |
     */

}
