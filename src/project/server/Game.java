package server;

import chess.ChessGame;
import chess.ChessGameImpl;
import dataAccess.PlayerRole;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A model object representing the core data of a chess game.
 */
public class Game {

    private final int gameID;
    private final ArrayList<String> spectators;
    private ChessGame chessGame;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;

    /**
     * Creates a new empty game with no players.
     *
     * @param gameID   the unique ID of this game
     * @param gameName a human-readable name for this game
     */
    public Game(int gameID, String gameName) {
        this(gameID, gameName, new ChessGameImpl());
    }

    public Game(int gameID, String gameName, ChessGame chessGame) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.chessGame = chessGame;
        this.whiteUsername = "";
        this.blackUsername = "";
        this.spectators = new ArrayList<>();
    }

    public int gameID() {
        return gameID;
    }

    public ChessGame chessGame() {
        return chessGame;
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    public String whiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String blackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String gameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void addSpectator(String username) {
        this.spectators.add(username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, chessGame, gameName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameID == game.gameID && Objects.equals(chessGame, game.chessGame) &&
                Objects.equals(gameName, game.gameName);
    }

    public boolean hasRole(PlayerRole role) {
        if (PlayerRole.WHITE_PLAYER.equals(role)) {
            return whiteUsername != null && whiteUsername.isEmpty();
        } else if (PlayerRole.BLACK_PLAYER.equals(role)) {
            return blackUsername != null && blackUsername.isEmpty();
        } else if (PlayerRole.SPECTATOR.equals(role)) {
            return !getSpectators().isEmpty();
        } else {
            throw new IllegalArgumentException("Called hasRole() with an unrecognized role type");
        }
    }

    public ArrayList<String> getSpectators() {
        return new ArrayList<>(spectators);
    }
}
