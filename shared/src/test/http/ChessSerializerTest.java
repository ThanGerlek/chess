package http;

import chess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChessSerializerTest {
    @Test
    void serialize_and_deserialize_move_with_null_promotion_piece() {
        ChessPosition startPos = new ChessPositionImpl(2, 1);
        ChessPosition endPos = new ChessPositionImpl(4, 1);
        ChessMove move = new ChessMoveImpl(startPos, endPos);
        String json = ChessSerializer.gson().toJson(move);

        System.out.println(json);

        ChessMove parsedMove = ChessSerializer.gson().fromJson(json, ChessMove.class);
        Assertions.assertEquals(move, parsedMove);
    }

    @Test
    void serialize_and_deserialize_move_with_queen_promotion_piece() {
        ChessPosition startPos = new ChessPositionImpl(2, 1);
        ChessPosition endPos = new ChessPositionImpl(4, 1);
        ChessPiece.PieceType promotionPiece = ChessPiece.PieceType.QUEEN;
        ChessMove move = new ChessMoveImpl(startPos, endPos, promotionPiece);
        String json = ChessSerializer.gson().toJson(move);

        System.out.println(json);

        ChessMove parsedMove = ChessSerializer.gson().fromJson(json, ChessMove.class);
        Assertions.assertEquals(move, parsedMove);
    }

    @Test
    void serialize_and_deserialize_position() {
        ChessPosition pos = new ChessPositionImpl(2, 1);
        String json = ChessSerializer.gson().toJson(pos);

        System.out.println(json);

        ChessPosition parsedPos = ChessSerializer.gson().fromJson(json, ChessPosition.class);
        Assertions.assertEquals(pos, parsedPos);
    }
}