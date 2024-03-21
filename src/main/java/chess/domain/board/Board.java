package chess.domain.board;

import chess.domain.Direction;
import chess.domain.piece.ColorType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.Square;
import chess.dto.BoardOutput;
import chess.dto.SquareDifferent;

import java.util.Map;

public class Board {

    private static final int VERTICAL_UP_INDEX = 1;
    private static final int VERTICAL_DOWN_INDEX = -1;
    private static final int HORIZONTAL_RIGHT_INDEX = 1;
    private static final int HORIZONTAL_LEFT_INDEX = -1;
    private static final String NOT_YOUR_TURN_ERROR = "움직이려고 하는 말이 본인 진영의 말이 아닙니다.";
    private static final String SAME_COLOR_ERROR = "목적지에 같은 편 말이 있어 이동할 수 없습니다.";
    private static final String CANNOT_MOVE_ERROR = "해당 말의 규칙으로는 도착지로 갈 수 없습니다.";
    private static final String PATH_BLOCKED_ERROR = "막힌 경로입니다.";
    public static final String PAWN_CANNOT_CATCH_STRAIGHT_ERROR = "폰은 직선 경로로 상대 말을 잡을 수 없습니다.";

    private final Map<Square, Piece> board;
    private final Turn turn;

    public Board() {
        this.board = new BoardFactory().create();
        this.turn = new Turn();
    }

    public BoardOutput toBoardOutput() {
        return new BoardOutput(board);
    }

    public void move(Square source, Square destination) {
        Piece sourcePiece = board.get(source);
        Piece destinationPiece = board.get(destination);

        checkTurn(sourcePiece);
        checkSameColor(sourcePiece, destinationPiece);

        if (sourcePiece.isSameType(PieceType.PAWN.name())) {
            SquareDifferent squareDifferent = source.calculateDiff(destination);
            Direction direction = Direction.findDirectionByDiff(squareDifferent);

            if (!direction.isDiagonal() && !destinationPiece.isEmpty()) {
                throw new IllegalArgumentException(PAWN_CANNOT_CATCH_STRAIGHT_ERROR);
            }
        }

        checkCannotMove(source, destination, sourcePiece);
        checkPathBlocked(source, destination, sourcePiece);
        moveOrCatch(sourcePiece, destinationPiece, source, destination);

        turn.update();
    }

    private void checkTurn(Piece sourcePiece) {
        if (turn.isBlackTurn() && sourcePiece.isWhite()) {
            throw new IllegalArgumentException(NOT_YOUR_TURN_ERROR);
        }

        if (turn.isWhiteTurn() && sourcePiece.isBlack()) {
            throw new IllegalArgumentException(NOT_YOUR_TURN_ERROR);
        }
    }

    private void checkSameColor(Piece sourcePiece, Piece destinationPiece) {
        if (sourcePiece.isSameColor(destinationPiece)) {
            throw new IllegalArgumentException(SAME_COLOR_ERROR);
        }
    }

    private void checkCannotMove(Square source, Square destination, Piece sourcePiece) {
        if (!sourcePiece.canMove(source, destination)) {
            throw new IllegalArgumentException(CANNOT_MOVE_ERROR);
        }
    }

    private void checkPathBlocked(Square source, Square destination, Piece sourcePiece) {
        SquareDifferent diff = source.calculateDiff(destination);

        if (!sourcePiece.isSameType(PieceType.KNIGHT.name())
                && !(sourcePiece.isSameType(PieceType.PAWN.name()) && source.isPawnFirstMove())
                && !sourcePiece.isSameType(PieceType.KING.name())) {
            findPath(source, destination, diff);
        }
    }

    private void findPath(Square source, Square destination, SquareDifferent diff) {
        Square candidate = source;

        while (!candidate.equals(destination)) {
            if (!source.equals(candidate) && !board.get(candidate).isEmpty()) {
                throw new IllegalArgumentException(PATH_BLOCKED_ERROR);
            }

            if (Direction.findDirectionByDiff(diff).equals(Direction.UP)) {
                candidate = candidate.moveVertical(VERTICAL_UP_INDEX);
                continue;
            }

            if (Direction.findDirectionByDiff(diff).equals(Direction.DOWN)) {
                candidate = candidate.moveVertical(VERTICAL_DOWN_INDEX);
                continue;
            }

            if (Direction.findDirectionByDiff(diff).equals(Direction.RIGHT)) {
                candidate = candidate.moveHorizontal(HORIZONTAL_RIGHT_INDEX);
                continue;
            }

            if (Direction.findDirectionByDiff(diff).equals(Direction.LEFT)) {
                candidate = candidate.moveHorizontal(HORIZONTAL_LEFT_INDEX);
                continue;
            }

            if (Direction.findDirectionByDiff(diff).equals(Direction.RIGHT_DOWN)) {
                candidate = candidate.moveDiagonal(HORIZONTAL_RIGHT_INDEX, VERTICAL_DOWN_INDEX);
                continue;
            }

            if (Direction.findDirectionByDiff(diff).equals(Direction.RIGHT_UP)) {
                candidate = candidate.moveDiagonal(HORIZONTAL_RIGHT_INDEX, VERTICAL_UP_INDEX);
                continue;
            }

            if (Direction.findDirectionByDiff(diff).equals(Direction.LEFT_DOWN)) {
                candidate = candidate.moveDiagonal(HORIZONTAL_LEFT_INDEX, VERTICAL_DOWN_INDEX);
                continue;
            }

            if (Direction.findDirectionByDiff(diff).equals(Direction.LEFT_UP)) {
                candidate = candidate.moveDiagonal(HORIZONTAL_LEFT_INDEX, VERTICAL_UP_INDEX);
            }
        }
    }

    private void moveOrCatch(Piece sourcePiece, Piece destinationPiece, Square source, Square destination) {
        if (!destinationPiece.isEmpty()) {
            board.replace(destination, sourcePiece);
            board.replace(source, new Piece(PieceType.EMPTY, ColorType.EMPTY));
            return;
        }

        board.replace(source, destinationPiece);
        board.replace(destination, sourcePiece);
    }
}
