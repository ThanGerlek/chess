package chess;

public class ChessMoveImpl implements ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMoveImpl(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    public ChessMoveImpl(ChessPosition startPosition, ChessPosition endPosition) {
        this(startPosition, endPosition, null);
    }

    /**
     * @return ChessPosition of starting location
     */
    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }
}
