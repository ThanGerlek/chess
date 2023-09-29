package chess;


import chess.pieces.*;

import java.util.Collection;

public abstract class ChessPieceImpl implements ChessPiece {

    private final PieceType type;
    private final ChessGame.TeamColor color;
    private boolean hasNeverMoved;

    public ChessPieceImpl(PieceType type, ChessGame.TeamColor color) {
        this.type = type;
        this.color = color;
        this.hasNeverMoved = true;
    }

    public static ChessPiece FromType(PieceType type, ChessGame.TeamColor color) {
        return switch (type) {
            case KING -> new King(color);
            case QUEEN -> new Queen(color);
            case BISHOP -> new Bishop(color);
            case KNIGHT -> new Knight(color);
            case ROOK -> new Rook(color);
            case PAWN -> new Pawn(color);
        };
    }

    /**
     * Returns a version of the given ChessPosition shifted by the given amounts.
     * Positive values shift up and right relative to the white player.
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
     * Returns a version of the given ChessPosition shifted by the given
     * amounts towards the opposing side.
     * Positive values shift up and right relative to the player who owns
     * this piece.
     *
     * @param position the starting ChessPosition.
     * @param deltaRow the amount to shift the row.
     * @param deltaCol the amount to shift the column.
     * @return a shifted ChessPosition.
     */
    protected ChessPosition shiftRelative(ChessPosition position, int deltaRow, int deltaCol) {
        if (color == ChessGame.TeamColor.WHITE)
            return shift(position, deltaRow, deltaCol);
        return shift(position, -deltaRow, -deltaCol);
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

    public void markAsHavingMoved() {
        hasNeverMoved = false;
    }

    /**
     * Calculates all the positions a chess piece can move to.
     * Does not take into account moves that are illegal due to leaving the king in
     * danger.
     *
     * @param board      the current ChessBoard.
     * @param myPosition this ChessPiece's current position.
     * @return Collection of valid moves.
     */
    @Override
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    protected boolean isValidEmptySpace(ChessBoard board, ChessPosition position) {
        return isOnBoard(position) && board.getPiece(position) == null;
    }

    // TODO? Make private, update Pawn to not use it?
    protected boolean isOnBoard(ChessPosition position) {
        return position.getRow() > 0 && position.getRow() < 9 && position.getColumn() > 0 && position.getColumn() < 9;
    }

    protected boolean hasNeverMoved() {
        return hasNeverMoved;
    }
}
