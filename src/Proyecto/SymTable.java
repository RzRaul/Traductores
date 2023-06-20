

import java.util.ArrayList;

/**
 * Clase para la tabla de símbolos
 */
public class SymTable {
    private MethodSymbol mainFunction = new MethodSymbol( "", 0, null);
    private ArrayList<MethodSymbol> funct = new ArrayList<>();
    private ArrayList<Long> constants = new ArrayList<>();

    private String nextConstantName = null;

    private MethodSymbol currentScope = mainFunction;

    private String lastVariableName;

    /**
     * Constructor
     */
    public SymTable() {
        funct.add(mainFunction);
    }

    /**
     * Adds a constant to the constant block and a ConstSymbol if a name was specified previously
     * @param value the constant value
     */
    public void addConstant(long value){
        if (nextConstantName == null){
            if (!constants.contains(value))
                constants.add(value);

            return;
        }

        if (!constants.contains(value))
            constants.add(value);

        int index = constants.indexOf(value);

        ConstSymbol sym = new ConstSymbol(currentScope.getProcedureIndex(), value, index, nextConstantName);

        currentScope.addConstantSym(sym);

        nextConstantName = null;
    }

    /**
     * Adds a VariableSym to the current procedure
     * @param token the token containing the variable identifier
     * @throws InvalidIdentifierException if the identifier already exists in the current scope
     */
    public void addVariable(Token token) throws InvalidIdentifierException {
        String name = token.getStringValue();
        if (findIdentifierLocal(name))
            throw new InvalidIdentifierException(token, "Identifier already exists, cannot be declared again");

        currentScope.addVariableSym(name);
    }

    /**
     * Sets the name of a variable for array indexing
     * @param lastVariableName the name of the last variable
     */
    public void setLastVariableName(String lastVariableName) {
        this.lastVariableName = lastVariableName;
    }

    /**
     * @return the name of the last variable
     */
    public String getLastVariableName() {
        return lastVariableName;
    }

    /**
     * Converts the last added variable into an array with length items
     * @param length the number of items in the array
     */
    public void makeVariableArray(long length) {
        currentScope.makeVariableArray((int) length);
    }

    /**
     * Adds a MethodSymbol to the name list
     * @param token the token containing the procedure identifier
     * @throws InvalidIdentifierException if the identifier already exists in the current scope
     */
    public void addProcedure(Token token) throws InvalidIdentifierException {
        String name = token.getStringValue();
        if (findIdentifierLocal(name))
            throw new InvalidIdentifierException(token, "Identifier already exists, cannot be declared again");

        MethodSymbol sym = new MethodSymbol(name, funct.size(), currentScope);

        currentScope.addMethodSymbol(sym);

        funct.add(sym);

        currentScope = sym;
    }

    /**
     * Sets the current procedure's parent to the new current procedure
     */
    public void endProcedure() {
        if (currentScope.equals(mainFunction))
            return;

        currentScope = currentScope.getParent();
    }

    private boolean findIdentifierLocal(String identifier){
        return currentScope.getIdentifiers().stream().anyMatch((sym -> sym.getName().equals(identifier)));
    }

    public void setConstantName(Token token) throws InvalidIdentifierException {
        String name = token.getStringValue();
        if (findIdentifierLocal(name))
            throw new InvalidIdentifierException(token, "Identifier already exists, cannot be declared again");

        nextConstantName = name;
    }

    /**
     * @return the constant block
     */
    public long[] getConstantBlock(){
        return constants.stream().mapToLong(value -> value).toArray();
    }

    /**
     * @return the number of funct
     */
    public int getNumberOffunct() {
        return funct.size();
    }

    /**
     * @return the index of the current procedure
     */
    public int getcurrentProcedureIndex(){
        return currentScope.getProcedureIndex();
    }

    /**
     * @return the length of the current procedure's variable block
     */
    public int getVariableLength(){
        return currentScope.getVariableLength();
    }

    /**
     * Looks for a Symbol starting in the current procedure going through the parent funct up to the main procedure
     * @param name the name of the sym to be found
     * @return a SymTable sym with the name as specified or null if no identifier has that name.
     * If multiple such entries exist, it returns the one at the lowest level.
     */
    public Symbol findIdentifier(String name){
        for (MethodSymbol searchProcedure = currentScope;
             searchProcedure != null;
             searchProcedure = searchProcedure.getParent()){

            Symbol sym = searchProcedure.getIdentifiers().stream()
                    .filter(Symbol -> Symbol.getName().equals(name)).findFirst().orElse(null);

            if (sym != null)
                return sym;

        }

        return null;
    }

    /**
     * Checks if the sym is a local identifier in the current procedure
     * @param sym the sym to be checked
     * @return true if and only if the sym belongs to the current procedure
     */
    public boolean symIsLocal(Symbol sym){
        return sym.getProcedureIndex() == funct.indexOf(currentScope);
    }

    /**
     * Checks if the sym is an identifier from the main procedure
     * @param sym the sym to be checked
     * @return true if and only if the sym belongs to the main procedure
     */
    public boolean symIsInMain(Symbol sym){
        return sym.getProcedureIndex() == funct.indexOf(mainFunction);
    }

    /**
     * Gets the index of a constant value from the constant block
     * @param constantValue the value of the constant
     * @return the index of the constant in the constant block
     */
    public int getIndexOfConstant(long constantValue){
        return constants.indexOf(constantValue);
    }
    public int getIndexOfProcedure(MethodSymbol procedure){
        return funct.indexOf(procedure);
    }
}
