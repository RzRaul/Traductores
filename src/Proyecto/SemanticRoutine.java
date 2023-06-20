import java.io.IOException;

/**
 *Interfaz que representa una rutina semántica
 */
public interface SemanticRoutine {
    void apply(Parser parser) throws FatalSemanticRoutineException, IOException;
}
