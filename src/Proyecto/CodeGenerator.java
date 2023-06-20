
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Stack;

/**
 * Clase encargarda de generar el código de salida
 */
public class CodeGenerator {
    private RandomAccessFile outputFile;

    private long currentProcedureStartAddress = 0;

    private char comparisonOperator;

    private Stack<Long> jumpAddressStack;

    private static final HashMap<Character, OperationCode> comparisonOperatorMap = new HashMap<>();

    static {
        comparisonOperatorMap.put('=', OperationCode.COMPARE_EQUAL);
        comparisonOperatorMap.put('#', OperationCode.COMPARE_NOT_EQUAL);
        comparisonOperatorMap.put('<', OperationCode.COMPARE_LESS);
        comparisonOperatorMap.put('>', OperationCode.COMPARE_GREATER);
        comparisonOperatorMap.put(SpecialCharacter.LESS_OR_EQUAL.value, OperationCode.COMPARE_LESS_OR_EQUAL);
        comparisonOperatorMap.put(SpecialCharacter.GREATER_OR_EQUAL.value, OperationCode.COMPARE_GREATER_OR_EQUAL);
    }

    /**
     * Constructor, que manipula el archivo de salida
     */
    public CodeGenerator(String outFile) throws IOException {
        outputFile = new RandomAccessFile(outFile, "rw");
        outputFile.setLength(0);
        outputFile.seek(4);

        jumpAddressStack = new Stack<>();
    }

    /**
     * Genera el código de un método
     */
    public void generateProcedureEntry(int procedureIndex, int variableLength) throws IOException {
        currentProcedureStartAddress = outputFile.getFilePointer();

        outputFile.write(OperationCode.ENTRY_PROCEDURE.code);
        outputFile.write(shortToBytes((short) 0));
        outputFile.write(shortToBytes((short) procedureIndex));
        outputFile.write(shortToBytes((short) variableLength));
    }

    /**
     * Genera el codigo de salida de un método
     */
    public void generateProcedureReturn() throws IOException {
        outputFile.write(OperationCode.RETURN_PROCEDURE.code);

        long currentPosition = outputFile.getFilePointer();

        long procedureCodeLength = currentPosition - currentProcedureStartAddress;
        byte[] codeLengthBytes = shortToBytes((short) procedureCodeLength);

        outputFile.seek(currentProcedureStartAddress + 1);
        outputFile.write(codeLengthBytes);
        
        outputFile.seek(currentPosition);
    }

    /**
     * Genera el código de una variable 
     */
    public void generatePushVariableValue(int displacement, int procedureIndex, boolean isLocal, boolean isMain) throws IOException {
        if (isMain)
            outputFile.write(OperationCode.PUSH_MAIN_VARIABLE_VALUE.code);
        else if (isLocal)
            outputFile.write(OperationCode.PUSH_LOCAL_VARIABLE_VALUE.code);
        else
            outputFile.write(OperationCode.PUSH_GLOBAL_VARIABLE_VALUE.code);

        outputFile.write(shortToBytes((short) displacement));

        if (!isLocal && !isMain)
            outputFile.write(shortToBytes((short) procedureIndex));
    }

    /**
     * genera el código para almacenar el valor de una variable de dirección en la pila
     */
    public void generatePushVariableAddress(int displacement, int procedureIndex, boolean isLocal, boolean isMain) throws IOException {
        if (isMain)
            outputFile.write(OperationCode.PUSH_MAIN_VARIABLE_ADDRESS.code);
        else if (isLocal)
            outputFile.write(OperationCode.PUSH_LOCAL_VARIABLE_ADDRESS.code);
        else
            outputFile.write(OperationCode.PUSH_GLOBAL_VARIABLE_ADDRESS.code);

        outputFile.write(shortToBytes((short) displacement));

        if (!isLocal && !isMain)
            outputFile.write(shortToBytes((short) procedureIndex));
    }

    /**
     * Guarda una constante en la pila
     */
    public void generatePushConstant(int constantIndex) throws IOException {
        outputFile.write(OperationCode.PUSH_CONST.code);

        outputFile.write(shortToBytes((short) constantIndex));
    }

    /**
     * Generates the store value code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateStoreValue() throws IOException {
        outputFile.write(OperationCode.STORE_VALUE.code);
    }

    /**
     * Generates the store put code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generatePutValue() throws IOException {
        outputFile.write(OperationCode.PUT_VALUE.code);
    }

    /**
     * Generates the get value code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateGetValue() throws IOException {
        outputFile.write(OperationCode.GET_VALUE.code);
    }

    /**
     * Generates the negative sign code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateNegativeSign() throws IOException {
        outputFile.write(OperationCode.NEGATIVE_SIGN.code);
    }

    /**
     * Generates the add operator code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateAddOperator() throws IOException {
        outputFile.write(OperationCode.OPERATOR_ADD.code);
    }

    /**
     * Generates the subtraction operator code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateSubtractOperator() throws IOException {
        outputFile.write(OperationCode.OPERATOR_SUBTRACT.code);
    }

    /**
     * Generates the multiplication operator code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateMultiplyOperator() throws IOException {
        outputFile.write(OperationCode.OPERATOR_MULTIPLY.code);
    }

    /**
     * Generates the division operator code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateDivideOperator() throws IOException {
        outputFile.write(OperationCode.OPERATOR_DIVIDE.code);
    }

    /**
     * Generates the odd code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateOdd() throws IOException {
        outputFile.write(OperationCode.ODD.code);
    }

    /**
     * Pushes the current code generation position to the stack for generating jump addresses later
     * @throws IOException if an I/O error occurs while writing
     */
    public void saveCurrentAddress() throws IOException {
        jumpAddressStack.push(outputFile.getFilePointer());
    }

    /**
     * Generates the JNOT code with the jump distance set to 0
     * @throws IOException if an I/O error occurs while writing
     */
    public void generatePreliminaryJNOT() throws IOException {
        outputFile.write(OperationCode.JUMP_NOT.code);

        outputFile.write(shortToBytes((short) 0));
    }

    /**
     * Generates the JUMP code with the jump distance set to 0
     * @throws IOException if an I/O error occurs while writing
     */
    public void generatePreliminaryELSEJUMP() throws IOException {
        outputFile.write(OperationCode.JUMP.code);

        outputFile.write(shortToBytes((short) 0));
    }

    /**
     * Completes the JNOT code for the conditional statement
     * @param elsePresent should be true if and only if the conditional statement has an ELSE branch
     * @throws IOException if an I/O error occurs while writing
     */
    public void completeIFJNOT(boolean elsePresent) throws IOException {
        long savedAddress = jumpAddressStack.pop();

        long currentAddress = outputFile.getFilePointer();

        long relativeAddress = currentAddress - savedAddress - 3
                + (elsePresent ? 3 : 0);

        outputFile.seek(savedAddress + 1);
        outputFile.write(shortToBytes((short) relativeAddress));
        outputFile.seek(currentAddress);
    }

    /**
     * Completes the JUMP code for the ELSE branch of a conditional statement
     */
    public void completeELSEJUMP() throws IOException {
        long savedAddress = jumpAddressStack.pop();

        long currentAddress = outputFile.getFilePointer();

        long relativeAddress = currentAddress - savedAddress - 3;

        outputFile.seek(savedAddress + 1);
        outputFile.write(shortToBytes((short) relativeAddress));
        outputFile.seek(currentAddress);
    }

    /**
     * Completes the jump codes for a loop statement
     * @throws IOException if an I/O error occurs while writing
     */
    public void completeWHILE() throws IOException {
        long jNotAddress = jumpAddressStack.pop();
        long conditionAddress = jumpAddressStack.pop();

        long currentAddress = outputFile.getFilePointer();

        long loopStartJumpDistance = conditionAddress - currentAddress - 3;
        long loopExitJumpDistance = currentAddress - jNotAddress;

        outputFile.write(OperationCode.JUMP.code);
        outputFile.write(shortToBytes((short) loopStartJumpDistance));

        currentAddress = outputFile.getFilePointer();

        outputFile.seek(jNotAddress + 1);

        outputFile.write(shortToBytes((short) loopExitJumpDistance));

        outputFile.seek(currentAddress);
    }

    /**
     *Genera la llamada a un método
     */
    public void generateProcedureCall(int procedureIndex) throws IOException {
        outputFile.write(OperationCode.CALL.code);

        outputFile.write(shortToBytes((short) procedureIndex));
    }

    /**
     * Prepara una comparación
     */
    public void setComparisonOperator(char comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    /**
     * Genera un operador de comparación
     */
    public void generateComparisonOperator() throws IOException {
        outputFile.write(comparisonOperatorMap.get(comparisonOperator).code);
    }

    /**
     * Genera una colocación de cadena
     */
    public void generatePutString(String string) throws IOException {
        outputFile.write(OperationCode.PUT_STRING.code);

        int arrayLength = string.length() + 1;
        byte[] bytes = new byte[arrayLength];
        bytes[arrayLength -1] = 0;
        System.arraycopy(string.getBytes(), 0, bytes, 0 , arrayLength - 1);

        outputFile.write(bytes);
    }

    /**
     * Genera un SWAP de operadores
     */
    public void generateSwap() throws IOException {
        outputFile.write(OperationCode.SWAP.code);
    }

    /**
     * construye una tabla de constantes
    
     */
    public void writeConstantBlock(long[] constantBlock) throws IOException {
        for (long constant : constantBlock)
            outputFile.write(longToBytes(constant));
    }
    /**
     * coloca el número de procedimientos
     */
    public void writeNumberOfProcedures(int numberOfProcedures) throws IOException {
        outputFile.seek(0);
        outputFile.write(longToBytes((long) numberOfProcedures));
    }

    /**
     * Closes the output file
     * @throws IOException if an I/O error occurs while closing
     */
    public void close() throws IOException {
        outputFile.close();
    }

    private static byte[] longToBytes(long value){
        byte[] bytes = new byte[4];

        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >> 24) & 0xFF);

        return bytes;
    }

    private static byte[] shortToBytes(short value){
        byte[] bytes = new byte[2];

        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);

        return bytes;
    }
}
