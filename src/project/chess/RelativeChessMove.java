package chess;

@FunctionalInterface
public interface RelativeChessMove {
    ChessPosition apply(ChessPosition position);
}
