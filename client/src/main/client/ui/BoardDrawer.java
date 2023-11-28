package client.ui;

import chess.ChessBoard;
import chess.ChessGame;

public class BoardDrawer {
    private final ConsoleUI ui;
    private final BoardToUIElementParser parser;

    public BoardDrawer(ConsoleUI ui, ChessBoard board) {
        this.ui = ui;
        this.parser = new BoardToUIElementParser(board);
    }

    public void draw(ChessGame.TeamColor color) {
        // TODO
        for (int row = 9; row >= 0; row--) {
            for (int col = 0; col <= 9; col++) {
                ui.print(parser.get(row, col).fgElem().toString());
            }
            ui.println();
        }
        ui.println();
    }
}