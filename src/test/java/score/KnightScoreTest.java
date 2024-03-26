package score;

import chess.domain.score.KingScore;
import chess.domain.score.KnightScore;
import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("나이트 점수")
public class KnightScoreTest {
    @Test
    @DisplayName("나이트 점수는 2.5점이다.")
    void knightScore() {
        // given
        KnightScore knightScore = new KnightScore();

        // when & then
        assertThat(knightScore.calculate()).isEqualTo(2.5);
    }
}
