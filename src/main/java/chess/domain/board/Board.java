package chess.domain.board;

import chess.domain.dao.BoardDao;
import chess.domain.piece.ColorType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.domain.position.Square;
import chess.domain.score.Score;
import chess.domain.score.WinStatus;
import chess.domain.state.Turn;
import chess.domain.state.WhiteTurn;

import java.util.Map;
import java.util.function.Predicate;

public class Board {

    private final Map<Square, Piece> board;
    private Turn turn;

    public Board(Map<Square, Piece> board) {
        this.board = board;
        this.turn = new WhiteTurn();
    }

    public void move(Square source, Square destination) {
        turn = turn.checkMovable(board, source, destination);

        moveOrCatch(source, destination);
    }

    private void moveOrCatch(Square source, Square destination) {
        Piece sourcePiece = board.get(source);

        board.replace(source, new Piece(PieceType.EMPTY, ColorType.EMPTY));
        board.replace(destination, sourcePiece);
    }

    public Piece findPieceBySquare(Square square) {
        return board.get(square);
    }

    public boolean isKingDead() {
        long kingCount =  board.values().stream()
                .filter(Piece::isKing)
                .count();

        return kingCount != 2;
    }

    public double whiteTotalScore() {
        double totalScore = 0;

        for (File file : File.sorted()) {
            int pawnCount = 0;
            totalScore = scoreByFile(Piece::isWhite, file, pawnCount, totalScore);
        }

        return totalScore;
    }

    public double blackTotalScore() {
        double totalScore = 0;

        for (File file : File.sorted()) {
            int pawnCount = 0;
            totalScore = scoreByFile(Piece::isBlack, file, pawnCount, totalScore);
        }

        return totalScore;
    }

    private double scoreByFile(Predicate<Piece> isMyColor, File file, int pawnCount, double totalScore) {
        for (Rank rank : Rank.sorted()) {
            if (!isMyColor.test(board.get(Square.of(file, rank)))) {
                continue;
            }

            if (board.get(Square.of(file, rank)).isPawn()) {
                pawnCount++;
            }
            totalScore += board.get(Square.of(file, rank)).score();
        }

        if (pawnCount > 1) {
            totalScore -= Score.value(PieceType.PAWN) / 2 * pawnCount;
        }
        return totalScore;
    }

    public WinStatus winningColorType() {
        if (blackTotalScore() > whiteTotalScore()) {
            return WinStatus.BLACK_WIN;
        }
        if (blackTotalScore() < whiteTotalScore()) {
            return WinStatus.WHITE_WIN;
        }
        return WinStatus.DRAW;
    }
}
