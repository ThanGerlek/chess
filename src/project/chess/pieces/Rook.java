package chess.pieces;

import chess.*;

import java.util.Collection;

public class Rook extends ChessPieceImpl {
    public Rook(ChessGame.TeamColor color) {
        super(PieceType.ROOK, color);
    }

    /**
     * Calculates all the positions this chess piece can move to.
     * Does not take into account moves that are illegal due to leaving the king in
     * danger.
     *
     * @param board      the current ChessBoard.
     * @param myPosition this Rook's current position.
     * @return a Collection of valid moves.
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
