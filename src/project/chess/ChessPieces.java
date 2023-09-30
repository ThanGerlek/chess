package chess;

import chess.pieces.*;

public final class ChessPieces {
    private ChessPieces() {}

    public static ChessPiece FromType(ChessPiece.PieceType type, ChessGame.TeamColor color) {
    return FromType(type, color, true);
    }

    public static ChessPiece FromType(ChessPiece.PieceType type, ChessGame.TeamColor color, boolean hasNeverMoved) {
        return switch (type) {
            case KING -> new King(color, hasNeverMoved);
            case QUEEN -> new Queen(color, hasNeverMoved);
            case BISHOP -> new Bishop(color, hasNeverMoved);
            case KNIGHT -> new Knight(color, hasNeverMoved);
            case ROOK -> new Rook(color, hasNeverMoved);
            case PAWN -> new Pawn(color, hasNeverMoved);
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

    public static ChessGame.TeamColor not(ChessGame.TeamColor color) {
        return color == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
    }

    public static boolean isValidPromotionPiece(ChessPiece.PieceType type) {
        return type != ChessPiece.PieceType.PAWN && type != ChessPiece.PieceType.KING;
    }

    public static ChessPiece promote(ChessPiece piece, ChessPiece.PieceType promotionPiece) {
        return FromType(promotionPiece, piece.getTeamColor(), false);
    }
}
