package chess;

public class ChessPositionImpl implements ChessPosition {

    private final int row;
    private final int col;

    public ChessPositionImpl(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static ChessPosition shift(ChessPosition position, int deltaRow, int deltaCol) {
        return new ChessPositionImpl(position.getRow() + deltaRow, position.getColumn() + deltaCol);
    }

    /**
     * @return which row this position is in (1 codes for the bottom row).
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in (1 codes for the left row).
     */
    @Override
    public int getColumn() {
        return col;
    }

    /**
     * Returns this ChessPosition's representation in algebraic chess notation.
     */
    public String notation() {
        char colChar = (char) ('a' + col - 1);
        return "" + (Character) colChar + row;
    }
}
