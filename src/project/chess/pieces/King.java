package chess.pieces;

import chess.*;

import java.util.Collection;

public class King extends ChessPieceImpl {
    public King(ChessGame.TeamColor color) {
        super(PieceType.KING, color);
    }

    /**
     * Calculates all the positions this chess piece can move to.
     * Does not take into account moves that are illegal due to leaving the king in
     * danger.
     *
     * @param board      the current ChessBoard.
     * @param myPosition this King's current position.
     * @return a Collection of valid moves.
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        return null;
    }
}
