package chess.domain.score;

import chess.domain.position.File;

public class BishopScore {

    private static final double SCORE = 3;

    public double calculate() {
        return SCORE;
    }
}
