package chess.domain.strategy;

import chess.domain.piece.ColorType;
import chess.domain.position.Square;
import chess.dto.SquareDifferent;

public class PawnMoveStrategy implements MoveStrategy {

    private static final int PAWN_FORWARD_INDEX = 1;
    private static final int PAWN_FIRST_FORWARD_INDEX = 2;

    @Override
    public boolean check(Square source, Square destination, ColorType colorType) {
        SquareDifferent diff = source.calculateDiff(destination);

        int forwardIndex = PAWN_FORWARD_INDEX;
        int firstForwardIndex = PAWN_FIRST_FORWARD_INDEX;

        if (colorType.equals(ColorType.BLACK)) {
            forwardIndex *= -1;
            firstForwardIndex *= -1;
        }

        if (source.isPawnStartSquare()) {
            return checkDiagonal(forwardIndex, diff.rankDiff(), diff.fileDiff())
                    || checkCanForward(firstForwardIndex, diff.rankDiff(), diff.fileDiff())
                    || checkCanForward(forwardIndex, diff.rankDiff(), diff.fileDiff());
        }

        return checkCanForward(forwardIndex, diff.rankDiff(), diff.fileDiff())
                || checkDiagonal(forwardIndex, diff.rankDiff(), diff.fileDiff());
    }

    private boolean checkCanForward(int forwardIndex, int rankDiff, int fileDiff) {
        return rankDiff == forwardIndex && fileDiff == 0;
    }

    private boolean checkDiagonal(int forwardIndex, int rankDiff, int fileDiff) {
        return rankDiff == forwardIndex && Math.abs(fileDiff) == 1;
    }
}
