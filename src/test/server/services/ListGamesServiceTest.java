package server.services;

import chess.ChessGame;
import chess.ChessGameImpl;
import chess.ChessPositionImpl;
import chess.pieces.King;
import chess.pieces.Queen;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AuthToken;
import server.Game;
import server.User;
import server.http.ListGamesResponse;

class ListGamesServiceTest extends ServiceTest {
    private final User user = new User("user1", "pass1", "mail1");
    private final AuthToken token = new AuthToken("1234", "user1");
    private final Game game1 = new Game(1, "game1", new ChessGameImpl());
    private final Game game2 = new Game(2, "game2", new ChessGameImpl());
    private ListGamesService service;

    // TODO 500?

    @BeforeEach
    void setUp() throws DataAccessException {
        initDAOs();
        userDAO.insertNewUser(user);
        authDAO.addAuthToken(token);
        service = new ListGamesService(authDAO, gameDAO);

        game1.chessGame().getBoard().resetBoard();
        game2.chessGame().getBoard().resetBoard();
        game1.chessGame().getBoard().addPiece(new ChessPositionImpl(3, 3), new King(ChessGame.TeamColor.WHITE));
        game2.chessGame().getBoard().addPiece(new ChessPositionImpl(3, 3), new Queen(ChessGame.TeamColor.BLACK));

        gameDAO.insertNewGame(game1);
        gameDAO.insertNewGame(game2);
    }

    // Positive test
    @Test
    void list_Games_returns_okay() throws DataAccessException {
        ListGamesResponse response = service.listGames(token);
        Assertions.assertEquals(200, response.status());
    }

    // Negative test
    @Test
    void list_Games_with_invalid_token_returns_forbidden() throws DataAccessException {
        ListGamesResponse response = service.listGames(new AuthToken("1234", "iDoNotExist"));
        Assertions.assertEquals(401, response.status());
    }

}