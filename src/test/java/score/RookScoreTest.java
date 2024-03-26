package score;

import chess.domain.score.BishopScore;
import chess.domain.score.RookScore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("룩 점수")
public class RookScoreTest {
    @Test
    @DisplayName("룩의 점수는 5점이다.")
    void bishopScore() {
        // given
        RookScore rookScore = new RookScore();

        // when & then
        assertThat(rookScore.calculate()).isEqualTo(5);
    }
}
