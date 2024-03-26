package score;

import chess.domain.score.EmptyScore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("빈칸 점수")
public class EmptyScoreTest {
    @Test
    @DisplayName("빈칸은 점수가 0이다.")
    void emptyScore() {
        // given
        EmptyScore emptyScore = new EmptyScore();

        // when & then
        assertThat(emptyScore.calculate()).isEqualTo(0);
    }
}
