package chess;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.view.BoardOutput;
import chess.domain.position.Square;
import chess.util.RetryUtil;
import chess.view.GameStatus;
import chess.view.InputView;
import chess.view.OutputView;
import chess.view.UserCommand;

import java.util.function.Supplier;

public class ChessGame {

    private final InputView inputView;
    private final OutputView outputView;

    public ChessGame() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
    }

    public void play() {
        UserCommand command = RetryUtil.retryUntilNoException(inputView::readStartCommand);

        if (!isStart(command)) {
            return;
        }

        Board board = new Board(new BoardFactory().create());
        outputView.writeBoard(BoardOutput.of(board));

        playUntilEnd(board);
    }

    private boolean isStart(UserCommand command) {
        return command.gameStatus().equals(GameStatus.START);
    }

    private void playUntilEnd(Board board) {
        boolean run = true;

        while (run) {
            run = RetryUtil.retryUntilNoException(() -> loopWhileEnd(board));
        }
    }

    private boolean loopWhileEnd(Board board) {
        if (board.isKingDead()) {
            return false;
        }
        UserCommand command = RetryUtil.retryUntilNoException(inputView::readMoveCommand);

        if (isEnd(command.gameStatus())) {
            return false;
        }
        if (command.gameStatus().equals(GameStatus.STATUS)) {
            outputView.writeGameScore(board.whiteTotalScore(), board.blackTotalScore());
            outputView.writeWinningColor(board.winningColorType());
        }
        if (command.gameStatus().equals(GameStatus.MOVE)) {
            movePiece(board, command);
            outputView.writeBoard(BoardOutput.of(board));
        }

        return true;
    }

    private boolean isEnd(GameStatus gameStatus) {
        return gameStatus.equals(GameStatus.END);
    }

    private void movePiece(Board board, UserCommand command) {
        Square source = command.squareSource();
        Square destination = command.squareDestination();

        board.move(source, destination);
    }
}
