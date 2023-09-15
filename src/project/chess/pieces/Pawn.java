package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.LinkedList;

public class Pawn extends ChessPieceImpl {
    public Pawn(ChessGame.TeamColor color) {
        super(PieceType.PAWN, color);
    }

    // TODO en passant
    //  ??? 3-valued variable (left, right, none) that the Board can set
    //  when this pawn becomes capable of attacking? Make sure it
    //  resets it afterward!

    // TODO Promotion pieces?

    /**
     * Calculates all the positions this chess piece can move to.
     * Does not take into account moves that are illegal due to leaving the king in
     * danger.
     *
     * @param board      the current ChessBoard.
     * @param myPosition this Pawn's current position.
     * @return a Collection of valid moves.
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessPosition> endPositions = new LinkedList<>();

        addOneStepMoveIfValid(endPositions, board, myPosition);
        addTwoStepMoveIfValid(endPositions, board, myPosition);
        addDiagonalAttackMoveIfValid(endPositions, board, myPosition, 1);
        addDiagonalAttackMoveIfValid(endPositions, board, myPosition, -1);

        Collection<ChessMove> moves = new LinkedList<>();
        for (ChessPosition endPosition : endPositions) {
            moves.add(new ChessMoveImpl(myPosition, endPosition));
        }

        return moves;
    }


    private void addOneStepMoveIfValid(Collection<ChessPosition> endPositions, ChessBoard board, ChessPosition myPosition) {
        ChessPosition endPosition = shiftRelative(myPosition, 1, 0);
        if (isOnBoard(endPosition) && board.getPiece(endPosition) == null)
            endPositions.add(endPosition);
    }

    private void addTwoStepMoveIfValid(Collection<ChessPosition> endPositions, ChessBoard board, ChessPosition myPosition) {
        ChessPosition endPosition = shiftRelative(myPosition, 2, 0);
        if (isOnBoard(endPosition)
                && board.getPiece(endPosition) == null
                && isPawnStartingSquare(myPosition)) {
            endPositions.add(endPosition);
        }
    }

    private void addDiagonalAttackMoveIfValid(Collection<ChessPosition> endPositions, ChessBoard board, ChessPosition myPosition, int direction) {
        int deltaCol = (direction > 0) ? 1 : -1;
        ChessPosition endPosition = shiftRelative(myPosition, 1, deltaCol);
        if (isOnBoard(endPosition)
                && board.getPiece(endPosition) != null
                && board.getPiece(endPosition).getTeamColor() != getTeamColor()) {
            endPositions.add(endPosition);
        }
    }

    private boolean isPawnStartingSquare(ChessPosition myPosition) {
        return (getTeamColor() == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2)
                || (getTeamColor() == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7);
    }

    @Override
    protected boolean isValidMoveEndPosition(ChessBoard board, ChessPosition position) {
        return isOnBoard(position);
    }
}