package client.ui.BoardUIElement.element;


import chess.ChessGame;
import chess.ChessPiece;
import client.ui.EscapeSequences;

public class ForegroundElement {
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

    public void appendTo(StringBuilder builder) {
        builder.append(str);
    }
}
