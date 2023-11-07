package server;

import chess.*;
import chess.pieces.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

public class ChessSerializer {

    public static Gson gson() {
        var boardRTAF = RuntimeTypeAdapterFactory.of(ChessBoard.class, "type").registerSubtype(ChessBoardImpl.class);
        var positionRTAF =
                RuntimeTypeAdapterFactory.of(ChessPosition.class, "type").registerSubtype(ChessPositionImpl.class);
        var moveRTAF = RuntimeTypeAdapterFactory.of(ChessMove.class, "type").registerSubtype(ChessMoveImpl.class);
        var pieceRTAF = RuntimeTypeAdapterFactory.of(ChessPiece.class, "pieceType")
                .registerSubtype(King.class)
                .registerSubtype(Queen.class)
                .registerSubtype(Knight.class)
                .registerSubtype(Rook.class)
                .registerSubtype(Bishop.class)
                .registerSubtype(Pawn.class);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(boardRTAF);
        builder.registerTypeAdapterFactory(positionRTAF);
        builder.registerTypeAdapterFactory(moveRTAF);
        builder.registerTypeAdapterFactory(pieceRTAF);
        return builder.create();
    }
}
