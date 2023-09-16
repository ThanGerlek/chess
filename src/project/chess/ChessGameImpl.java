package chess;

import java.util.Collection;

public class ChessGameImpl implements ChessGame {

    private ChessBoardImpl board;
    private boolean isWhitesTurn;

    public ChessGameImpl() {
        board = new ChessBoardImpl();
        isWhitesTurn = true;
    }

    /**
     * @return which team's turn it is.
     */
    @Override
    public TeamColor getTeamTurn() {
        return isWhitesTurn ? TeamColor.WHITE : TeamColor.BLACK;
    }

    /**
     * Sets which team's turn it is.
     *
     * @param team the team whose turn it is.
     */
    @Override
    public void setTeamTurn(TeamColor team) {
        isWhitesTurn = team == TeamColor.WHITE;
    }

    /**
     * Gets all valid moves for a piece at the given location.
     *
     * @param startPosition the piece to get valid moves for.
     * @return a Collection of valid moves for requested piece, or null if no piece at
     * startPosition.
     */
    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // TODO validMoves()
        return null;
    }

    /**
     * Makes a move in a chess game.
     *
     * @param move chess move to perform.
     * @throws InvalidMoveException if move is invalid.
     */
    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //TODO makeMove()
        // Don't forget to update flags: let the piece know it's been
        // moved, and let the Board know if the king has been moved
    }

    /**
     * Determines if the given team is in check.
     *
     * @param teamColor which team to check for check.
     * @return true if the specified team is in check.
     */
    @Override
    public boolean isInCheck(TeamColor teamColor) {
        // TODO isInCheck()
        return false;
    }

    /**
     * Determines if the given team is in checkmate.
     *
     * @param teamColor which team to check for checkmate.
     * @return true if the specified team is in checkmate.
     */
    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        // TODO isInCheckmate()
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves.
     *
     * @param teamColor which team to check for stalemate.
     * @return true if the specified team is in stalemate.
     */
    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        // TODO isInStaleMate
        return false;
    }

    /**
     * Gets the current chessboard.
     *
     * @return the chessboard.
     */
    @Override
    public ChessBoard getBoard() {
        return board;
    }

    /**
     * Sets this game's chessboard with a given board.
     *
     * @param board the new board to use.
     */
    @Override
    public void setBoard(ChessBoard board) {
        // TODO setBoard()   Problem! BoardImpl?
    }
}
