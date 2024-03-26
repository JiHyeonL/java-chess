package chess.view;

import chess.domain.position.Direction;
import chess.domain.position.Square;

public record UserCommand(GameStatus gameStatus, String source, String destination) {

    public UserCommand(GameStatus gameStatus) {
        this(gameStatus, "", "");
    }

    public Square squareSource() {
        return Square.findByName(source);
    }

    public Square squareDestination() {
        return Square.findByName(destination);
    }
}
