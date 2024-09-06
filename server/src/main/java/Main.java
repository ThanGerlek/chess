import chess.ChessGame;
import chess.ChessPiece;
import server.Server;

public class Main {
    private static final int DEFAULT_PORT = 8081;


    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        new Server().run(DEFAULT_PORT);
    }
}