package chess;


import chess.pieces.*;

import java.util.Collection;

public abstract class ChessPieceImpl implements ChessPiece {

    private final PieceType type;
    private final ChessGame.TeamColor color;

    public ChessPieceImpl(PieceType type, ChessGame.TeamColor color) {
        this.type = type;
        this.color = color;
    }

    public static ChessPiece FromType(PieceType type, ChessGame.TeamColor color) {
        return switch (type) {
            case KING -> new King(color);
            case QUEEN -> new Queen(color);
            case BISHOP -> new Bishop(color);
            case KNIGHT -> new Knight(color);
            case ROOK -> new Rook(color);
            case PAWN -> new Pawn(color);
        };
    }

    /**
     * @return Which team this chess piece belongs to
     */
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    @Override
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to.
     * Does not take into account moves that are illegal due to leaving the king in
     * danger.
     *
     * @param board      the current ChessBoard.
     * @param myPosition this ChessPiece's current position.
     * @return Collection of valid moves.
     */
    @Override
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
