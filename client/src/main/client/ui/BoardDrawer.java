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
        StringBuilder builder = new StringBuilder();
        appendNewLine(builder);
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                reader.get(row, col).appendTo(builder);
            }
            appendNewLine(builder);
        }

        ui.println(builder.toString());
    }

    private void appendNewLine(StringBuilder builder) {
        appendReset(builder);
        builder.append("\n");
    }

    private void appendReset(StringBuilder builder) {
        builder.append(EscapeSequences.RESET_TEXT_AND_BG);
    }
}