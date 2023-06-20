
/**
 * An interface for all name list entries (variable, constant and procedure)
 */
public interface Symbol {
    /**
     * @return the index of the procedure in which the entry is declared
     */
    int getProcedureIndex();

    /**
     * @return the name of the entry
     */
    String getName();
}
