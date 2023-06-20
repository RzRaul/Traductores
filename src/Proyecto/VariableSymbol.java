

public class VariableSymbol implements Symbol {
    private String name;
    private int procedureIndex;
    private int relativeAddress;

    VariableSymbol(String name, int procedureIndex, int relativeAddress) {
        this.name = name;
        this.procedureIndex = procedureIndex;
        this.relativeAddress = relativeAddress;
    }

    @Override
    public int getProcedureIndex() {
        return procedureIndex;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getRelativeAddress() {
        return relativeAddress;
    }
}
