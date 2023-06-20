
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Lexer para el compilador PL/0, convierte el archivo de entrada en una lista de tokens
 */
public class Lexer {
    /**
     * Lista de tokens generada por el lexer
     * Estados del automata finito
     */
    private enum Estado {
        Z_0, Z_1, Z_2, Z_3, Z_4, Z_5, Z_6, Z_7, Z_8, Z_9, Z10, Z11, Z12, Z13, Z14, ESy, EIK, EId, ENu, EAs, ECo, ELE, ELT, EGE, EGT, ESt
    }

    private enum CharType {
        SYMBOL, DIGIT, ALPHA, COLON, EQUALS, LESS, GREATER, OTHER, ALPHA_KEY_START, SLASH, STAR, QUOTE, BACKSLASH
    }

    private enum Accion {
        WRE, W_R, END, REA, CLR
    }

    private static final HashMap<Character, CharType> CharTypeMap = new HashMap<>();

    static {
        CharTypeMap.put(':', CharType.COLON);
        CharTypeMap.put('=', CharType.EQUALS);
        CharTypeMap.put('<', CharType.LESS);
        CharTypeMap.put('>', CharType.GREATER);

        CharTypeMap.put('+', CharType.SYMBOL);
        CharTypeMap.put('-', CharType.SYMBOL);
        CharTypeMap.put(',', CharType.SYMBOL);
        CharTypeMap.put('.', CharType.SYMBOL);
        CharTypeMap.put(';', CharType.SYMBOL);
        CharTypeMap.put('(', CharType.SYMBOL);
        CharTypeMap.put(')', CharType.SYMBOL);
        CharTypeMap.put('?', CharType.SYMBOL);
        CharTypeMap.put('!', CharType.SYMBOL);
        CharTypeMap.put('#', CharType.SYMBOL);
        CharTypeMap.put('[', CharType.SYMBOL);
        CharTypeMap.put(']', CharType.SYMBOL);
        CharTypeMap.put('{', CharType.SYMBOL);
        CharTypeMap.put('}', CharType.SYMBOL)
;
        CharTypeMap.put('*', CharType.STAR);
        CharTypeMap.put('/', CharType.SLASH);
        CharTypeMap.put('"', CharType.QUOTE);
        CharTypeMap.put('\\', CharType.BACKSLASH);

        for (char c = '0'; c <= '9'; c++)
            CharTypeMap.put(c, CharType.DIGIT);

        for (char c = 'a'; c <= 'z'; c++)
            CharTypeMap.put(c, CharType.ALPHA);

        for (char c = 'A'; c <= 'Z'; c++)
            CharTypeMap.put(c, CharType.ALPHA);

        CharTypeMap.put('a', CharType.ALPHA_KEY_START);
        CharTypeMap.put('b', CharType.ALPHA_KEY_START);
        CharTypeMap.put('c', CharType.ALPHA_KEY_START);
        CharTypeMap.put('d', CharType.ALPHA_KEY_START);
        CharTypeMap.put('e', CharType.ALPHA_KEY_START);
        CharTypeMap.put('i', CharType.ALPHA_KEY_START);
        CharTypeMap.put('n', CharType.ALPHA_KEY_START);
        CharTypeMap.put('o', CharType.ALPHA_KEY_START);
        CharTypeMap.put('p', CharType.ALPHA_KEY_START);
        CharTypeMap.put('t', CharType.ALPHA_KEY_START);
        CharTypeMap.put('v', CharType.ALPHA_KEY_START);
        CharTypeMap.put('w', CharType.ALPHA_KEY_START);

        CharTypeMap.put('A', CharType.ALPHA_KEY_START);
        CharTypeMap.put('B', CharType.ALPHA_KEY_START);
        CharTypeMap.put('C', CharType.ALPHA_KEY_START);
        CharTypeMap.put('D', CharType.ALPHA_KEY_START);
        CharTypeMap.put('E', CharType.ALPHA_KEY_START);
        CharTypeMap.put('I', CharType.ALPHA_KEY_START);
        CharTypeMap.put('N', CharType.ALPHA_KEY_START);
        CharTypeMap.put('O', CharType.ALPHA_KEY_START);
        CharTypeMap.put('P', CharType.ALPHA_KEY_START);
        CharTypeMap.put('T', CharType.ALPHA_KEY_START);
        CharTypeMap.put('V', CharType.ALPHA_KEY_START);
        CharTypeMap.put('W', CharType.ALPHA_KEY_START);
    }

    private static final Estado[][] EstadoTable = {
            /*       SYMBOL      DIGIT      ALPHA      COLON      EQUALS     LESS       GREATER    OTHER      ALPHA_KS   SLASH      STAR       QUOTE      BACKSLASH*/
            /*Z_0*/ { Estado.ESy, Estado.Z_2, Estado.Z_1, Estado.Z_3, Estado.ESy, Estado.Z_4, Estado.Z_5, Estado.Z_0, Estado.Z_9, Estado.Z10, Estado.ESy, Estado.Z13, Estado.ESy },
            /*Z_1*/ { Estado.EId, Estado.Z_1, Estado.Z_1, Estado.EId, Estado.EId, Estado.EId, Estado.EId, Estado.EId, Estado.Z_1, Estado.EId, Estado.EId, Estado.EId, Estado.EId },
            /*Z_2*/ { Estado.ENu, Estado.ENu, Estado.ENu, Estado.ENu, Estado.ENu, Estado.ENu, Estado.ENu, Estado.ENu, Estado.ENu, Estado.ENu, Estado.ENu, Estado.ENu, Estado.ENu },
            /*Z_3*/ { Estado.ECo, Estado.ECo, Estado.ECo, Estado.ECo, Estado.Z_6, Estado.ECo, Estado.ECo, Estado.ECo, Estado.ECo, Estado.ECo, Estado.ECo, Estado.ECo, Estado.ECo },
            /*Z_4*/ { Estado.ELT, Estado.ELT, Estado.ELT, Estado.ELT, Estado.Z_7, Estado.ELT, Estado.ELT, Estado.ELT, Estado.ELT, Estado.ELT, Estado.ELT, Estado.ELT, Estado.ELT },
            /*Z_5*/ { Estado.EGT, Estado.EGT, Estado.EGT, Estado.EGT, Estado.Z_8, Estado.EGT, Estado.EGT, Estado.EGT, Estado.EGT, Estado.EGT, Estado.EGT, Estado.EGT, Estado.EGT },
            /*Z_6*/ { Estado.EAs, Estado.EAs, Estado.EAs, Estado.EAs, Estado.EAs, Estado.EAs, Estado.EAs, Estado.EAs, Estado.EAs, Estado.EAs, Estado.EAs, Estado.EAs, Estado.EAs },
            /*Z_7*/ { Estado.ELE, Estado.ELE, Estado.ELE, Estado.ELE, Estado.ELE, Estado.ELE, Estado.ELE, Estado.ELE, Estado.ELE, Estado.ELE, Estado.ELE, Estado.ELE, Estado.ELE },
            /*Z_8*/ { Estado.EGE, Estado.EGE, Estado.EGE, Estado.EGE, Estado.EGE, Estado.EGE, Estado.EGE, Estado.EGE, Estado.EGE, Estado.EGE, Estado.EGE, Estado.EGE, Estado.EGE },
            /*Z_9*/ { Estado.EIK, Estado.Z_1, Estado.Z_9, Estado.EIK, Estado.EIK, Estado.EIK, Estado.EIK, Estado.EIK, Estado.Z_9, Estado.EIK, Estado.EIK, Estado.EIK, Estado.EIK },
            /*Z10*/ { Estado.ESy, Estado.ESy, Estado.ESy, Estado.ESy, Estado.ESy, Estado.ESy, Estado.ESy, Estado.ESy, Estado.ESy, Estado.ESy, Estado.Z11, Estado.ESy, Estado.ESy },
            /*Z11*/ { Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z12, Estado.Z11, Estado.Z11 },
            /*Z12*/ { Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z11, Estado.Z_0, Estado.Z11, Estado.Z11, Estado.Z11 },
            /*Z13*/ { Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.ESt, Estado.Z14 },
            /*Z14*/ { Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13, Estado.Z13 },
    };

    private static final Accion[][] actionTable = {
            /*       SYMBOL       DIGIT       ALPHA       COLON       EQUALS      LESS        GREATER     OTHER       ALPHA_KS    SLASH       STAR        QUOTE       BACKSLASH*/
            /*Z_0*/ { Accion.WRE, Accion.W_R, Accion.W_R, Accion.W_R, Accion.WRE, Accion.W_R, Accion.W_R, Accion.REA, Accion.W_R, Accion.W_R, Accion.WRE, Accion.W_R, Accion.WRE },
            /*Z_1*/ { Accion.END, Accion.W_R, Accion.W_R, Accion.END, Accion.END, Accion.END, Accion.END, Accion.REA, Accion.W_R, Accion.END, Accion.END, Accion.END, Accion.END },
            /*Z_2*/ { Accion.END, Accion.W_R, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.REA, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END },
            /*Z_3*/ { Accion.END, Accion.END, Accion.END, Accion.END, Accion.W_R, Accion.END, Accion.END, Accion.REA, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END },
            /*Z_4*/ { Accion.END, Accion.END, Accion.END, Accion.END, Accion.W_R, Accion.END, Accion.END, Accion.REA, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END },
            /*Z_5*/ { Accion.END, Accion.END, Accion.END, Accion.END, Accion.W_R, Accion.END, Accion.END, Accion.REA, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END },
            /*Z_6*/ { Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.REA, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END },
            /*Z_7*/ { Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.REA, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END },
            /*Z_8*/ { Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.REA, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END },
            /*Z_9*/ { Accion.END, Accion.W_R, Accion.W_R, Accion.END, Accion.END, Accion.END, Accion.END, Accion.REA, Accion.W_R, Accion.END, Accion.END, Accion.END, Accion.END },
            /*Z10*/ { Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.END, Accion.REA, Accion.END, Accion.END, Accion.REA, Accion.END, Accion.END },
            /*Z11*/ { Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.CLR, Accion.REA, Accion.REA },
            /*Z12*/ { Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.REA, Accion.CLR, Accion.REA, Accion.REA, Accion.REA },
            /*Z13*/ { Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.WRE, Accion.REA },
            /*Z14*/ { Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R, Accion.W_R },
    };

    private FileReader reader;

    private int readRow = 1;
    private int readColumn = 0;
    private int tokenRow;
    private int tokenColumn;


    private Token current;
    private Token next;

    private Estado currentState;

    private boolean eof = false;

    private char currentChar;
    private String currentString;

    /**
     * Constructor
     * @param reader lector de archivo
     */
    public Lexer(FileReader reader){
        this.reader = reader;
        read();
        lex();
    }

    /**
     * convierte a Lexema el string actual
     */
    public void lex(){
        current = next;

        if(eof) {
            next = Token.EOF_TOKEN;
            return;
        }

        boolean end = false;

        currentState = Estado.Z_0;
        currentString = "";

        while (currentState.toString().charAt(0)=='Z' && !end && !eof){
            Estado nextEstado = EstadoTable[currentState.ordinal()][getCharacterType(currentChar).ordinal()];
            switch (actionTable[currentState.ordinal()][getCharacterType(currentChar).ordinal()]){
                case WRE:
                    write();
                    read();
                    end = true;
                    break;
                case W_R:
                    write();
                    read();
                    break;
                case REA:
                    read();
                    break;
                case CLR:
                    clear();
                    read();
                    break;
                case END:
                    end = true;
                    break;
            }
            currentState = nextEstado;
        }
        end();
    }

    Token getCurrentToken(){
        return current;
    }

    public Token getNextToken(){
        return next;
    }

    private void read(){
        try {
            int readValue = reader.read();
            readColumn++;
            if(readValue == -1)
                eof = true;
            else {
                currentChar = (char) readValue;
                if(currentChar == '\n'){
                    readRow++;
                    readColumn = 0;
                }
                if(currentChar == '\r')
                    readColumn = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void write(){
        char upper = (currentState == Estado.Z13 || currentState == Estado.Z14) ? currentChar :
                Character.isAlphabetic(currentChar) ? Character.toUpperCase(currentChar) : currentChar;

        if(currentString.equals("")){
            tokenRow = readRow;
            tokenColumn = readColumn;
        }

        currentString = currentString + upper;
    }

    private void end(){
        try {
            switch (currentState) {
                case EIK:
                    if (esReservada(currentString)) {
                        next = new Token(TokenType.KEYWORD, SpecialCharacter.stringCharacterMap.get(currentString), tokenRow, tokenColumn);
                        break;
                    }
                case EId:
                    next = new Token(TokenType.IDENTIFIER, currentString, tokenRow, tokenColumn);
                    break;
                case ENu:
                    next = new Token(TokenType.NUMERAL, Long.parseLong(currentString), tokenRow, tokenColumn);
                    break;
                case ESt:
                    next = new Token(TokenType.STRING, currentString.substring(1, currentString.length() - 1), tokenRow, tokenColumn);
                    break;
                case ESy:
                case EAs:
                case ECo:
                case ELE:
                case ELT:
                case EGE:
                case EGT:
                    next = new Token(TokenType.SYMBOL, getOperatorChar(currentString), tokenRow, tokenColumn);
                    break;
            }
        }
        catch (InvalidTokenTypeException e){
            e.printStackTrace();
            System.err.println("token alienígena en: " + tokenRow + ":" + tokenColumn);
            System.exit(-1);
        }
    }
    

    private void clear() {
        currentString = "";
    }

    private static CharType getCharacterType(char character){
        if (CharTypeMap.containsKey(character))
            return CharTypeMap.get(character);

        return CharType.OTHER;
    }

    private static char getOperatorChar(String string){
        if (string.equals("<="))
            return SpecialCharacter.LESS_OR_EQUAL.value;
        if (string.equals(":="))
            return SpecialCharacter.ASSIGN.value;
        if (string.equals(">="))
            return SpecialCharacter.GREATER_OR_EQUAL.value;

        return string.charAt(0);
    }

    private static boolean esReservada(String string){
        return SpecialCharacter.stringCharacterMap.get(string) != null;
    }
}


