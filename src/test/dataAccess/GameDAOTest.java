package dataAccess;

import chess.*;
import chess.pieces.King;
import chess.pieces.Queen;
import http.GameListItem;
import model.Game;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class GameDAOTest {
    private final boolean USE_DATABASE_DAOS = true;
    private final ChessDatabase database = new ChessDatabase();
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private ChessGame chessGame1;
    private ChessGame chessGame2;
    private Game game1;
    private Game game2;

    // TODO Test player roles in allGames()?
    // TODO Test "unauthorized" negative cases
    // TODO Instead of clearing every time, simply rollback a transaction? See Unit Testing module video.

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = USE_DATABASE_DAOS ? new DatabaseUserDAO(database) : new MemoryUserDAO();
        gameDAO = USE_DATABASE_DAOS ? new DatabaseGameDAO(database, userDAO) : new MemoryGameDAO(userDAO);
        userDAO.initialize();
        gameDAO.initialize();

        database.update("TRUNCATE games");
        database.update("TRUNCATE roles");
        database.update("TRUNCATE users");

        setUpGames();
        setUpUsers();
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

        game1 = new Game(1, "game1", chessGame1);
        game2 = new Game(2, "game2", chessGame2);
    }

    void setUpUsers() throws DataAccessException {
        User user1 = new User("user1", "pass1", "email1");
        User user2 = new User("user2", "pass2", "email2");
        userDAO.insertNewUser(user1);
        userDAO.insertNewUser(user2);
    }

    @Test
    void insertNewGameDoesNotError() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> {
            gameDAO.insertNewGame(game1);
        });
    }

    // findGame positive test
    @Test
    void find_inserted_game_returns_nonnull() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        Assertions.assertNotNull(gameDAO.findGame(1));
    }

    // insertNewGame negative test
    @Test
    void insert_game_twice_with_same_gameID_throws_error() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        Assertions.assertThrows(ValueAlreadyTakenException.class, () -> {
            gameDAO.insertNewGame(game1);
        });
    }

    // insertNewGame positive test
    @Test
    void findReturnsGameWithEqualBoard() throws DataAccessException {
        gameDAO.insertNewGame(game1);

        Game fetchedGame = gameDAO.findGame(1);

        Assertions.assertEquals(game1.chessGame().getBoard().toString(), fetchedGame.chessGame().getBoard().toString());
    }

    @Test
    void findReturnsGameWithSameGameID() throws DataAccessException {
        gameDAO.insertNewGame(game1);

        Game fetchedGame = gameDAO.findGame(1);

        Assertions.assertEquals(1, fetchedGame.gameID());
    }

    @Test
    void findReturnsGameWithSameGameName() throws DataAccessException {
        gameDAO.insertNewGame(game1);

        Game fetchedGame = gameDAO.findGame(1);

        Assertions.assertEquals("game1", fetchedGame.gameName());
    }

    @Test
    void findReturnsGameWithSamePlayers() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.assignPlayerRole(1, "user1", PlayerRole.WHITE_PLAYER);
        gameDAO.assignPlayerRole(1, "user2", PlayerRole.BLACK_PLAYER);

        Game fetchedGame = gameDAO.findGame(1);

        Assertions.assertEquals("user1", fetchedGame.whiteUsername());
        Assertions.assertEquals("user2", fetchedGame.blackUsername());
    }

    @Test
    void findReturnsGameWithSameNumberOfSpectators() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.assignPlayerRole(1, "user1", PlayerRole.SPECTATOR);
        gameDAO.assignPlayerRole(1, "user2", PlayerRole.SPECTATOR);

        Game fetchedGame = gameDAO.findGame(1);

        Assertions.assertEquals(2, fetchedGame.getSpectators().size());
    }

    // findGame negative test
    @Test
    void findNonexistentGameErrors() {
        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(42));
    }

    // removeGame positive test
    @Test
    void findRemovedGameErrors() throws DataAccessException {
        gameDAO.insertNewGame(game1);

        gameDAO.removeGame(1);

        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(1));
    }

    // removeGame "negative" test
    @Test
    void removeNonexistentGameDoesNotThrow() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> {
            gameDAO.removeGame(42);
        });
    }

    // allGames "negative" test
    @Test
    void allGamesFromEmpty() throws DataAccessException {
        Assertions.assertEquals(0, gameDAO.allGames().size());
    }

    // allGames positive test
    @Test
    void allGamesWithTwoGames() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.insertNewGame(game2);
        GameListItem item1 = new GameListItem(1, null, null, "game1");
        GameListItem item2 = new GameListItem(2, null, null, "game2");

        ArrayList<GameListItem> actual = gameDAO.allGames();
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(item1, actual.get(0));
        Assertions.assertEquals(item2, actual.get(1));
    }

    @Test
    void get_white_username_without_assigning_returns_empty() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        Assertions.assertEquals("", gameDAO.findGame(1).whiteUsername());
    }

    @Test
    void get_black_username_without_assigning_returns_empty() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        Assertions.assertEquals("", gameDAO.findGame(1).blackUsername());
    }

    // assignPlayerRole positive test
    @Test
    void assignPlayerRoleWhite_then_get_white_username_matches() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.assignPlayerRole(1, "user1", PlayerRole.WHITE_PLAYER);

        Assertions.assertEquals("user1", gameDAO.findGame(1).whiteUsername());
    }

    @Test
    void assignPlayerRoleBlack_then_get_black_username_matches() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.assignPlayerRole(1, "user1", PlayerRole.BLACK_PLAYER);

        Assertions.assertEquals("user1", gameDAO.findGame(1).blackUsername());
    }

    @Test
    void assign_both_players_then_get_usernames_both_match() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.assignPlayerRole(1, "user1", PlayerRole.WHITE_PLAYER);
        gameDAO.assignPlayerRole(1, "user2", PlayerRole.BLACK_PLAYER);

        Assertions.assertEquals("user1", gameDAO.findGame(1).whiteUsername());
        Assertions.assertEquals("user2", gameDAO.findGame(1).blackUsername());
    }

    // assignPlayerRole positive test
    @Test
    void assignPlayerRole_same_color_twice_then_get_white_username_matches() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.assignPlayerRole(1, "user1", PlayerRole.WHITE_PLAYER);
        gameDAO.assignPlayerRole(1, "user1", PlayerRole.WHITE_PLAYER);

        Assertions.assertEquals("user1", gameDAO.findGame(1).whiteUsername());
    }

    @Test
    void assignPlayerRoleSpectator_adds_player_to_spectators_list() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.assignPlayerRole(1, "user1", PlayerRole.SPECTATOR);

        Assertions.assertTrue(gameDAO.findGame(1).getSpectators().contains("user1"));
    }

    @Test
    void assigning_two_spectators_adds_both_to_spectators_list() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.assignPlayerRole(1, "user1", PlayerRole.SPECTATOR);
        gameDAO.assignPlayerRole(1, "user2", PlayerRole.SPECTATOR);

        ArrayList<String> spectators = gameDAO.findGame(1).getSpectators();

        Assertions.assertTrue(spectators.contains("user1"));
        Assertions.assertTrue(spectators.contains("user2"));
    }

    @Test
    void assignPlayerRole_with_null_role_adds_player_to_spectators_list() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.assignPlayerRole(1, "user1", null);

        Assertions.assertTrue(gameDAO.findGame(1).getSpectators().contains("user1"));
    }

    // assignPlayerRole negative test
    @Test
    void assignPlayerRoleWhite_when_already_taken_errors() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.assignPlayerRole(1, "user1", PlayerRole.WHITE_PLAYER);

        Assertions.assertThrows(ValueAlreadyTakenException.class, () -> {
            gameDAO.assignPlayerRole(1, "user2", PlayerRole.WHITE_PLAYER);
        });
    }

    @Test
    void assignPlayerRoleBlack_when_already_taken_errors() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.assignPlayerRole(1, "user1", PlayerRole.BLACK_PLAYER);

        Assertions.assertThrows(ValueAlreadyTakenException.class, () -> {
            gameDAO.assignPlayerRole(1, "user2", PlayerRole.BLACK_PLAYER);
        });
    }

    // updateGameState positive test
    @Test
    void updateGameState_changes_board_state() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        Game updatedGame = new Game(1, "game1", chessGame2);

        gameDAO.updateGameState(updatedGame);

        ChessGame fetchedGame = gameDAO.findGame(1).chessGame();
        Assertions.assertEquals(chessGame2.getBoard().toString(), fetchedGame.getBoard().toString());
    }

    // updateGameState negative test
    @Test
    void updateGameState_of_nonexistent_game_throws_error() throws DataAccessException {
        Assertions.assertThrows(NoSuchItemException.class, () -> {
            gameDAO.updateGameState(game1);
        });
    }

    // clearGames positive test
    @Test
    void findClearedGamesErrors() throws DataAccessException {
        gameDAO.insertNewGame(game1);
        gameDAO.insertNewGame(game2);

        gameDAO.clearGames();

        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(1));
        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(2));
    }

    // clearGames "negative" test
    @Test
    void clearEmptyDoesNotThrow() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> {
            gameDAO.clearGames();
        });
    }

    // generateNewGameID positive test
    @Test
    void generateTwoGameIDsReturnsDifferentValues() throws DataAccessException {
        int id1 = gameDAO.generateNewGameID();
        int id2 = gameDAO.generateNewGameID();
        Assertions.assertNotEquals(id1, id2);
    }
}