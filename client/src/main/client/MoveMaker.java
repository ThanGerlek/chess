package client;

import chess.*;
import client.httpConnection.FailedConnectionException;
import client.ui.ConsoleUI;
import client.websocket.WebSocketClient;
import webSocketMessages.userCommands.MakeMoveGameCommand;

public class MoveMaker {
    private final ConsoleUI ui;
    private final WebSocketClient wsClient;
    private final SessionData sessionData;
    private final ChessGame game;

    public MoveMaker(ConsoleUI ui, WebSocketClient wsClient, SessionData sessionData, ChessGame game) {
        this.ui = ui;
        this.wsClient = wsClient;
        this.sessionData = sessionData;
        this.game = game;
    }

    public void makeMove() throws FailedConnectionException, CommandCancelException {
        String startString = ui.promptInput("Enter the starting position: ");
        ChessPosition startPosition = parseToPosition(startString);
        String endString = ui.promptInput("Enter the ending position: ");
        ChessPosition endPosition = parseToPosition(endString);
        String promotionString = ui.promptInput("(Optional) Enter the promotion piece [Q|R|B|N]: ");
        ChessPiece.PieceType promotionPiece = parseToPromotionPiece(promotionString);

        ChessMove move = new ChessMoveImpl(startPosition, endPosition, promotionPiece);

        if (game.validMoves(startPosition).contains(move)) {
            wsClient.send(new MakeMoveGameCommand(sessionData.getAuthTokenString(), sessionData.getGameID(), move));
        }
    }

    private ChessPosition parseToPosition(String positionString) throws CommandCancelException {
        positionString = positionString.strip().toLowerCase();
        if (positionString.isEmpty()) {
            throw new CommandCancelException("Cancelled by player");
        } else if (positionString.length() != 2) {
            throw cancelOnInvalidInput(positionString, "Invalid positionString: '" + positionString + "'");
        }
        int row = positionString.charAt(0) - 'a' + 1;
        int col = positionString.charAt(1) - '1' + 1;
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            throw cancelOnInvalidInput(positionString, "Invalid positionString: '" + positionString + "'");
        }
        return new ChessPositionImpl(row, col);
    }

    private ChessPiece.PieceType parseToPromotionPiece(String rawPromotionString) throws CommandCancelException {
        String promotionString = rawPromotionString.strip().toLowerCase();
        if (promotionString.isEmpty()) {
            return null;
        } else if (promotionString.length() != 1) {
            throw cancelOnInvalidInput(rawPromotionString, "Invalid promotionString: '" + rawPromotionString + "'");
        }

        ChessPiece.PieceType piece = null;
        if ("q".equals(promotionString)) {
            piece = ChessPiece.PieceType.QUEEN;
        } else if ("b".equals(promotionString)) {
            piece = ChessPiece.PieceType.BISHOP;
        } else if ("r".equals(promotionString)) {
            piece = ChessPiece.PieceType.ROOK;
        } else if ("n".equals(promotionString)) {
            piece = ChessPiece.PieceType.KNIGHT;
        } else {
            throw cancelOnInvalidInput(promotionString, "Invalid promotionString: '" + promotionString + "'");
        }
        return piece;
    }

    private CommandCancelException cancelOnInvalidInput(String input, String msg) {
        ui.println(String.format("Unrecognized value '%s'. Cancelling", input));
        return new CommandCancelException(msg);
    }
}
