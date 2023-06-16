/*

*/

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PseudoCompiler {
    PseudoLexer pLexer;

    public static void main(String[] args) {
        new PseudoCompiler();
    }

    public PseudoCompiler() {
        pLexer = new PseudoLexer();

        String input = "";

        try {
            FileReader reader = new FileReader("ejercicio");
            int character;

            while ((character = reader.read()) != -1) {
                input += (char) character;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("********** Programa fuente **********\n");
        System.out.println(input);

        ArrayList<PseudoLexer.Token> tokens = pLexer.getTokens(input);

        System.out.println("\n\n********** Tokens **********\n");

        for (PseudoLexer.Token token : tokens)
            System.out.println(token);
        
    }
}
