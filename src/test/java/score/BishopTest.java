package score;

import chess.domain.score.BishopScore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("비숍 점수")
public class BishopTest {
    @Test
    @DisplayName("비숍의 점수는 3점이다.")
    void bishopScore() {
        // given
        BishopScore bishopScore = new BishopScore();

        // when & then
        assertThat(bishopScore.calculate()).isEqualTo(3);
    }
}
