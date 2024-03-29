package chess.domain.board;

import chess.domain.piece.ColorType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.domain.position.Square;
import chess.domain.score.Score;
import chess.domain.state.Turn;
import chess.domain.state.WhiteTurn;

import java.util.Map;
import java.util.function.Predicate;
import java.util.HashMap;

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

        Map<Square, Piece> whitePieces = piecesByColor(Piece::isWhite);

        for (File file : File.sorted()) {
            int pawnCount = 0;
            totalScore = scoreByFile(whitePieces, file, pawnCount, totalScore);
        }

        return totalScore;
    }

    public double blackTotalScore() {
        double totalScore = 0;

        Map<Square, Piece> blackPieces = piecesByColor(Piece::isBlack);

        for (File file : File.sorted()) {
            int pawnCount = 0;
            totalScore = scoreByFile(blackPieces, file, pawnCount, totalScore);
        }

        return totalScore;
    }

    private Map<Square, Piece> piecesByColor(Predicate<Piece> predicate) {
        return board.entrySet().stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);
    }

    private double scoreByFile(Map<Square, Piece> whitePieces, File file, int pawnCount, double totalScore) {
        for (Rank rank : Rank.sorted()) {
            if (whitePieces.get(Square.of(file, rank)).isPawn()) {
                pawnCount++;
            }
            totalScore += whitePieces.get(Square.of(file, rank)).score();
        }

        if (pawnCount > 1) {
            totalScore -= Score.value(PieceType.PAWN) / 2 * pawnCount;
        }
        return totalScore;
    }
}
