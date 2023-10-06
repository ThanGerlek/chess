package chess;


import java.util.Collection;
import java.util.LinkedList;

public abstract class ChessPieceImpl implements ChessPiece {

    private final PieceType type;
    private final ChessGame.TeamColor color;
    private boolean hasNeverMoved;

    public ChessPieceImpl(PieceType type, ChessGame.TeamColor color) {
        this(type, color, true);
    }

    public ChessPieceImpl(PieceType type, ChessGame.TeamColor color, boolean hasNeverMoved) {
        this.type = type;
        this.color = color;
        this.hasNeverMoved = hasNeverMoved;
    }

    /**
     * Returns a version of the given ChessPosition shifted by the given amounts towards the opposing side. Positive
     * values shift up and right relative to the player who owns this piece.
     *
     * @param position the starting ChessPosition.
     * @param deltaRow the amount to shift the row.
     * @param deltaCol the amount to shift the column.
     * @return a shifted ChessPosition.
     */
    protected ChessPosition shiftRelative(ChessPosition position, int deltaRow, int deltaCol) {
        if (color == ChessGame.TeamColor.WHITE) {
            return shift(position, deltaRow, deltaCol);
        } else {
            return shift(position, -deltaRow, -deltaCol);
        }
    }

    /**
     * Returns a version of the given ChessPosition shifted by the given amounts. Positive values shift up and right
     * relative to the white player.
     *
     * @param position the starting ChessPosition.
     * @param deltaRow the amount to shift the row.
     * @param deltaCol the amount to shift the column.
     * @return a shifted ChessPosition.
     */
    protected static ChessPosition shift(ChessPosition position, int deltaRow, int deltaCol) {
        return new ChessPositionImpl(position.getRow() + deltaRow, position.getColumn() + deltaCol);
    }

    /**
     * Returns a Collection of all ChessMoves obtained by repeatedly applying the given RelativeChessMove to the given
     * starting ChessPosition any number of times until an invalid move is made. Application stops when applying the
     * RelativeChessMove again would land either outside the board, on an occupied space, or back on the start position
     * (the start position is not included in the Collection). If the final move is a capture (one that ends on a space
     * occupied by an enemy piece), it is included in the Collection, but counts as an end condition and the
     * relativeMove is not applied again.
     *
     * @param board         the ChessBoard.
     * @param startPosition the starting ChessPosition.
     * @param relativeMove  the RelativeChessMove to apply to the startPosition.
     * @return a Collection of ChessMoves made by repeatedly applying relativeMove.
     */
    protected Collection<ChessMove> getMovesFromRepeatedRelativeMove(ChessBoard board, ChessPosition startPosition,
            RelativeChessMove relativeMove) {
        Collection<ChessMove> moves = new LinkedList<>();

        ChessPosition currentEndPosition = relativeMove.apply(startPosition);

        while (isValidEmptySpace(board, currentEndPosition) && !currentEndPosition.equals(startPosition)) {
            moves.add(new ChessMoveImpl(startPosition, currentEndPosition));
            currentEndPosition = relativeMove.apply(currentEndPosition);
        }

        ChessPiece previousOccupant = board.getPiece(currentEndPosition);
        if (previousOccupant != null && previousOccupant.getTeamColor() != getTeamColor()) {
            moves.add(new ChessMoveImpl(startPosition, currentEndPosition));
        }

        return moves;
    }

    protected boolean isValidEmptySpace(ChessBoard board, ChessPosition position) {
        return isOnBoard(position) && board.getPiece(position) == null;
    }

    /**
     * @return which team this chess piece belongs to.
     */
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is.
     */
    @Override
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to. Does not take into account moves that are illegal due to
     * leaving the king in danger.
     *
     * @param board      the current ChessBoard.
     * @param myPosition this ChessPiece's current position.
     * @return Collection of valid moves.
     */
    @Override
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    @Override
    public ChessPiece copy() {
        return ChessPieces.FromType(getPieceType(), getTeamColor(), hasNeverMoved());
    }

    public void markAsHavingMoved() {
        hasNeverMoved = false;
    }

    protected boolean hasNeverMoved() {
        return hasNeverMoved;
    }

    private boolean isOnBoard(ChessPosition position) {
        return position.getRow() > 0 && position.getRow() < 9 && position.getColumn() > 0 && position.getColumn() < 9;
    }

    protected boolean isValidCapturingSpace(ChessBoard board, ChessPosition position) {
        return isOnBoard(position) && board.getPiece(position) != null &&
                board.getPiece(position).getTeamColor() != getTeamColor();
    }
}
