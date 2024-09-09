package websocket.commands;

import chess.ChessGame;

public class ConnectGameCommand extends UserGameCommand {
    private final ChessGame.TeamColor playerColor; // TODO Convert to dataaccess.PlayerRole?

    public ConnectGameCommand(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(CommandType.CONNECT, authToken, gameID);
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}