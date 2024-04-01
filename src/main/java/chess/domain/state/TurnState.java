package chess.domain.state;

import chess.domain.board.Board;

public enum TurnState {
    BLACK,
    WHITE,
    ;

    public Turn decideTurn() {
        if (this.equals(BLACK)) {
            return new BlackTurn();
        }
        return new WhiteTurn();
    }

    public static TurnState findByName(String name) {
        if (BLACK.name().equals(name)) {
            return BLACK;
        }
        return WHITE;
    }
}