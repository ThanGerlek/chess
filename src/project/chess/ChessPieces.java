package chess;

import chess.pieces.*;

public final class ChessPieces {
    private ChessPieces() {}

    public static ChessPiece FromType(ChessPiece.PieceType type, ChessGame.TeamColor color) {
        return switch (type) {
            case KING -> new King(color);
            case QUEEN -> new Queen(color);
            case BISHOP -> new Bishop(color);
            case KNIGHT -> new Knight(color);
            case ROOK -> new Rook(color);
            case PAWN -> new Pawn(color);
        };
    }

    public static char symbol(ChessPiece.PieceType type, ChessGame.TeamColor color) {
        char symbol = switch (type) {
            case KING -> 'K';
            case QUEEN -> 'Q';
            case BISHOP -> 'B';
            case KNIGHT -> 'N';
            case ROOK -> 'R';
            case PAWN -> 'P';
        };
        if (color == ChessGame.TeamColor.BLACK) {
            symbol = (char) (symbol - 'A' + 'a');
        }
        return symbol;
    }

    public static char symbol(ChessPiece piece) {
        return symbol(piece.getPieceType(), piece.getTeamColor());
    }
}
