package chess;

public class ChessPositionImpl implements ChessPosition {

    private final int row;
    private final int col;

    public ChessPositionImpl(int row, int col) {
        this.row = row;
        this.col = col;
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
     * @return this ChessPosition's representation in algebraic chess notation.
     */
    public String notation() {
        char colChar = (char) ('a' + col - 1);
        return "" + colChar + row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o.getClass() != this.getClass()) return false;

        ChessPositionImpl other = (ChessPositionImpl) o;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public int hashCode() {
        return 9876000 + row * 16 + col;
    }
}
