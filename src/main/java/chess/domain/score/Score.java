package chess.domain.score;

import chess.domain.piece.PieceType;

import java.util.Arrays;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

public enum Score {
    QUEEN(PieceType.QUEEN, 9),
    ROOK(PieceType.ROOK, 5),
    BISHOP(PieceType.BISHOP, 3),
    KNIGHT(PieceType.KNIGHT, 2.5),
    PAWN(PieceType.PAWN, 1),
    KING(PieceType.KING, 0),
    EMPTY(PieceType.EMPTY, 0),
    ;

    private final PieceType pieceType;
    private final double score;

    Score(PieceType pieceType, double score) {
        this.pieceType = pieceType;
        this.score = score;
    }

    public static double value(PieceType pieceType) {
        Optional<Score> values =  Arrays.stream(Score.values())
                .filter(score -> score.pieceType.equals(pieceType))
                .findFirst();

        if (values.isPresent()) {
            return values.get().score;
        }

        throw new NullPointerException("pieceType에 맞는 score가 없습니다.");
    }
}
