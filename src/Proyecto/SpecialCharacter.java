import java.util.HashMap;
import java.util.Map;
public enum SpecialCharacter {
    ASSIGN(128),
    LESS_OR_EQUAL(129),
    GREATER_OR_EQUAL(130),
    BEGIN(131),
    CALL(132),
    CONST(133),
    DO(134),
    END(135),
    IF(136),
    ODD(137),
    PROCEDURE(138),
    THEN(139),
    VAR(140),
    WHILE(141),
    ELSE(142),
    NOT(143),
    AND(144),
    OR(145);

    public char value;

    SpecialCharacter(int value) {
        this.value = (char) value;
    }

    static Map<String, Character> stringCharacterMap;
    static Map<Character, String> characterStringMap;

    static {
        stringCharacterMap = new HashMap<>();
        characterStringMap = new HashMap<>();

        for (SpecialCharacter item : SpecialCharacter.values()) {
            stringCharacterMap.put(item.toString(), item.value);
            characterStringMap.put(item.value, item.toString());
        }
    }
}