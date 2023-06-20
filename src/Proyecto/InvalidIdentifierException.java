/**
 *Error para cuando se intenta declarar una variable con un identificador ya existente
 */
public class InvalidIdentifierException extends FatalSemanticRoutineException {
    public InvalidIdentifierException(Token token, String message) {
        super(token, message);
    }
}
