package chess;

import startingClasses.ChessMove;
import startingClasses.ChessPiece;
import startingClasses.ChessPosition;

public class ChessMoveImpl implements ChessMove {

    // TODO ChessMoveImpl

    /**
     * @return ChessPosition of starting location
     */
    @Override
    public ChessPosition getStartPosition() {
        // TODO getStartPosition()
        return null;
    }

    /**
     * @return ChessPosition of ending location
     */
    @Override
    public ChessPosition getEndPosition() {
        // TODO getEndPosition()
        return null;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        // TODO getPromotionPiece()
        return null;
    }
}
