package score;

import chess.domain.position.File;
import chess.domain.score.PawnScore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("폰 점수")
public class PawnScoreTest {
    @Test
    @DisplayName("폰의 기본점수는 1점이다.")
    void pawnBasicScore() {
        // given
        PawnScore pawnScore = new PawnScore();
        File file = File.C;

        // when & then
        assertThat(pawnScore.calculate(file)).isEqualTo(1);
    }

    @Test
    @DisplayName("같은 File에 다른 폰이 존재할 경우 0.5점을 반환한다.")
    void pawnSameFile() {
        // given
        PawnScore pawnScore = new PawnScore();
        File file = File.A;

        // when
        pawnScore.calculate(file);

        // then
        assertThat(pawnScore.calculate(file)).isEqualTo(0.5);
    }
}
