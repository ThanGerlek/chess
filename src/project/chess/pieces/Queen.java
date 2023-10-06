package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.LinkedList;

public class Queen extends ChessPieceImpl {

    public Queen(ChessGame.TeamColor color, boolean hasNeverMoved) {
        super(PieceType.QUEEN, color, hasNeverMoved);
    }

    public Queen(ChessGame.TeamColor color) {
        super(PieceType.QUEEN, color);
    }

    /**
     * Calculates all the positions this chess piece can move to. Does not take into account moves that are illegal due
     * to leaving the king in danger.
     *
     * @param board      the current ChessBoard.
     * @param myPosition this Queen's current position.
     * @return a Collection of valid moves.
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new LinkedList<>();

        RelativeChessMove moveUp = pos -> shift(pos, 1, 0);
        RelativeChessMove moveDown = pos -> shift(pos, -1, 0);
        RelativeChessMove moveLeft = pos -> shift(pos, 0, -1);
        RelativeChessMove moveRight = pos -> shift(pos, 0, 1);

        RelativeChessMove moveUpLeft = pos -> shift(pos, 1, -1);
        RelativeChessMove moveUpRight = pos -> shift(pos, 1, 1);
        RelativeChessMove moveDownLeft = pos -> shift(pos, -1, -1);
        RelativeChessMove moveDownRight = pos -> shift(pos, -1, 1);

        moves.addAll(getMovesFromRepeatedRelativeMove(board, myPosition, moveUp));
        moves.addAll(getMovesFromRepeatedRelativeMove(board, myPosition, moveDown));
        moves.addAll(getMovesFromRepeatedRelativeMove(board, myPosition, moveLeft));
        moves.addAll(getMovesFromRepeatedRelativeMove(board, myPosition, moveRight));

        moves.addAll(getMovesFromRepeatedRelativeMove(board, myPosition, moveUpLeft));
        moves.addAll(getMovesFromRepeatedRelativeMove(board, myPosition, moveUpRight));
        moves.addAll(getMovesFromRepeatedRelativeMove(board, myPosition, moveDownLeft));
        moves.addAll(getMovesFromRepeatedRelativeMove(board, myPosition, moveDownRight));

        return moves;
    }

}
