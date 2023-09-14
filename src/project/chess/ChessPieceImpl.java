package chess;

import startingClasses.*;

import java.util.Collection;

public class ChessPieceImpl implements ChessPiece {

    // TODO ChessPieceImpl

    /**
     * @return Which team this chess piece belongs to
     */
    @Override
    public ChessGame.TeamColor getTeamColor() {
        // TODO getTeamColor()
        return null;
    }

    /**
     * @return which type of chess piece this piece is
     */
    @Override
    public PieceType getPieceType() {
        // TODO getPieceType()
        return null;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @param board
     * @param myPosition
     * @return Collection of valid moves
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // TODO pieceMoves()
        return null;
    }
}
