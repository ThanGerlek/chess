package client.ui;

import chess.*;

public class BoardDrawer {
    private final ConsoleUI ui;
    private BoardElement[][] elements;

    public BoardDrawer(ConsoleUI ui, ChessBoard board) {
        this.ui = ui;
        parseElements(board);
    }

    public void draw(ChessGame.TeamColor color) {
        // TODO
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                ui.print(elements[i][j].fgElem.toString());
            }
            ui.println();
        }
        ui.println();
    }

    private void parseElements(ChessBoard board) {
        elements = new BoardElement[10][10];
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[0].length; j++) {
                elements[i][j] = generateBoardElement(i, j, board);
            }
        }
    }

    private BoardElement generateBoardElement(int row, int col, ChessBoard board) {
        BackgroundElement bg = generateBackgroundElement(row, col);
        ForegroundElement fg = generateForegroundElement(row, col, board);
        return new BoardElement(bg, fg);
    }

    private BackgroundElement generateBackgroundElement(int row, int col) {
        if (isOnBorder(row, col)) {
            return BackgroundElement.BORDER;
        } else if (row + col % 2 == 0) {
            return BackgroundElement.WHITE_SQUARE;
        } else {
            return BackgroundElement.BLACK_SQUARE;
        }
    }

    private ForegroundElement generateForegroundElement(int row, int col, ChessBoard board) {
        if (isOnBorder(row, col)) {
            return generateBorderFGElement(row, col);
        }

        ChessPosition chessPos = new ChessPositionImpl(row, col);
        if (board.hasPieceAt(chessPos)) {
            return new ForegroundElement(board.getPiece(chessPos));
        }

        return new ForegroundElement();
    }

    private boolean isOnBorder(int row, int col) {
        return isOnTopOrBottomBorder(row, col) || isOnLeftOrRightBorder(row, col);
    }

    private boolean isOnBorderCorner(int row, int col) {
        return isOnTopOrBottomBorder(row, col) && isOnLeftOrRightBorder(row, col);
    }

    private boolean isOnTopOrBottomBorder(int row, int col) {
        return row == 0 || row == 9;
    }

    private boolean isOnLeftOrRightBorder(int row, int col) {
        return col == 0 || col == 9;
    }

    private ForegroundElement generateBorderFGElement(int row, int col) {
        if (isOnBorderCorner(row, col)) {
            return new ForegroundElement();
        } else if (isOnLeftOrRightBorder(row, col)) {
            return new ForegroundElement(getRankCharFromRowIndex(row));
        } else if (isOnTopOrBottomBorder(row, col)) {
            return new ForegroundElement(getFileCharFromColIndex(col));
        } else {
            throw new IllegalArgumentException(
                    "Called generateBorderFGElement() on a position that is not on the border");
        }
    }

    private char getFileCharFromColIndex(int col) {
        if (col <= 0 || col >= 9) {
            throw new IllegalArgumentException(String.format("Column index %d is out of bounds", col));
        }
        return (char) (col - 1 + 'a');
    }

    private char getRankCharFromRowIndex(int row) {
        if (row <= 0 || row >= 9) {
            throw new IllegalArgumentException(String.format("Row index %d is out of bounds", row));
        }
        return (char) (row - 1 + '1');
    }

    private record BoardElement(BackgroundElement bgElem, ForegroundElement fgElem) {
    }

    private enum BackgroundElement {
        BORDER,
        WHITE_SQUARE,
        BLACK_SQUARE
    }

    private static class ForegroundElement {
        private final String str;

        public ForegroundElement(ChessPiece piece) {
            this.str = getStrFromPiece(piece);
        }

        public ForegroundElement(char label) {
            this.str = " " + label + " ";
        }

        public ForegroundElement() {
            this.str = EscapeSequences.EMPTY;
        }

        private String getStrFromPiece(ChessPiece piece) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return switch (piece.getPieceType()) {
                    case KING -> EscapeSequences.WHITE_KING;
                    case QUEEN -> EscapeSequences.WHITE_QUEEN;
                    case BISHOP -> EscapeSequences.WHITE_BISHOP;
                    case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                    case ROOK -> EscapeSequences.WHITE_ROOK;
                    case PAWN -> EscapeSequences.WHITE_PAWN;
                };
            } else {
                return switch (piece.getPieceType()) {
                    case KING -> EscapeSequences.BLACK_KING;
                    case QUEEN -> EscapeSequences.BLACK_QUEEN;
                    case BISHOP -> EscapeSequences.BLACK_BISHOP;
                    case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                    case ROOK -> EscapeSequences.BLACK_ROOK;
                    case PAWN -> EscapeSequences.BLACK_PAWN;
                };
            }
        }

        public String toString() {
            return str;
        }
    }
}