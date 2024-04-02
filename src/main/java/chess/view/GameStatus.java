package chess.view;

import java.util.Arrays;

public enum GameStatus {

    START("start"),
    MOVE("move"),
    END("end"),
    STATUS("status"),
    ;

    private final String value;

    GameStatus(String value) {
        this.value = value;
    }

    private static final String NOT_FOUND = "존재하지 않는 게임 상태 명령어 입니다.";

    public static GameStatus findByValue(String value) {
        return Arrays.stream(GameStatus.values())
                .filter(status -> status.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND));
    }

    public boolean isGamingCommand() {
        return this.equals(MOVE) || this.equals(END) || this.equals(STATUS);
    }

    public boolean isStartOrEnd() {
        return this.equals(END) || this.equals(START);
    }

    public static boolean isEnd(UserCommand command) {
        return command.gameStatus().equals(GameStatus.END);
    }

    public static boolean isStatus(UserCommand command) {
        return command.gameStatus().equals(GameStatus.STATUS);
    }

    public static boolean isMove(UserCommand command) {
        return command.gameStatus().equals(GameStatus.MOVE);
    }

    public String value() {
        return value;
    }
}
