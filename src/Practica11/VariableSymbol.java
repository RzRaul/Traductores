public class VariableSymbol extends Symbol {
    public VariableSymbol(String name, Type type) { super(name, type); } 
    public VariableSymbol(String name, Scope scope) { super(name, scope); }  
    public VariableSymbol(String name, StructSymbol ss) { super(name, ss); }
}
