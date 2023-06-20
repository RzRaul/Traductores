
import java.util.Objects;

/**
 * Excepción que se arroja cuando se encuentra un error en una rutina semántica
 */
public abstract class SemanticRoutineException extends Exception {
    protected Token token;
    private String message;

    SemanticRoutineException(Token token, String message) {
        this.token = token;
        this.message = message;
    }

    /**
     * Mensaje de error
     */
    @Override
    public final String toString() {
        return message + " at or near Token " + token;
    }

    /**
     * Revisa si dos objetos son iguales
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SemanticRoutineException)) return false;
        SemanticRoutineException that = (SemanticRoutineException) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(message, that.message);
    }
}
