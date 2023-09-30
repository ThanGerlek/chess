package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class ChessGameImpl implements ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGameImpl() {
        board = new ChessBoardImpl();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return which team's turn it is.
     */
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which team's turn it is.
     *
     * @param team the team whose turn it is.
     */
    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    public void changeTeamTurn() {
        teamTurn = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
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
        Collection<ChessMove> validMoves = new HashSet<>();

        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
        for (ChessMove move : potentialMoves) {
            if (!wouldLeaveInCheck(move)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    private boolean wouldLeaveInCheck(ChessMove move) {
        TeamColor color = board.getPiece(move.getStartPosition()).getTeamColor();
        ChessPiece capturedPiece = board.forceApplyMove(move);
        boolean result = isInCheck(color);
        board.forceRestoreFromMove(move, capturedPiece);
        return result;
    }

    /**
     * Makes a move in a chess game.
     *
     * @param move chess move to perform.
     * @throws InvalidMoveException if move is invalid.
     */
    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();

        if (!board.hasPieceAt(startPosition)) {
            throw new InvalidMoveException("Called makeMove() on an empty position");
        }

        if (board.getPiece(startPosition).getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Called makeMove() on the opponent's turn");
        }

        if (!validMoves(startPosition).contains(move)) {
            throw new InvalidMoveException("Called makeMove() on an invalid move");
        }

        board.forceApplyMove(move);
        board.getPiece(move.getEndPosition()).markAsHavingMoved();
        changeTeamTurn();
    }

    /**
     * Determines if the given team is in check.
     *
     * @param teamColor which team to check for check.
     * @return true if the specified team is in check.
     */
    @Override
    public boolean isInCheck(TeamColor teamColor) {
        if (!board.containsKing(teamColor)) {
            return false;
        }

        ChessPosition kingPosition = board.getKingPosition(teamColor);
        TeamColor attackColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        return isPositionUnderAttackFrom(kingPosition, attackColor);
    }

    /**
     * Determines if the given team is in checkmate.
     *
     * @param teamColor which team to check for checkmate.
     * @return true if the specified team is in checkmate.
     */
    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        // TODO optimize?
        ChessPosition kingPosition = board.getKingPosition(teamColor);
        Collection<ChessPosition> potentialSpaces = new LinkedList<>();
        TeamColor attackColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        potentialSpaces.add(ChessPieceImpl.shift(kingPosition, 1, 1));
        potentialSpaces.add(ChessPieceImpl.shift(kingPosition, 1, 0));
        potentialSpaces.add(ChessPieceImpl.shift(kingPosition, 1, -1));
        potentialSpaces.add(ChessPieceImpl.shift(kingPosition, 0, 1));
        potentialSpaces.add(ChessPieceImpl.shift(kingPosition, 0, 0));
        potentialSpaces.add(ChessPieceImpl.shift(kingPosition, 0, -1));
        potentialSpaces.add(ChessPieceImpl.shift(kingPosition, -1, 1));
        potentialSpaces.add(ChessPieceImpl.shift(kingPosition, -1, 0));
        potentialSpaces.add(ChessPieceImpl.shift(kingPosition, -1, -1));

        for (ChessPosition position : potentialSpaces) {
            if (!isPositionUnderAttackFrom(position, attackColor)) return false;
        }
        return true;
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
        this.board = board;
    }

    private boolean isPositionUnderAttackFrom(ChessPosition position, TeamColor attackColor) {
        // TODO Implement caching?

        for (ChessPosition attackPosition : board.occupiedPositions()) {

            ChessPiece attacker = board.getPiece(attackPosition);
            if (attacker.getTeamColor() == attackColor) {
                Collection<ChessMove> attackerMoves = attacker.pieceMoves(board, attackPosition);
                ChessMove attackMove = new ChessMoveImpl(attackPosition, position);
                if (attackerMoves.contains(attackMove)) {
                    return true;
                }
            }
        }

        return false;
    }
}
