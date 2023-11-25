package http;

import chess.*;
import chess.pieces.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.util.LinkedList;

public class ChessSerializer {

    public static Gson gson() {
        return getBuilder().create();
    }

    public static GsonBuilder getBuilder() {
        LinkedList<RuntimeTypeAdapterFactory> factories = new LinkedList<>();

        factories.push(RuntimeTypeAdapterFactory.of(ChessGame.class, "type").registerSubtype(ChessGameImpl.class));
        factories.push(RuntimeTypeAdapterFactory.of(ChessBoard.class, "type").registerSubtype(ChessBoardImpl.class));
        factories.push(RuntimeTypeAdapterFactory.of(ChessMove.class, "type").registerSubtype(ChessMoveImpl.class));
        factories.push(
                RuntimeTypeAdapterFactory.of(ChessPosition.class, "type").registerSubtype(ChessPositionImpl.class));
        factories.push(RuntimeTypeAdapterFactory.of(ChessPiece.class, "pieceType")
                .registerSubtype(King.class)
                .registerSubtype(Queen.class)
                .registerSubtype(Knight.class)
                .registerSubtype(Rook.class)
                .registerSubtype(Bishop.class)
                .registerSubtype(Pawn.class));

        GsonBuilder builder = new GsonBuilder();
        for (RuntimeTypeAdapterFactory factory : factories) {
            builder.registerTypeAdapterFactory(factory);
        }
        return builder;
    }
}
