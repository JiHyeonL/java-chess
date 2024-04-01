package chess.domain.board;

import chess.domain.piece.ColorType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.domain.position.Square;
import chess.domain.score.Score;
import chess.domain.score.WinStatus;
import chess.domain.state.Turn;
import chess.domain.state.TurnState;
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

    public Board(Map<Square, Piece> board, Turn turn) {
        this.board = board;
        this.turn = turn;
    }

    public void move(Square source, Square destination) {
        turn = turn.checkMovable(board, source, destination);

        changePosition(source, destination);
    }

    private void changePosition(Square source, Square destination) {
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

    public double calculateScore(Predicate<Piece> myColor) {
        double totalScore = 0;

        for (File file : File.sorted()) {
            totalScore = scoreByFile(myColor, file, totalScore);
        }

        return totalScore;
    }

    private double scoreByFile(Predicate<Piece> isMyColor, File file, double totalScore) {
        int pawnCount = 0;

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
        double scoreDiff = calculateScore(Piece::isBlack) - calculateScore(Piece::isWhite);

        if (scoreDiff > 0) {
            return WinStatus.BLACK_WIN;
        }

        if (scoreDiff < 0) {
            return WinStatus.WHITE_WIN;
        }

        return WinStatus.DRAW;
    }

    public TurnState turnState() {
        return turn.turnState();
    }
}
