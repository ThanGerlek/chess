package chess.pieces;

import chess.*;

import java.util.Collection;

public class Knight extends ChessPieceImpl {
    public Knight(ChessGame.TeamColor color) {
        super(PieceType.KNIGHT, color);
    }

    /**
     * Calculates all the positions this chess piece can move to.
     * Does not take into account moves that are illegal due to leaving the king in
     * danger.
     *
     * @param board      the current ChessBoard.
     * @param myPosition this Knight's current position.
     * @return Collection of valid moves.
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
