package chess.domain.strategy;

import chess.domain.piece.ColorType;
import chess.domain.position.Square;

public interface MoveStrategy {
    boolean check(Square source, Square destination, ColorType colorType);
}
