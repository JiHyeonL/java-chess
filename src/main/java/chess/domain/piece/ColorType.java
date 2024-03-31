package chess.domain.piece;

import java.util.Arrays;
import java.util.List;

public enum ColorType {

    BLACK,
    WHITE,
    EMPTY,
    ;

    public static List<ColorType> valuesNotEmpty() {
        return Arrays.stream(ColorType.values())
                .filter(colorType -> !colorType.equals(ColorType.EMPTY))
                .toList();
    }
}
