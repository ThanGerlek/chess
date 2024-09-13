package ui.BoardUIElement.element;


import chess.ChessGame;
import chess.ChessPiece;
import ui.EscapeSequences;

public class ForegroundElement {
    private final String str;

    public ForegroundElement(ChessPiece piece) {
        this.str = getStrFromPiece(piece);
    }

    public ForegroundElement(char label) {
        this.str = " " + label + " ";
    }

    public ForegroundElement() {
        this.str = EscapeSequences.EMPTY_L;
    }

    private String getStrFromPiece(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch (piece.getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING_L;
                case QUEEN -> EscapeSequences.WHITE_QUEEN_L;
                case BISHOP -> EscapeSequences.WHITE_BISHOP_L;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT_L;
                case ROOK -> EscapeSequences.WHITE_ROOK_L;
                case PAWN -> EscapeSequences.WHITE_PAWN_L;
            };
        } else {
            return switch (piece.getPieceType()) {
                case KING -> EscapeSequences.BLACK_KING_L;
                case QUEEN -> EscapeSequences.BLACK_QUEEN_L;
                case BISHOP -> EscapeSequences.BLACK_BISHOP_L;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT_L;
                case ROOK -> EscapeSequences.BLACK_ROOK_L;
                case PAWN -> EscapeSequences.BLACK_PAWN_L;
            };
        }
    }

    public void appendTo(StringBuilder builder) {
        builder.append(str);
    }
}
