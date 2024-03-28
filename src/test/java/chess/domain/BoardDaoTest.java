package chess.domain;

import org.junit.jupiter.api.Test;

public class BoardDaoTest {
    private final BoardDao boardDao = new BoardDao();

    @Test
    public void connection() {
        try (final var connection = boardDao.getConnection()) {
            assertThat(connection).isNotNull();
        }
    }
}
