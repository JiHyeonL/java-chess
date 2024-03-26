package chess.domain.piece;

import chess.domain.position.Square;

public class Piece {

    private final PieceType pieceType;
    private final ColorType colorType;

    public Piece(PieceType pieceType, ColorType colorType) {
        this.pieceType = pieceType;
        this.colorType = colorType;
    }

    public boolean isBlack() {
        return colorType.equals(ColorType.BLACK);
    }

    public boolean isWhite() {
        return colorType.equals(ColorType.WHITE);
    }

    public boolean isKing() {
        return pieceType.equals(PieceType.KING);
    }

    public boolean matches(String pieceViewName) {
        return pieceType.name().equals(pieceViewName);
    }

    public boolean matches(PieceType pieceType) {
        return this.pieceType.equals(pieceType);
    }

    public boolean isSameColor(Piece whitePiece) {
        return colorType.equals(whitePiece.colorType);
    }

    public boolean isNotEmpty() {
        return !pieceType.equals(PieceType.EMPTY);
    }

    public boolean canMove(Square source, Square destination) {
        return pieceType.canMove(source, destination, colorType);
    }
}
