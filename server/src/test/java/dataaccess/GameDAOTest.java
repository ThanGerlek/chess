package dataaccess;

import chess.*;
import http.GameListItem;
import model.Game;
import model.User;
import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {
    private static final boolean IS_SQL_DAO = false;
    private static final User USER = new User("user1", "pass1", "email1");
    private static final User USER_2 = new User("user2", "pass2", "email2");
    private static final Game GAME_2 = new Game(2, "GameDAOTest Game 2");
    private static final int INVALID_GAME_ID = 42;
    private static UserDAO userDAO;
    private static GameDAO gameDAO;
    private static Game game = new Game(1, "GameDAOTest Game");

    @BeforeAll
    static void init() throws DataAccessException {
        userDAO = new MemoryUserDAO();
        userDAO.clearUsers();
        userDAO.insertNewUser(USER);
        userDAO.insertNewUser(USER_2);
    }

    @AfterAll
    static void deInit() throws DataAccessException {
        userDAO.clearUsers();
        gameDAO.clearGames();
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO = IS_SQL_DAO ? new DatabaseGameDAO(userDAO) : new MemoryGameDAO(userDAO);
        gameDAO.clearGames();
        game = new Game(1, "GameDAOTest Game");
    }

    @Test
    void findPreexistingGameReturnsNonnull() throws DataAccessException {
        gameDAO.insertNewGame(game);
        assertNotNull(gameDAO.findGame(game.gameID()));
    }

    @Test
    void insertNewGameWithPreexistingGameIDThrows() throws DataAccessException {
        gameDAO.insertNewGame(game);
        assertThrows(ValueAlreadyTakenException.class, () -> gameDAO.insertNewGame(game));
    }

    @Test
    void findNonexistentGameThrows() {
        assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(INVALID_GAME_ID));
    }

    @Test
    void allGamesAfterInsertingReturnsCorrectlySizedList() throws DataAccessException {
        gameDAO.insertNewGame(game);
        gameDAO.insertNewGame(GAME_2);
        Collection<GameListItem> games = gameDAO.allGames();
        assertEquals(2, games.size());
    }

    @Test
    void allGamesWithoutInsertingReturnsEmptyList() throws DataAccessException {
        Collection<GameListItem> games = gameDAO.allGames();
        assertEquals(0, games.size());
    }

    @Test
    void assignPlayerRoleWHITEThenGetHasCorrectRoleUsernames() throws DataAccessException {
        gameDAO.insertNewGame(game);
        System.out.println(gameDAO.findGame(1));
        gameDAO.assignPlayerRole(game.gameID(), USER.username(), PlayerRole.WHITE_PLAYER);

        Game retrievedGame = gameDAO.findGame(game.gameID());
        assertEquals(USER.username(), retrievedGame.whiteUsername());
        assertEquals("", retrievedGame.blackUsername());
    }

    @Test
    void assignPlayerRoleBLACKThenGetHasCorrectRoleUsernames() throws DataAccessException {
        gameDAO.insertNewGame(game);
        gameDAO.assignPlayerRole(game.gameID(), USER_2.username(), PlayerRole.BLACK_PLAYER);

        Game retrievedGame = gameDAO.findGame(game.gameID());
        assertEquals("", retrievedGame.whiteUsername());
        assertEquals(USER_2.username(), retrievedGame.blackUsername());
    }

    @Test
    void assignPlayerRoleSPECTATORDoesNotChangeRoleUsernames() throws DataAccessException {
        gameDAO.insertNewGame(game);
        gameDAO.assignPlayerRole(game.gameID(), USER.username(), PlayerRole.SPECTATOR);

        Game retrievedGame = gameDAO.findGame(game.gameID());
        assertEquals("", retrievedGame.whiteUsername());
        assertEquals("", retrievedGame.blackUsername());
    }

    @Test
    void assignPlayerRoleBothRolesThenGetHasCorrectRoleUsernames() throws DataAccessException {
        gameDAO.insertNewGame(game);
        gameDAO.assignPlayerRole(game.gameID(), USER.username(), PlayerRole.WHITE_PLAYER);
        gameDAO.assignPlayerRole(game.gameID(), USER_2.username(), PlayerRole.BLACK_PLAYER);

        Game retrievedGame = gameDAO.findGame(game.gameID());
        assertEquals(USER.username(), retrievedGame.whiteUsername());
        assertEquals(USER_2.username(), retrievedGame.blackUsername());
    }

    @Test
    void assignPlayerRoleInvalidGameIDThrows() {
        assertThrows(NoSuchItemException.class,
                () -> gameDAO.assignPlayerRole(INVALID_GAME_ID, USER.username(), PlayerRole.WHITE_PLAYER));
    }

    @Test
    void assignPlayerRoleInvalidUsernameThrows() throws DataAccessException {
        gameDAO.insertNewGame(game);

        User invalidUser = new User("invalidUsername", "password", "email");
        assertThrows(UnauthorizedAccessException.class,
                () -> gameDAO.assignPlayerRole(game.gameID(), invalidUser.username(), PlayerRole.WHITE_PLAYER));
    }

    @Test
    void updateGameStateThenGetReturnsUpdatedBoard() throws DataAccessException, InvalidMoveException {
        gameDAO.insertNewGame(game);

        ChessGame updatedChessGame = new ChessGame();
        ChessMove move = new ChessMove(new ChessPosition(2, 1), new ChessPosition(4, 1));
        updatedChessGame.makeMove(move);
        Game updatedGame = new Game(game.gameID(), game.gameName(), updatedChessGame);

        gameDAO.updateGameState(updatedGame);

        Game retrievedGame = gameDAO.findGame(game.gameID());
        ChessBoard board = retrievedGame.chessGame().getBoard();
        assertNotNull(board.getPiece(new ChessPosition(4, 1)));
    }

    @Test
    void removeGameThenFindGameThrows() throws DataAccessException {
        gameDAO.insertNewGame(game);
        gameDAO.removeGame(game.gameID());

        assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(game.gameID()));
    }

    @Test
    void allGamesAfterClearGamesReturnsEmptyList() throws DataAccessException {
        gameDAO.insertNewGame(game);
        gameDAO.insertNewGame(GAME_2);
        gameDAO.clearGames();
        Collection<GameListItem> games = gameDAO.allGames();
        assertEquals(0, games.size());
    }

    @Test
    void twoGeneratedGameIDsAreDifferent() throws DataAccessException {
        Assertions.assertNotEquals(gameDAO.generateNewGameID(), gameDAO.generateNewGameID());
    }
}
