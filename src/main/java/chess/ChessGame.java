package chess;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.dao.BoardDao;
import chess.domain.piece.Piece;
import chess.view.BoardOutput;
import chess.domain.position.Square;
import chess.util.RetryUtil;
import chess.view.GameStatus;
import chess.view.InputView;
import chess.view.OutputView;
import chess.view.UserCommand;

public class ChessGame {

    private final InputView inputView;
    private final OutputView outputView;
    private final BoardDao boardDao;

    public ChessGame() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.boardDao = new BoardDao();
    }

    public void play() {
        UserCommand command = RetryUtil.retryUntilNoException(inputView::readFirstCommand);

        if (isEnd(command)) {
            return;
        }

        Board board = makeBoard();
        outputView.writeBoard(BoardOutput.of(board));

        playUntilEnd(board);
    }

    private boolean isEnd(UserCommand command) {
        return command.gameStatus().equals(GameStatus.END);
    }

    private Board makeBoard() {
        if (boardDao.existBoard()) {
            return new Board(boardDao.selectTotalBoard(), boardDao.selectTurn().decideTurn());
        }

        Board board = new BoardFactory().create();
        boardDao.addBoard(board);

        return board;
    }

    private void playUntilEnd(Board board) {
        boolean run = true;

        while (run) {
            run = RetryUtil.retryUntilNoException(() -> loopWhileEnd(board));
        }
    }

    private boolean loopWhileEnd(Board board) {
        if (board.isKingDead()) {
            boardDao.deleteBoard();
            return false;
        }

        UserCommand command = RetryUtil.retryUntilNoException(inputView::readMoveCommand);

        return commandProcess(board, command);
    }

    private boolean commandProcess(Board board, UserCommand command) {
        if (isEnd(command)) {
            boardDao.updateBoard(board);
            return false;
        }

        if (isStatus(command)) {
            outputView.writeGameScore(board.calculateScore(Piece::isWhite), board.calculateScore(Piece::isBlack));
            outputView.writeWinningColor(board.winningColorType());
        }

        if (isMove(command)) {
            movePiece(board, command);
            outputView.writeBoard(BoardOutput.of(board));
        }

        return true;
    }

    private boolean isStatus(UserCommand command) {
        return command.gameStatus().equals(GameStatus.STATUS);
    }

    private boolean isMove(UserCommand command) {
        return command.gameStatus().equals(GameStatus.MOVE);
    }

    private void movePiece(Board board, UserCommand command) {
        Square source = command.squareSource();
        Square destination = command.squareDestination();

        board.move(source, destination);
    }
}
