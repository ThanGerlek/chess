package dataAccess;

import chess.*;
import chess.pieces.King;
import chess.pieces.Queen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Game;
import server.http.GameListItem;

class GameDAOTest {
    private GameDAO gameDAO;
    private ChessGame chessGame1;
    private ChessGame chessGame2;

    // TODO Test player roles in allGames()?

    @BeforeEach
    void setUp() {
        UserDAO userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO(userDAO);
        setUpGames();
    }

    void setUpGames() {
        ChessBoard board1 = new ChessBoardImpl();
        ChessBoard board2 = new ChessBoardImpl();

        board1.addPiece(new ChessPositionImpl(3, 3), new King(ChessGame.TeamColor.WHITE));
        board2.addPiece(new ChessPositionImpl(3, 3), new Queen(ChessGame.TeamColor.BLACK));

        chessGame1 = new ChessGameImpl();
        chessGame1.setBoard(board1);
        chessGame2 = new ChessGameImpl();
        chessGame2.setBoard(board2);
    }

    @Test
    void insertNewGameDoesNotError() throws DataAccessException {
        Game game1 = new Game(1, "game1", chessGame1);
        gameDAO.insertNewGame(game1);
        Assertions.assertTrue(true);
    }

    @Test
    void findInsertedGameReturnsGameWithEqualBoard() throws DataAccessException {
        Game game1 = new Game(1, "game1", chessGame1);
        gameDAO.insertNewGame(game1);

        Game fetchedGame = gameDAO.findGame(1);

        Assertions.assertEquals(game1.chessGame().getBoard().toString(), fetchedGame.chessGame().getBoard().toString());
    }

    @Test
    void findNonexistentGameErrors() throws DataAccessException {
        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(42));
    }

    @Test
    void findRemovedGameErrors() throws DataAccessException {
        Game game1 = new Game(1, "game1", chessGame1);
        gameDAO.insertNewGame(game1);

        gameDAO.removeGame(1);

        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(1));
    }

    @Test
    void removeNonexistentGameErrors() throws DataAccessException {
        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.removeGame(42));
    }

    @Test
    void allGamesFromEmpty() throws DataAccessException {
        GameListItem[] expected = new GameListItem[0];
        Assertions.assertEquals(expected, gameDAO.allGames());
    }

    @Test
    void allGamesWithTwoGames() throws DataAccessException {
        Game game1 = new Game(1, "game1", chessGame1);
        Game game2 = new Game(2, "game2", chessGame2);
        gameDAO.insertNewGame(game1);
        gameDAO.insertNewGame(game2);
        GameListItem item1 = new GameListItem(1, "w1", "b1", "game1");
        GameListItem item2 = new GameListItem(2, "w2", "b2", "game2");
        GameListItem[] expected = {item1, item2};

        Assertions.assertArrayEquals(expected, gameDAO.allGames());
    }

    @Test
    void assignPlayerRoleWhite() throws DataAccessException {
        // TODO
    }

    @Test
    void assignPlayerRoleBlack() throws DataAccessException {
        // TODO
    }

    @Test
    void assignPlayerRoleUndefined() throws DataAccessException {
        // TODO
    }

    @Test
    void assignPlayerRoleOther() throws DataAccessException {
        // TODO
    }

    @Test
    void updateGameState() throws DataAccessException {
        // TODO
    }

    @Test
    void findClearedGamesErrors() throws DataAccessException {
        Game game1 = new Game(1, "game1", chessGame1);
        Game game2 = new Game(2, "game2", chessGame2);
        gameDAO.insertNewGame(game1);
        gameDAO.insertNewGame(game2);

        gameDAO.clearGames();

        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(1));
        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(2));
    }

    @Test
    void clearEmptyIsOkay() throws DataAccessException {
        gameDAO.clearGames();
        Assertions.assertTrue(true);
    }

    @Test
    void generateTwoGameIDsReturnsDifferentValues() {
        int id1 = gameDAO.generateNewGameID();
        int id2 = gameDAO.generateNewGameID();
        Assertions.assertNotEquals(id1, id2);
    }
}