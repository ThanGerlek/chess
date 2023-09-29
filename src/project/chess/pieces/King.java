package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.LinkedList;

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
        Collection<ChessMove> moves = new LinkedList<>();
        for (ChessPosition endPosition : getPotentialEndPositions(myPosition))
            if (isValidEndPosition(board, endPosition)) {
                moves.add(new ChessMoveImpl(myPosition, endPosition));
            }

        return moves;
    }

    private Collection<ChessPosition> getPotentialEndPositions(ChessPosition position) {
        Collection<ChessPosition> positions = new LinkedList<>();
        positions.add(shift(position, -1, -1));
        positions.add(shift(position, -1, 0));
        positions.add(shift(position, -1, 1));
        positions.add(shift(position, 1, -1));
        positions.add(shift(position, 1, 0));
        positions.add(shift(position, 1, 1));
        positions.add(shift(position, 0, -1));
        positions.add(shift(position, 0, 1));
        return positions;
    }

    private boolean isValidEndPosition(ChessBoard board, ChessPosition position) {
        if (isValidEmptySpace(board, position))
            return true;
        ChessPiece previousOccupant = board.getPiece(position);
        return previousOccupant != null && previousOccupant.getTeamColor() != getTeamColor();
    }
}
