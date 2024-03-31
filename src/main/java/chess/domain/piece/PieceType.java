package chess.domain.piece;

import chess.domain.position.Square;
import chess.domain.strategy.*;

import java.util.Arrays;
import java.util.List;

public enum PieceType {

    KING(new KingMoveStrategy()),
    QUEEN(new QueenMoveStrategy()),
    ROOK(new RookMoveStrategy()),
    BISHOP(new BishopMoveStrategy()),
    KNIGHT(new KnightMoveStrategy()),
    PAWN(new PawnMoveStrategy()),
    EMPTY(new EmptyMoveStrategy()),
    ;

    private final MoveStrategy moveStrategy;

    PieceType(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    public boolean canMove(Square source, Square destination, ColorType colorType) {
        return moveStrategy.check(source, destination, colorType);
    }

    public static List<PieceType> valuesNotEmpty() {
        return Arrays.stream(PieceType.values())
                .filter(pieceType -> !pieceType.equals(PieceType.EMPTY))
                .toList();
    }
}
