import java.io.IOException;

/**
 * El Parser es el componente principal del compilador. Toma los tokens del Lexer y genera código de máquina.
 */
public class Parser {
    private Lexer lexer;
    private SymTable symTable = new SymTable();
    private CodeGenerator codeGenerator;

    public Parser(Lexer lexer, CodeGenerator codeGenerator) {
        this.lexer = lexer;
        this.codeGenerator = codeGenerator;
    }

    /**
     * Hace el análisis sintáctico del programa
     */
    public void parse() throws FatalSemanticRoutineException, IOException {
        try {
            parse(Estructura.PROGRAM);
        } catch (BacktrackableUnexpectedTokenException e) {
            throw new UnexpectedTokenException(lexer.getNextToken());
        }
    }

    private void parse(Estructura estructura) throws FatalSemanticRoutineException, IOException, BacktrackableUnexpectedTokenException {
        boolean success = false;
        boolean backTrackPossible = true;

        Expresion currentExpresion = estructura.getExpresions()[0];

        Token nextToken;

        while (! (nextToken = lexer.getNextToken()).equals(Token.EOF_TOKEN)){
            switch (currentExpresion.getExpresionType()){
                case NIL:
                    success = true;
                    break;
                case SYMBOL:
                    success = (nextToken.getCharValue() == currentExpresion.getSymbolValue());
                    break;
                case IDENTIFIER_OR_NUMERAL_OR_STRING:
                    success = (nextToken.getType().equals(currentExpresion.getTokenType()));
                    break;
                case ESTRUCTURA:
                    try {
                        parse(currentExpresion.getEstructura());
                        success = true;
                    }
                    catch (BacktrackableUnexpectedTokenException e) {
                        success = false;
                    }
                    break;
                case END:
                    return;
            }

            if (success && currentExpresion.getSemanticRoutine() != null){
                currentExpresion.getSemanticRoutine().apply(this);
            }

            if (!success){
                if (currentExpresion.getAlternative() != Expresion.NO_ALTERNATIVE)
                    currentExpresion = estructura.getExpresions()[currentExpresion.getAlternative()];
                else if (backTrackPossible)
                    throw new BacktrackableUnexpectedTokenException(lexer.getNextToken());
                else
                    throw new UnexpectedTokenException(lexer.getNextToken());
            }

            else {
                if (currentExpresion.getExpresionType() == ExpresionType.SYMBOL
                        || currentExpresion.getExpresionType() == ExpresionType.IDENTIFIER_OR_NUMERAL_OR_STRING){
                    lexer.lex();
                    backTrackPossible = false;
                }

                currentExpresion = estructura.getExpresions()[currentExpresion.getNext()];
            }
        }

        //EOF encountered
        if(!currentExpresion.getExpresionType().equals(ExpresionType.END))
            throw new UnexpectedTokenException(nextToken);
    }

    Lexer getLexer() {
        return lexer;
    }

    SymTable getSymTable() {
        return symTable;
    }

    CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }
}
