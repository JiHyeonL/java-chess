package chess.domain.piece;

import chess.domain.position.Square;
import chess.domain.score.Score;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Piece {

    private static final Map<String, Piece> POOL;

    static {
        POOL = PieceType.valuesNotEmpty().stream()
                .flatMap(type -> ColorType.valuesNotEmpty().stream()
                        .map(color -> new Piece(type, color)))
                .collect(Collectors.toMap(it -> toKey(it.pieceType, it.colorType), Function.identity()));

        POOL.put(toKey(PieceType.EMPTY, ColorType.EMPTY), new Piece(PieceType.EMPTY, ColorType.EMPTY));
    }

    private final PieceType pieceType;
    private final ColorType colorType;

    public Piece(PieceType pieceType, ColorType colorType) {
        this.pieceType = pieceType;
        this.colorType = colorType;
    }

    public static Piece of(PieceType pieceType, ColorType colorType) {
        return POOL.get(toKey(pieceType, colorType));
    }

    public static Piece from(String name) {
        return POOL.get(name);
    }

    private static String toKey(PieceType pieceType, ColorType colorType) {
        return colorType.name() + pieceType.name();
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

    public boolean isPawn() {
        return pieceType.equals(PieceType.PAWN);
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

    public double score() {
        return Score.value(pieceType);
    }

    // TODO: 게터 없앨 방법 찾아보기
    public PieceType pieceType() {
        return pieceType;
    }

    public ColorType colorType() {
        return colorType;
    }
}
