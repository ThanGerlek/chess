package chess;

import chess.pieces.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ChessBoardImpl implements ChessBoard {

    private Map<ChessPosition, ChessPiece> pieces;

    public ChessBoardImpl() {
        this.pieces = new HashMap<>();
    }

    /**
     * Adds a chess piece to the chessboard.
     *
     * @param position at which to add the piece.
     * @param piece    the piece to add.
     */
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (hasPieceAt(position)) {
            String errMsg = String.format("Tried to add a piece to a nonempty position. Position: '%s', piece to add: '%s', existing piece: '%s'",
                    position.toString(), piece.toString(), getPiece(position).toString());
            throw new IllegalArgumentException(errMsg);
        }
        pieces.put(position, piece);
    }

    @Override
    public void removePiece(ChessPosition position) {
        pieces.remove(position);
    }

    /**
     * Gets a chess piece on the chessboard.
     *
     * @param position the position to get the piece from.
     * @return the piece at the position, or null if no piece is at that
     * position.
     */
    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return pieces.get(position);
    }

    public boolean hasPieceAt(ChessPosition position) {
        return pieces.get(position) != null;
    }

    /**
     * Sets the board to the default starting board
     * (how the game of chess normally starts).
     */
    @Override
    public void resetBoard() {
        clearBoard();
        placePawns();
        placeKnights();
        placeBishops();
        placeRooks();
        placeRoyals();
    }

    public void clearBoard() {
        pieces = new HashMap<>();
    }

    @Override
    public Collection<ChessPosition> occupiedPositions() {
        return pieces.keySet();
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor color) {
        for (ChessPosition position : pieces.keySet()) {
            ChessPiece piece = getPiece(position);
            if (piece.getPieceType() == ChessPiece.PieceType.KING
                    && piece.getTeamColor() == color) {
                return position;
            }
        }
        throw new InvalidBoardException("Called getKingPosition() but no King piece was found");
    }

    public ChessPiece forceApplyMove(ChessMove move) {
        ChessPiece capturedPiece = getPiece(move.getEndPosition());
        if (capturedPiece != null) {
            capturedPiece = capturedPiece.copy();
        }
        removePiece(move.getEndPosition());

        ChessPiece piece = getPiece(move.getStartPosition()).copy();
        removePiece(move.getStartPosition());

        addPiece(move.getEndPosition(), piece);
        return capturedPiece;
    }

    public void forceRestoreFromMove(ChessMove move, ChessPiece capturedPiece) {
        ChessPiece piece = getPiece(move.getEndPosition()).copy();
        removePiece(move.getEndPosition());

        addPiece(move.getStartPosition(), piece);
        if (capturedPiece != null) {
            addPiece(move.getEndPosition(), capturedPiece);
        }
    }

    private void placePawns() {
        for (int col = 1; col <= 8; col++) {
            Pawn whitePawn = new Pawn(ChessGame.TeamColor.WHITE);
            Pawn blackPawn = new Pawn(ChessGame.TeamColor.BLACK);
            ChessPosition whitePosition = new ChessPositionImpl(2, col);
            ChessPosition blackPosition = new ChessPositionImpl(7, col);
            pieces.put(whitePosition, whitePawn);
            pieces.put(blackPosition, blackPawn);
        }
    }

    private void placeKnights() {
        Knight whiteKnight1 = new Knight(ChessGame.TeamColor.WHITE);
        Knight whiteKnight2 = new Knight(ChessGame.TeamColor.WHITE);
        Knight blackKnight1 = new Knight(ChessGame.TeamColor.BLACK);
        Knight blackKnight2 = new Knight(ChessGame.TeamColor.BLACK);
        ChessPosition whiteKnightPosition1 = new ChessPositionImpl(1, 2);
        ChessPosition whiteKnightPosition2 = new ChessPositionImpl(1, 7);
        ChessPosition blackKnightPosition1 = new ChessPositionImpl(8, 2);
        ChessPosition blackKnightPosition2 = new ChessPositionImpl(8, 7);
        pieces.put(whiteKnightPosition1, whiteKnight1);
        pieces.put(whiteKnightPosition2, whiteKnight2);
        pieces.put(blackKnightPosition1, blackKnight1);
        pieces.put(blackKnightPosition2, blackKnight2);
    }

    private void placeBishops() {
        Bishop whiteBishop1 = new Bishop(ChessGame.TeamColor.WHITE);
        Bishop whiteBishop2 = new Bishop(ChessGame.TeamColor.WHITE);
        Bishop blackBishop1 = new Bishop(ChessGame.TeamColor.BLACK);
        Bishop blackBishop2 = new Bishop(ChessGame.TeamColor.BLACK);
        ChessPosition whiteBishopPosition1 = new ChessPositionImpl(1, 3);
        ChessPosition whiteBishopPosition2 = new ChessPositionImpl(1, 6);
        ChessPosition blackBishopPosition1 = new ChessPositionImpl(8, 3);
        ChessPosition blackBishopPosition2 = new ChessPositionImpl(8, 6);
        pieces.put(whiteBishopPosition1, whiteBishop1);
        pieces.put(whiteBishopPosition2, whiteBishop2);
        pieces.put(blackBishopPosition1, blackBishop1);
        pieces.put(blackBishopPosition2, blackBishop2);
    }

    private void placeRooks() {
        Rook whiteRook1 = new Rook(ChessGame.TeamColor.WHITE);
        Rook whiteRook2 = new Rook(ChessGame.TeamColor.WHITE);
        Rook blackRook1 = new Rook(ChessGame.TeamColor.BLACK);
        Rook blackRook2 = new Rook(ChessGame.TeamColor.BLACK);
        ChessPosition whiteRookPosition1 = new ChessPositionImpl(1, 1);
        ChessPosition whiteRookPosition2 = new ChessPositionImpl(1, 8);
        ChessPosition blackRookPosition1 = new ChessPositionImpl(8, 1);
        ChessPosition blackRookPosition2 = new ChessPositionImpl(8, 8);
        pieces.put(whiteRookPosition1, whiteRook1);
        pieces.put(whiteRookPosition2, whiteRook2);
        pieces.put(blackRookPosition1, blackRook1);
        pieces.put(blackRookPosition2, blackRook2);
    }

    private void placeRoyals() {
        King whiteKing = new King(ChessGame.TeamColor.WHITE);
        King blackKing = new King(ChessGame.TeamColor.BLACK);
        Queen whiteQueen = new Queen(ChessGame.TeamColor.WHITE);
        Queen blackQueen = new Queen(ChessGame.TeamColor.BLACK);
        ChessPosition whiteKingPosition = new ChessPositionImpl(1, 5);
        ChessPosition blackKingPosition = new ChessPositionImpl(8, 5);
        ChessPosition whiteQueenPosition = new ChessPositionImpl(1, 4);
        ChessPosition blackQueenPosition = new ChessPositionImpl(8, 4);
        pieces.put(whiteKingPosition, whiteKing);
        pieces.put(blackKingPosition, blackKing);
        pieces.put(whiteQueenPosition, whiteQueen);
        pieces.put(blackQueenPosition, blackQueen);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 8; row >= 1; row--) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPositionImpl(row, col);
                char symbol = ' ';
                if (hasPieceAt(position)) {
                    ChessPiece piece = getPiece(position);
                    symbol = ChessPieces.symbol(piece);
                }
                builder.append("|").append(symbol);
            }
            builder.append("|\n");
        }
        return builder.toString();
    }
}
