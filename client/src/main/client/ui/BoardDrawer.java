package client.ui;

import chess.ChessBoard;
import chess.ChessGame;

public class BoardDrawer {
    private final ConsoleUI ui;
    private final BoardToUIElementParser parser;
    private BoardUIElementReader reader;

    public BoardDrawer(ConsoleUI ui, ChessBoard board) {
        this.ui = ui;
        this.parser = new BoardToUIElementParser(board);
        this.reader = parser;
    }

    public void setViewerTeamColor(ChessGame.TeamColor color) {
        this.reader = new BoardUIElementRotator(parser, color);
    }

    public void draw() {
        // TODO
        drawForegroundOnly();
    }

    public void drawForegroundOnly() {
        // TODO
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                ui.print(reader.get(row, col).fgElem().toString());
            }
            ui.println();
        }
        ui.println();
    }
}