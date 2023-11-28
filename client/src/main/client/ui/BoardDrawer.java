package client.ui;

import chess.ChessBoard;
import chess.ChessGame;

public class BoardDrawer {
    private final ConsoleUI ui;

    public BoardDrawer(ConsoleUI ui) {
        this.ui = ui;
    }

    public void drawBoard(ChessBoard board, ChessGame.TeamColor color) {
        drawBackground();
        drawBorder(color);
        drawCheckerboard();
        drawPieces(board, color);
    }

    private void drawBackground() {
        // TODO
    }

    private void drawBorder(ChessGame.TeamColor color) {
        // TODO
    }

    private void drawCheckerboard() {
        // TODO
    }

    private void drawPieces(ChessBoard board, ChessGame.TeamColor color) {
        // TODO
    }
}
