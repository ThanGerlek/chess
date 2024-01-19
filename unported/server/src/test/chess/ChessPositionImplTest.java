package chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChessPositionImplTest {

    @Test
    void bottom_left_is_a1() {
        ChessPositionImpl position = new ChessPositionImpl(1, 1);
        Assertions.assertEquals("a1", position.notation());
    }

    @Test
    void top_right_is_h8() {
        ChessPositionImpl position = new ChessPositionImpl(8, 8);
        Assertions.assertEquals("h8", position.notation());
    }

    @Test
    void top_left_is_a8() {
        ChessPositionImpl position = new ChessPositionImpl(8, 1);
        Assertions.assertEquals("a8", position.notation());
    }

    @Test
    void bottom_right_is_h1() {
        ChessPositionImpl position = new ChessPositionImpl(1, 8);
        Assertions.assertEquals("h1", position.notation());
    }

    @Test
    void positions_with_the_same_row_and_col_are_equal() {
        ChessPosition pos1 = new ChessPositionImpl(2, 2);
        ChessPosition pos2 = new ChessPositionImpl(2, 2);
        Assertions.assertEquals(pos1, pos2);
    }

    @Test
    void positions_with_the_same_row_and_col_have_equal_hash_codes() {
        ChessPosition pos1 = new ChessPositionImpl(2, 2);
        ChessPosition pos2 = new ChessPositionImpl(2, 2);
        Assertions.assertEquals(pos1.hashCode(), pos2.hashCode());
    }

    @Test
    void positions_with_different_row_are_unequal() {
        ChessPosition pos1 = new ChessPositionImpl(2, 2);
        ChessPosition pos2 = new ChessPositionImpl(4, 2);
        Assertions.assertNotEquals(pos1, pos2);
    }

    @Test
    void positions_with_different_col_are_unequal() {
        ChessPosition pos1 = new ChessPositionImpl(2, 2);
        ChessPosition pos2 = new ChessPositionImpl(2, 4);
        Assertions.assertNotEquals(pos1, pos2);
    }

    @Test
    void positions_with_different_row_have_different_hash_codes() {
        ChessPosition pos1 = new ChessPositionImpl(2, 2);
        ChessPosition pos2 = new ChessPositionImpl(4, 2);
        Assertions.assertNotEquals(pos1.hashCode(), pos2.hashCode());
    }

    @Test
    void positions_with_different_col_have_different_hash_codes() {
        ChessPosition pos1 = new ChessPositionImpl(2, 2);
        ChessPosition pos2 = new ChessPositionImpl(2, 4);
        Assertions.assertNotEquals(pos1.hashCode(), pos2.hashCode());
    }
}