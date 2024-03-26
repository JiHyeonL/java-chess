package score;

import chess.domain.score.QueenScore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("퀸 점수")
public class QueenScoreTest {
    @Test
    @DisplayName("퀸 점수는 9점이다.")
    void queenScore() {
        // given
        QueenScore queenScore = new QueenScore();

        // when & then
        assertThat(queenScore.calculate()).isEqualTo(9);
    }
}
