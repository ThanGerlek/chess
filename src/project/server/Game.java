package server;

import chess.ChessGame;

import java.util.Objects;

/**
 * A model object representing the core data of a chess game.
 */
public class Game {

    private final int gameID;
    private ChessGame chessGame;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;

    /**
     * Creates a new Game object.
     *
     * @param gameID    the unique ID of this Game
     * @param gameName  a human-readable name for this Game
     * @param chessGame a ChessGame object representing the current game state
     */
    public Game(int gameID, String gameName, ChessGame chessGame) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.chessGame = chessGame;
        this.whiteUsername = "";
        this.blackUsername = "";
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

    @Override
    public int hashCode() {
        return Objects.hash(gameID, chessGame, whiteUsername, blackUsername, gameName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameID == game.gameID && Objects.equals(chessGame, game.chessGame) &&
                Objects.equals(whiteUsername, game.whiteUsername) &&
                Objects.equals(blackUsername, game.blackUsername) &&
                Objects.equals(gameName, game.gameName);
    }
}
