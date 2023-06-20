
/**
 * Error si un token inesperado es encontrado durante el análisis sintáctico
 */
class BacktrackableUnexpectedTokenException extends SemanticRoutineException {
    BacktrackableUnexpectedTokenException(Token token) {
        super(token, "Token inesperado");
    }
}
