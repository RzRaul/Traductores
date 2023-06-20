// Purpose: FatalSemanticRoutineException class file.
public abstract class FatalSemanticRoutineException extends SemanticRoutineException{
    public FatalSemanticRoutineException(Token token, String message) {
        super(token, message);
    }
}
