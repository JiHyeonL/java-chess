package chess.domain.score;

import chess.domain.position.File;

import java.util.HashMap;
import java.util.Map;

public class PawnScore {

    private static final double BASIC_SCORE = 1;
    private static final double SAME_FILE_SCORE = 0.5;

    private final Map<File, Integer> pawnFile;

    public PawnScore() {
        pawnFile = new HashMap<>();
    }

    public double calculate(File file) {
        if (pawnFile.containsKey(file)) {
            pawnFile.put(file, pawnFile.get(file) + 1);
            return SAME_FILE_SCORE;
        }

        pawnFile.put(file, 1);
        return BASIC_SCORE;
    }
}
