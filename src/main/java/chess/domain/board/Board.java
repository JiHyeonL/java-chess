package chess.domain.board;

import chess.domain.piece.ColorType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.Square;
import chess.domain.state.Turn;
import chess.domain.state.WhiteTurn;

import java.util.Map;

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
}
