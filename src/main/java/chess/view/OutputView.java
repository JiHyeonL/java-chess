package chess.view;

import java.util.List;
import java.util.stream.Collectors;

public class OutputView {

    public static final String WHITE_PIECE_SCORE = "흰색 말 점수 총합: ";
    public static final String BLACK_PIECE_SCORE = "검은색 말 점수 총합: ";

    public void writeBoard(BoardOutput boardOutput) {
        List<String> piecesByRank = boardOutput.pieces().stream()
                .map(row -> row.stream()
                        .map(PieceView::toView)
                        .collect(Collectors.joining()))
                .toList();

        System.out.println(String.join("\n", piecesByRank));
    }

    public void writeGameScore(double whiteScore, double blackScore) {
        System.out.println(WHITE_PIECE_SCORE + whiteScore);
        System.out.println(BLACK_PIECE_SCORE + blackScore);
    }
}
