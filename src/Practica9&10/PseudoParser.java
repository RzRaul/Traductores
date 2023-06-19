import java.util.ArrayList;

public class PseudoParser {
    ArrayList<PseudoLexer.Token> tokens;
    int tokenIndex = 0;
    SymbolTable tabla;

    public boolean parse(ArrayList<PseudoLexer.Token> tokens) {
        this.tokens = tokens;

        tabla = new SymbolTable();
        tabla.initTypeSystem();

        System.out.println("\n\n********** Reglas de produccion **********\n");
        return programa();
    }

    private boolean token(String name) {
        if(tokens.get(tokenIndex).type.name().equals(name)) {
            System.out.println(tokens.get(tokenIndex).type.name() + " " + tokens.get(tokenIndex).data);
            tokenIndex++;
            return true;
        }

        return false;
    }

    // <Programa> ::= inicio-programa <Enunciados> fin-p´rograma
    private boolean programa() {
        System.out.println("<Programa> --> inicio-programa <Enunciados> fin-programa");
        GlobalScope globalScope = new GlobalScope();
        if(token("INICIOPROGRAMA"))
            if(enunciados())
                if(token("FINPROGRAMA")){
                    //globalScope.print();
                    globalScope.getEnclosingScope();
                    if(tokenIndex == tokens.size()-1)
                        return true;
                }
                    
        return false;
    }

    // <Enunciados> ::= <Enunciado> <Enunciados> | Vacio
    private boolean enunciados() {
        System.out.println("<Enunciados> --> <Enunciado> <Enunciados> | Vacio");

        if(token("FINPROGRAMA")) {
            tokenIndex--;
            return true;
        }

        if(token("FINSI")) {
            tokenIndex--;
            return true;
        }

        if(token("FINMIENTRAS")) {
            tokenIndex--;
            return true;
        }

        if(token("FINREPITE")) {
            tokenIndex--;
            return true;
        }

        if(enunciado())
            if(enunciados())
                return true;
        
        return false;
    }

    // <Enunciado> ::= <Asignacion> | <Leer> | <Escribir> | <Si> | <Mientras> | <Declaracion>
    private boolean enunciado() {
        System.out.println("<Enunciado> --> <Declaracion> | <Asignacion> | <Leer> | <Escribir> | <Si> | <Mientras> | <repite> | <variables>");
        if(enunciadoVariables()) {
            System.out.println("<Enunciado> --> <Variables>");
            return true;
        }
        if(enunciadoAsignacion()) {
            System.out.println("<Enunciado> --> <Asignacion>");
            return true;
        }

        if(enunciadoLeer()) {
            System.out.println("<Enunciado> --> <Leer>");
            return true;
        }
        
        if(enunciadoEscribir()) {
            System.out.println("<Enunciado> --> <Escribir>");
            return true;
        }
        
        if(enunciadoSi()) {
            System.out.println("<Enunciado> --> <Si>");
            return true;
        }
        
        if(enunciadoMientas()) {
            System.out.println("<Enunciado> --> <Mientras>");
            return true;
        }

        if(enunciadoRepite()) {
            System.out.println("<Enunciado> --> <Repite>");
            return true;
        }

        

        return false;
    }

    // <Asignacion> ::= <Variable> = <Expresiom>
    private boolean enunciadoAsignacion() {
        System.out.println("<Asignacion> --> <Variable> = <Expresion>");

        if(token("VARIABLE")) {
            if(token("IGUAL")) {
                if(expresion()) {
                    // Verificar que cada variable aparezca en la expresion este declarada
                    return true;
                }
            }
        }
        
        return false;
    }

    // <Expresion> ::= <Valor> | <Operacion>
    private boolean expresion() {
        System.out.println("<Expresion> --> <Operacion> | <Valor>");

        if(operacion())
            return true;
        
        if(valor())
            return true;

        return false;
    }

    // <Valor> ::= <Variable> | <Numero>
    private boolean valor() {
        System.out.println("<Valor> --> <Variable> | <Numero>");

        if(token("VARIABLE"))
            return true;
        
        if(token("NUMERO"))
            return true;
        
        return false;
    }

    // <Operacion> ::= <Valor> <Operador-aritmetico> <Valor>
    private boolean operacion() {
        System.out.println("<Operacion> --> <Valor> <Operador-aritmetico> <Valor>");

        int tokenIndexAux = tokenIndex;

        if(valor())
            if(token("OPARITMETICO"))
                if(valor())
                    return true;
        
        tokenIndex = tokenIndexAux;
        return false;
    }

    // <Leer> ::= leer <Cadena> , <Variable>
    private boolean enunciadoLeer() {
        System.out.println("<Leer> --> Leer <Cadena> , <Variable>");

        if(token("LEER"))
            if(token("CADENA"))
                if(token("COMA"))
                    if(token("VARIABLE"))
                        return true;

        return false;
    }

    // <Escribir> ::= escribir <Cadena> | escribir <Cadena> , <Variable>
    private boolean enunciadoEscribir() {
        System.out.println("<Escribir> --> escribir <Cadena> | escribir <Cadena> , <Variable>");

        int tokenIndexAux = tokenIndex;

        if(token("ESCRIBIR"))
            if(token("CADENA"))
                if(token("COMA"))
                    if(token("VARIABLE"))
                        return true;

        tokenIndex = tokenIndexAux;

        if(token("ESCRIBIR"))
            if(token("CADENA"))
                return true;

        return false;
    }

    // <Si> ::= si <Comparacion> entonces <Enunciados> fin-si
    private boolean enunciadoSi() {
        System.out.println("<Si> --> si <Comparacion> entonces <Enunciados> fin-si");

        if(token("SI"))
            if(comparacion())
                if(token("ENTONCES"))
                    if(enunciados())
                        if(token("FINSI"))
                            return true;

        return false;
    }

    // <Comparacion> ::= ( <Valor> <Operador-relacional> <Valor> )
    private boolean comparacion() {
        System.out.println("<Comparacion> --> ( <Valor> <Operador-relacional> <Valor> )");

        if(token("PARENTESISIZQ"))
            if(valor())
                if(token("OPERACIONAL"))
                    if(valor())
                        if(token("PARENTESISDER"))
                            return true;

        return false;
    }

    // <Mientras> ::= mientras <Comparacion> <Enunciados> fin-mientras
    private boolean enunciadoMientas() {
        System.out.println("<Mientas> --> mientras <Comparacion> <Enunciados> fin-mientras");

        if(token("MIENTRAS"))
            if(comparacion())
                if(enunciados())
                    if(token("FINMIENTRAS"))
                        return true;

        return false;
    }

    private boolean enunciadoRepite() {
        System.out.println("<Repite> --> repite ( <Asigancion> , <Variable> ) <Enunciados> fin-repite");  
        
        if(token("REPITE"))
            if(token("PARENTESISIZQ"))
                if(enunciadoAsignacion())
                    if(token("COMA"))
                        if(token("VARIABLE"))
                            if(token("PARENTESISDER"))
                                if(enunciados())
                                    if(token("FINREPITE"))
                                        return true;
                    
        return false;
    }

    private boolean enunciadoVariables() {
        System.out.println("<Variables> --> variables enteras | flotantes : <Multivariable> ");

        if(token("VARIABLES"))
            if(varType())
                if(token("DOSPUNTOS"))
                    if(multiVariable())
                        return true;
                    
        return false;
    }

    private boolean multiVariable(){
        System.out.println("<MultiVariable> --> <Variable> <COMMA> <Variable> | <Variable>");
        if (token("VARIABLE"))
            if (token("COMA")){
                if (multiVariable())
                    return true;
            }else{
                // tokenIndex--;
                return true;
            }
        return false;
            
    }

    private boolean varType() {
        System.out.println("<Variables> --> variables ENTERAS | flotantes");

        if(token("ENTERAS"))
            return true;
        
        if(token("FLOTANTES"))
            return true;
        
        return false;
    }
}