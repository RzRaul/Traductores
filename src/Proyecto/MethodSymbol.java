
import java.util.ArrayList;

/**
 * Class representing a procedure sym in the name list
 */
public class MethodSymbol implements Symbol {
    private String name;
    private MethodSymbol parent;
    private int procedureIndex;
    private ArrayList<Symbol> identifiers = new ArrayList<>();
    private int variableRelativeAddressCounter = 0;

    MethodSymbol(String name, int procedureIndex, MethodSymbol parent) {
        this.name = name;
        this.procedureIndex = procedureIndex;
        this.parent = parent;
    }

    MethodSymbol getParent() {
        return parent;
    }

    ArrayList<Symbol> getIdentifiers() {
        return identifiers;
    }

    @Override
    public int getProcedureIndex() {
        return procedureIndex;
    }

    @Override
    public String getName() {
        return name;
    }

    int getVariableLength(){
        return variableRelativeAddressCounter;
    }

    void addConstantSym(ConstSymbol sym){
        identifiers.add(sym);
    }

    void addVariableSym(String name){
        VariableSymbol sym = new VariableSymbol(name, procedureIndex, variableRelativeAddressCounter);

        variableRelativeAddressCounter += 4;

        identifiers.add(sym);
    }

    void makeVariableArray(int length){
        variableRelativeAddressCounter += (4 * (length - 1));
    }

    void addMethodSymbol(MethodSymbol sym) {
        identifiers.add(sym);
    }
}
