package chess.domain.strategy;

import chess.domain.piece.ColorType;
import chess.domain.position.Square;

public class EmptyMoveStrategy implements MoveStrategy{
    @Override
    public boolean check(Square source, Square destination, ColorType colorType) {
        return false;
    }
}
