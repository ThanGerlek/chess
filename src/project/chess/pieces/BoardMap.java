package chess.pieces;

import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessPositionImpl;

import java.util.HashSet;
import java.util.Set;

public class BoardMap {
    private final ChessPiece[] board = new ChessPiece[64];

    public void remove(ChessPosition pos) {
        board[index(pos)] = null;
    }

    private int index(ChessPosition pos) {
        return (pos.getRow() - 1) * 8 + (pos.getColumn() - 1);
    }

    public void put(ChessPosition pos, ChessPiece piece) {
        board[index(pos)] = piece;
    }

    public ChessPiece get(ChessPosition pos) {
        return board[index(pos)];
    }

    public Set<ChessPosition> keySet() {
        Set<ChessPosition> positions = new HashSet<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                positions.add(new ChessPositionImpl(row, col));
            }
        }
        return positions;
    }
}
