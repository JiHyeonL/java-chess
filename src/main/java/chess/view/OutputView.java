package chess.view;

import chess.domain.piece.ColorType;
import chess.domain.score.WinStatus;

import java.util.List;
import java.util.stream.Collectors;

public class OutputView {

    private static final String WHITE_PIECE_SCORE = "흰색 말 점수 총합: ";
    private static final String BLACK_PIECE_SCORE = "검은색 말 점수 총합: ";
    private static final String WINNING_COLOR = "점수 승리 결과: ";
    private static final String BLACK_SIDE = "흑색 진영";
    private static final String WHITE_SIDE = "백색 진영";
    private static final String DRAW = "동점";

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

    public void writeWinningColor(WinStatus winStatus) {
        System.out.println(WINNING_COLOR + messageByWinStatus(winStatus));
    }

    private String messageByWinStatus(WinStatus winStatus) {
        if (winStatus == WinStatus.BLACK_WIN) {
            return BLACK_SIDE;
        }
        if (winStatus == WinStatus.WHITE_WIN) {
            return WHITE_SIDE;
        }
        return DRAW;
    }
}
