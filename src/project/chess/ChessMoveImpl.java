package chess;

public class ChessMoveImpl implements ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMoveImpl(ChessPosition startPosition, ChessPosition endPosition) {
        this(startPosition, endPosition, null);
    }

    public ChessMoveImpl(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location.
     */
    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location.
     */
    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this chess move, or null otherwise.
     *
     * @return the type of piece to promote a pawn to, or null if no promotion.
     */
    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (hashCode() != o.hashCode()) return false;

        if (!(o instanceof ChessMove other)) return false;


        return other.getStartPosition().equals(getStartPosition()) && other.getEndPosition().equals(getEndPosition());

    }

    @Override
    public int hashCode() {
        return 12345 * getStartPosition().hashCode() + 67890 * getEndPosition().hashCode();
    }

    @Override
    public String toString() {
        String str = String.format("%s-%s", startPosition, endPosition);
        if (getPromotionPiece() != null) {
            str += "(" + getPromotionPiece().toString().substring(0, 2) + ")";
        }
        return str;
    }
}
