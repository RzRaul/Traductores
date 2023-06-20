
class UnexpectedTokenException extends FatalSemanticRoutineException {
    UnexpectedTokenException(Token token) {
        super(token, "token inesperado");
    }
}
