import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class PSEUDOCompiler {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: PSEUDOCompiler <archivo fuente> [<archivo salida>]");
            System.exit(1);
        }

        String inputFileName = args[0];

        FileReader reader = null;
        try {
            reader = new FileReader(inputFileName);
        } catch (FileNotFoundException e) {
            System.err.println("No se encontró el archivo fuente " + inputFileName);
            System.exit(2);
        }

        Lexer lexer = new Lexer(reader);

        String outFileName =
                args.length >= 2 ?
                        args[1] :
                inputFileName.endsWith(".PSEUDO") ?
                        inputFileName.substring(0, inputFileName.length() - 3) + "cl0" :
                        inputFileName + ".cl0";

        CodeGenerator codeGenerator = null;
        try {
            codeGenerator = new CodeGenerator(outFileName);
        } catch (IOException e) {
            System.err.println("No se puede abrir o crear el archivo de salida "
                    + outFileName
                    + ". La ruta requiere permisos especiales?");
            System.exit(3);
        }

        Parser parser = new Parser(lexer, codeGenerator);

        try {
            parser.parse();
            System.out.println("Compilacion exitosa.");
        }
        catch (FatalSemanticRoutineException e) {
            System.out.println("Error: " + e.toString());
            (new File(outFileName)).delete();
            System.exit(4);
        }
        catch (IOException e) {
            System.err.println("I/O error while parsing.");
            System.exit(5);
        }
    }
}
