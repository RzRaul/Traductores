
/**
 * Representación de una expresión en la gramática de PL/0
 */
class Expresion {
    private ExpresionType type;

    private char symbolValue;
    private Estructura estructura = null;
    private TokenType tokenType = null;

    private SemanticRoutine semanticRoutine = null;

    private int next;
    private int alternative;

    static final int NO_ALTERNATIVE = -1;

    /**
     * Construye una expresión
     */
    Expresion(char symbolValue, SemanticRoutine semanticRoutine, int next, int alternative) {
        this.type = ExpresionType.SYMBOL;
        this.symbolValue = symbolValue;
        this.semanticRoutine = semanticRoutine;
        this.next = next;
        this.alternative = alternative;
    }

    /**
     * Construye una expresión de tipo ESTRUCTURA
     */
    Expresion(Estructura estructura, SemanticRoutine semanticRoutine, int next, int alternative) {
        this.type = ExpresionType.ESTRUCTURA;
        this.estructura = estructura;
        this.semanticRoutine = semanticRoutine;
        this.next = next;
        this.alternative = alternative;
    }

    /**
     * Construye una expresión de tipo ID o NUMERAL o STRING
     */
    Expresion(TokenType tokenType, SemanticRoutine semanticRoutine, int next, int alternative) {
        this.type = ExpresionType.IDENTIFIER_OR_NUMERAL_OR_STRING;
        this.tokenType = tokenType;
        this.semanticRoutine = semanticRoutine;
        this.next = next;
        this.alternative = alternative;
    }

    /**
     * Construye una expresión de tipo NIL
     */
    Expresion(int next) {
        this.type = ExpresionType.NIL;
        this.alternative = NO_ALTERNATIVE;
        this.next = next;
    }

    /**
     * Construye una expresión de tipo NIL con rutina semántica
     */
    Expresion(int next, SemanticRoutine semanticRoutine) {
        this.type = ExpresionType.NIL;
        this.alternative = NO_ALTERNATIVE;
        this.next = next;
        this.semanticRoutine = semanticRoutine;
    }

    /**
     * Construye una expresión de tipo END (fin de la gramática)
     */
    private Expresion() {
        this.type = ExpresionType.END;
        this.next = -1;
        this.alternative = NO_ALTERNATIVE;
    }

    static final Expresion END_EXPRESION = new Expresion();

    ExpresionType getExpresionType() {
        return type;
    }

    char getSymbolValue() {
        return symbolValue;
    }

    Estructura getEstructura() {
        return estructura;
    }

    TokenType getTokenType() {
        return tokenType;
    }

    SemanticRoutine getSemanticRoutine() {
        return semanticRoutine;
    }

    int getNext() {
        return next;
    }

    int getAlternative() {
        return alternative;
    }
}
