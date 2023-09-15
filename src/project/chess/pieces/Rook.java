package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.LinkedList;

public class Rook extends ChessPieceImpl {
    public Rook(ChessGame.TeamColor color) {
        super(PieceType.ROOK, color);
    }

    // TODO castling

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
        Collection<ChessMove> moves = new LinkedList<>();
        for (ChessPosition endPosition : getMoveEndPositions(myPosition))
            if (isValidMoveEndPosition(board, endPosition)) moves.add(new ChessMoveImpl(myPosition, endPosition));

        return moves;
    }

    private Collection<ChessPosition> getMoveEndPositions(ChessPosition myPosition) {
        Collection<ChessPosition> endPositions = new LinkedList<>();
        for (int distance = 1; distance < 8; distance++)
            endPositions.addAll(getEndPositionsOfDistance(distance, myPosition));

        return endPositions;
    }

    private Collection<ChessPosition> getEndPositionsOfDistance(int dist, ChessPosition myPosition) {
        Collection<ChessPosition> endPositions = new LinkedList<>();

        endPositions.add(shift(myPosition, -dist, 0));
        endPositions.add(shift(myPosition, dist, 0));
        endPositions.add(shift(myPosition, 0, -dist));
        endPositions.add(shift(myPosition, 0, dist));

        return endPositions;
    }
}
