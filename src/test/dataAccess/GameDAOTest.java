package dataAccess;

import chess.*;
import chess.pieces.King;
import chess.pieces.Queen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import server.Game;
import server.http.GameListItem;

class GameDAOTest {
    private final boolean USE_DATABASE_DAOS = true;
    private GameDAO gameDAO;
    private ChessGame chessGame1;
    private ChessGame chessGame2;

    private ChessDatabase database;

    // TODO Test player roles in allGames()?

    @BeforeEach
    void setUp() {
        database = new ChessDatabase();
        UserDAO userDAO = USE_DATABASE_DAOS ? new DatabaseUserDAO(database) : new MemoryUserDAO();
        gameDAO = USE_DATABASE_DAOS ? new DatabaseGameDAO(database, userDAO) : new MemoryGameDAO(userDAO);
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
    void removeNonexistentGameDoesNotError() throws DataAccessException {
        gameDAO.removeGame(42);
        Assertions.assertTrue(true);
    }

    @Test
    void allGamesFromEmpty() throws DataAccessException {
        Assertions.assertEquals(0, gameDAO.allGames().length);
    }

    @Test
    void allGamesWithTwoGames() throws DataAccessException {
        Game game1 = new Game(1, "game1", chessGame1);
        Game game2 = new Game(2, "game2", chessGame2);
        gameDAO.insertNewGame(game1);
        gameDAO.insertNewGame(game2);
        GameListItem item1 = new GameListItem(1, null, null, "game1");
        GameListItem item2 = new GameListItem(2, null, null, "game2");

        GameListItem[] actual = gameDAO.allGames();
        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(item1, actual[0]);
        Assertions.assertEquals(item2, actual[1]);
    }

    @Test
    @Disabled
        // TODO test: assignPlayerRoleWhite
    void assignPlayerRoleWhite() throws DataAccessException {
    }

    @Test
    @Disabled
        // TODO test: assignPlayerRoleBlack
    void assignPlayerRoleBlack() throws DataAccessException {
    }

    @Test
    @Disabled
        // TODO test: assignPlayerRoleUndefined
    void assignPlayerRoleUndefined() throws DataAccessException {
    }

    @Test
    @Disabled
        // TODO test: assignPlayerRoleOther
    void assignPlayerRoleOther() throws DataAccessException {
    }

    @Test
    @Disabled
        // TODO test: updateGameState
    void updateGameState() throws DataAccessException {
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