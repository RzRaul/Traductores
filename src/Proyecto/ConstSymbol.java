
/**
 * Clase para representar una entrada de constante en la tabla de símbolos
 */
public class ConstSymbol implements Symbol {
    private int procedureIndex;
    private long value;
    private int index;
    private String name;

    ConstSymbol(int procedureIndex, long value, int index, String name) {
        this.procedureIndex = procedureIndex;
        this.value = value;
        this.index = index;
        this.name = name;
    }

    @Override
    public int getProcedureIndex() {
        return procedureIndex;
    }

    @Override
    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }
}
