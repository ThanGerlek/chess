package chess;

import chess.pieces.*;

import java.util.HashMap;
import java.util.Map;

public class ChessBoardImpl implements ChessBoard {

    private ChessPosition whiteKingPosition;
    private ChessPosition blackKingPosition;
    private Map<ChessPosition, ChessPiece> pieces;

    public ChessBoardImpl() {
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard.
     *
     * @param position at which to add the piece.
     * @param piece    the piece to add.
     */
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        pieces.put(position, piece);
    }

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

    public ChessPosition getKingPosition(ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE) ? whiteKingPosition : blackKingPosition;
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
        pieces = new HashMap<>();
        placePawns();
        placeKnights();
        placeBishops();
        placeRooks();
        placeRoyals();
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
        Knight blackKnight1 = new Knight(ChessGame.TeamColor.WHITE);
        Knight blackKnight2 = new Knight(ChessGame.TeamColor.WHITE);
        ChessPosition whiteKnightPosition1 = new ChessPositionImpl(1, 1);
        ChessPosition whiteKnightPosition2 = new ChessPositionImpl(1, 8);
        ChessPosition blackKnightPosition1 = new ChessPositionImpl(8, 1);
        ChessPosition blackKnightPosition2 = new ChessPositionImpl(8, 8);
        pieces.put(whiteKnightPosition1, whiteKnight1);
        pieces.put(whiteKnightPosition2, whiteKnight2);
        pieces.put(blackKnightPosition1, blackKnight1);
        pieces.put(blackKnightPosition2, blackKnight2);
    }

    private void placeBishops() {
        Bishop whiteBishop1 = new Bishop(ChessGame.TeamColor.WHITE);
        Bishop whiteBishop2 = new Bishop(ChessGame.TeamColor.WHITE);
        Bishop blackBishop1 = new Bishop(ChessGame.TeamColor.WHITE);
        Bishop blackBishop2 = new Bishop(ChessGame.TeamColor.WHITE);
        ChessPosition whiteBishopPosition1 = new ChessPositionImpl(1, 1);
        ChessPosition whiteBishopPosition2 = new ChessPositionImpl(1, 1);
        ChessPosition blackBishopPosition1 = new ChessPositionImpl(8, 1);
        ChessPosition blackBishopPosition2 = new ChessPositionImpl(8, 1);
        pieces.put(whiteBishopPosition1, whiteBishop1);
        pieces.put(whiteBishopPosition2, whiteBishop2);
        pieces.put(blackBishopPosition1, blackBishop1);
        pieces.put(blackBishopPosition2, blackBishop2);
    }

    private void placeRooks() {
        Rook whiteRook1 = new Rook(ChessGame.TeamColor.WHITE);
        Rook whiteRook2 = new Rook(ChessGame.TeamColor.WHITE);
        Rook blackRook1 = new Rook(ChessGame.TeamColor.WHITE);
        Rook blackRook2 = new Rook(ChessGame.TeamColor.WHITE);
        ChessPosition whiteRookPosition1 = new ChessPositionImpl(1, 1);
        ChessPosition whiteRookPosition2 = new ChessPositionImpl(1, 1);
        ChessPosition blackRookPosition1 = new ChessPositionImpl(8, 1);
        ChessPosition blackRookPosition2 = new ChessPositionImpl(8, 1);
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

        this.whiteKingPosition = whiteKingPosition;
        this.blackKingPosition = blackKingPosition;
    }
}
