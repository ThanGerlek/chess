package chess;

import java.util.Collection;

/**
 * A chessboard that can hold and rearrange chess pieces. Note: You can add to this interface, but you should not alter
 * the existing methods.
 */
public interface ChessBoard {

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    void addPiece(ChessPosition position, ChessPiece piece);

    void removePiece(ChessPosition position);


    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that position
     */
    ChessPiece getPiece(ChessPosition position);

    boolean hasPieceAt(ChessPosition position);

    /**
     * Sets the board to the default starting board (How the game of chess normally starts)
     */
    void resetBoard();

    ChessPosition getKingPosition(ChessGame.TeamColor color) throws InvalidBoardException;

    Collection<ChessPosition> getTeamPieces(ChessGame.TeamColor teamColor);

    ChessPiece forceApplyMove(ChessMove move);

    void forceRestoreFromMove(ChessMove move, ChessPiece capturedPiece);

    boolean containsKing(ChessGame.TeamColor color);

}