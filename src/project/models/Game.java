package models;

import chess.ChessGame;
import chess.ChessGameImpl;

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
     * Creates a new empty game with no players.
     *
     * @param gameID   the unique ID of this game
     * @param gameName a human-readable name for this game
     */
    public Game(int gameID, String gameName) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.chessGame = new ChessGameImpl();
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
}
