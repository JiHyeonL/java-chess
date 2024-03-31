package chess.domain;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.dao.BoardDao;
import chess.domain.piece.ColorType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.domain.position.Square;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("보드 dao")
public class BoardDaoTest {
    private final BoardDao boardDao = new BoardDao();

    @Test
    public void connection() throws SQLException {
        try (final var connection = boardDao.getConnection()) {
            assertThat(connection).isNotNull();
        }
    }

    @DisplayName("보드의 첫 결과를 저장한다.")
    @Test
    public void addBoard() {
        final Board chessboard = new Board(new BoardFactory().create());
        boardDao.addChessBoard(0, chessboard);
    }

    @Test
    @DisplayName("보드의 특정 위치에 있는 말을 반환한다.")
    public void findPieceBySquare() {
        // given
        final File file = File.A;
        final Rank rank = Rank.TWO;

        // when
        Piece piece = boardDao.findPieceBySquare(file, rank);

        // then
        assertThat(piece).isEqualTo(Piece.of(PieceType.PAWN, ColorType.WHITE));
    }

    @Test
    @DisplayName("보드의 특정 위치에 있는 말을 바꾼다.")
    void changePiece() {
        // given
        final File file = File.A;
        final Rank rank = Rank.TWO;
        final PieceType pieceType = PieceType.KING;
        final ColorType colorType = ColorType.BLACK;

        // when
        boardDao.updateSquarePiece(file, rank, pieceType, colorType);

        // then
        assertThat(boardDao.findPieceBySquare(file, rank))
                .isEqualTo(Piece.of(pieceType, colorType));
    }

    @Test
    @DisplayName("보드에 데이터가 있으면 true를 반환한다")
    void existBoard() {
        // given & when & then
        assertThat(boardDao.existBoard()).isTrue();
    }

    @Test
    @DisplayName("보드의 위치별 체스말을 전부 가져온다.")
    void allBoard() {
        // given
        deleteBoardInfo();
        addBoard();

        // when
        Map<Square, Piece> actual = boardDao.getAllPiecesInfo();

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(new BoardFactory().create());
    }

    @Test
    @DisplayName("보드의 모든 데이터를 삭제한다.")
    void deleteBoardInfo() {
        // given & when
        boardDao.deleteBoard();

        // then
        assertThat(boardDao.existBoard()).isFalse();
    }
}
