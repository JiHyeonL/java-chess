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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        Piece destinationPiece = board.get(destination);

        // TODO: 조건문 삭제(source를 empty로 하면 같음)
        if (destinationPiece.isNotEmpty()) {
            board.replace(source, new Piece(PieceType.EMPTY, ColorType.EMPTY));
            board.replace(destination, sourcePiece);
            return;
        }

        board.replace(source, destinationPiece);
        board.replace(destination, sourcePiece);
    }

    public Piece findPieceBySquare(Square square) {
        return board.get(square);
    }

    public boolean isKingAlive() {
        long kingCount =  board.values().stream()
                .filter(Piece::isKing)
                .count();

        return kingCount == 2;
    }

    public double whiteTotalScore() {
        double totalScore = 0;

        for (File file : File.sorted()) {
            int pawnCount = 0;
            for (Rank rank : Rank.sorted()) {
                if (!board.get(Square.of(file, rank)).isWhite()) {
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
        }

        return totalScore;
    }

    public double blackTotalScore() {
        double totalScore = 0;

        for (File file : File.sorted()) {
            int pawnCount = 0;
            for (Rank rank : Rank.sorted()) {
                if (!board.get(Square.of(file, rank)).isBlack()) {
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
        }

        return totalScore;
    }
}
