package http;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChessSerializerTest {
    @Test
    void serializeAndDeserializeMoveWithNullPromotionPiece() {
        ChessPosition startPos = new ChessPosition(2, 1);
        ChessPosition endPos = new ChessPosition(4, 1);
        ChessMove move = new ChessMove(startPos, endPos);
        String json = ChessSerializer.gson().toJson(move);

        System.out.println(json);

        ChessMove parsedMove = ChessSerializer.gson().fromJson(json, ChessMove.class);
        Assertions.assertEquals(move, parsedMove);
    }

    @Test
    void serializeAndDeserializeMoveWithQueenPromotionPiece() {
        ChessPosition startPos = new ChessPosition(2, 1);
        ChessPosition endPos = new ChessPosition(4, 1);
        ChessPiece.PieceType promotionPiece = ChessPiece.PieceType.QUEEN;
        ChessMove move = new ChessMove(startPos, endPos, promotionPiece);
        String json = ChessSerializer.gson().toJson(move);

        System.out.println(json);

        ChessMove parsedMove = ChessSerializer.gson().fromJson(json, ChessMove.class);
        Assertions.assertEquals(move, parsedMove);
    }

    @Test
    void serializeAndDeserializePosition() {
        ChessPosition pos = new ChessPosition(2, 1);
        String json = ChessSerializer.gson().toJson(pos);

        System.out.println(json);

        ChessPosition parsedPos = ChessSerializer.gson().fromJson(json, ChessPosition.class);
        Assertions.assertEquals(pos, parsedPos);
    }
}
