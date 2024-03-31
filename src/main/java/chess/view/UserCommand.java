package chess.view;

import chess.domain.position.Square;

public record UserCommand(GameStatus gameStatus, String source, String destination) {

    public UserCommand(GameStatus gameStatus) {
        this(gameStatus, "", "");
    }

    public Square squareSource() {
        return Square.from(source);
    }

    public Square squareDestination() {
        return Square.from(destination);
    }
}
