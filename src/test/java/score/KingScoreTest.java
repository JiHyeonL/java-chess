package score;

import chess.domain.score.KingScore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("킹 점수")
public class KingScoreTest {

    @Test
    @DisplayName("킹의 점수는 0이다.")
    void kingScore() {
        // given
        KingScore kingScore = new KingScore();

        // when & then
        assertThat(kingScore.calculate()).isEqualTo(0);
    }
}
