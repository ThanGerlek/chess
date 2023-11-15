package chess;

import chess.pieces.Queen;
import chess.pieces.Rook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChessBoardImplTest {

    @Test
    void adding_one_piece_to_an_empty_board_then_getting_it_returns_that_piece() {
        ChessBoardImpl board = givenAnEmptyBoard();

        ChessPosition position = new ChessPositionImpl(2, 2);
        ChessPiece piece = new Rook(ChessGame.TeamColor.WHITE);
        board.addPiece(position, piece);

        Assertions.assertEquals(piece, board.getPiece(position));
    }

    public ChessBoardImpl givenAnEmptyBoard() {
        return new ChessBoardImpl();
    }

    @Test
    void hasPiece_returns_true_after_adding_piece_there() {
        ChessBoardImpl board = givenAnEmptyBoard();

        ChessPosition position = new ChessPositionImpl(2, 2);
        ChessPiece piece = new Rook(ChessGame.TeamColor.WHITE);
        board.addPiece(position, piece);

        Assertions.assertTrue(board.hasPieceAt(position));
    }

    @Test
    void hasPiece_returns_false_after_adding_piece_in_a_different_location() {
        ChessBoardImpl board = givenAnEmptyBoard();

        ChessPosition position1 = new ChessPositionImpl(2, 2);
        ChessPiece piece = new Rook(ChessGame.TeamColor.WHITE);
        board.addPiece(position1, piece);

        ChessPosition position2 = new ChessPositionImpl(3, 2);
        Assertions.assertFalse(board.hasPieceAt(position2));
    }

    @Test
    void adding_two_pieces_to_an_empty_board_then_getting_each_returns_each() {
        ChessBoardImpl board = givenAnEmptyBoard();

        ChessPosition position1 = new ChessPositionImpl(2, 2);
        ChessPiece piece1 = new Rook(ChessGame.TeamColor.WHITE);
        board.addPiece(position1, piece1);
        ChessPosition position2 = new ChessPositionImpl(5, 5);
        ChessPiece piece2 = new Queen(ChessGame.TeamColor.BLACK);
        board.addPiece(position2, piece2);

        Assertions.assertEquals(piece1, board.getPiece(position1));
        Assertions.assertEquals(piece2, board.getPiece(position2));
    }

    @Test
    void adding_a_piece_where_a_piece_already_exists_throws_an_error() {
        ChessBoardImpl board = givenAnEmptyBoard();
        ChessPosition position = new ChessPositionImpl(2, 2);
        ChessPiece piece = new Rook(ChessGame.TeamColor.WHITE);
        board.addPiece(position, piece);

        Assertions.assertThrows(IllegalArgumentException.class, () -> board.addPiece(position, piece));
    }

    @Test
    void getting_a_piece_from_an_empty_board_returns_null() {
        ChessBoardImpl board = givenAnEmptyBoard();
        ChessPosition position = new ChessPositionImpl(2, 2);
        ChessPiece piece = board.getPiece(position);

        Assertions.assertNull(piece);
    }

    @Test
    void calling_hasPiece_on_a_board_with_one_piece_returns_true_for_that_position() {
        ChessBoardImpl board = givenAnEmptyBoard();
        ChessPosition position = new ChessPositionImpl(2, 2);
        ChessPiece piece = new Rook(ChessGame.TeamColor.WHITE);
        board.addPiece(position, piece);

        Assertions.assertTrue(board.hasPieceAt(position));
    }

    @Test
    void calling_hasPiece_on_a_board_with_one_piece_returns_false_for_adjacent_positions() {
        ChessBoardImpl board = givenAnEmptyBoard();
        ChessPosition position = new ChessPositionImpl(3, 3);
        ChessPiece piece = board.getPiece(position);
        board.addPiece(position, piece);

        ChessPosition abovePos = new ChessPositionImpl(4, 3);
        ChessPosition belowPos = new ChessPositionImpl(2, 3);
        ChessPosition rightPos = new ChessPositionImpl(3, 4);
        ChessPosition leftPos = new ChessPositionImpl(3, 2);

        Assertions.assertFalse(board.hasPieceAt(abovePos));
        Assertions.assertFalse(board.hasPieceAt(belowPos));
        Assertions.assertFalse(board.hasPieceAt(rightPos));
        Assertions.assertFalse(board.hasPieceAt(leftPos));
    }

    @Test
    void calling_hasPiece_on_an_empty_board_always_returns_false() {
        ChessBoardImpl board = givenAnEmptyBoard();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPositionImpl(row, col);
                Assertions.assertFalse(board.hasPieceAt(position));
            }
        }
    }

    @Test
    void calling_hasPiece_on_a_setup_board_returns_true_for_rows_1_2_7_and_8() {
        ChessBoardImpl board = givenASetupBoard();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                boolean expectedHasPiece = row <= 2 || row >= 7;
                boolean actualHasPiece = board.hasPieceAt(new ChessPositionImpl(row, col));
                Assertions.assertEquals(expectedHasPiece, actualHasPiece);
            }
        }
    }

    public ChessBoardImpl givenASetupBoard() {
        ChessBoardImpl board = new ChessBoardImpl();
        board.resetBoard();
        return board;
    }

    @Test
    void removing_a_piece_means_the_piece_is_not_there() {
        ChessBoardImpl board = givenASetupBoard();
        ChessPositionImpl position = new ChessPositionImpl(2, 2);
        board.removePiece(position);
        Assertions.assertFalse(board.hasPieceAt(position));
    }

    @Test
    void printing_an_empty_board() {
        String expected = """
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                """;
        ChessBoardImpl board = givenAnEmptyBoard();
        Assertions.assertEquals(expected, board.toString());
    }

    @Test
    void printing_a_reset_board() {
        String expected = """
                |r|n|b|q|k|b|n|r|
                |p|p|p|p|p|p|p|p|
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                |P|P|P|P|P|P|P|P|
                |R|N|B|Q|K|B|N|R|
                """;
        ChessBoardImpl board = givenASetupBoard();
        Assertions.assertEquals(expected, board.toString());
    }
}