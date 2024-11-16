package chess.movementrule;

import chess.ChessPosition;

@FunctionalInterface
public interface RelativeChessMove {
    ChessPosition apply(ChessPosition position);
}
