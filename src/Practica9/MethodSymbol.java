import java.util.*;

public class MethodSymbol extends Symbol implements Scope {
    Scope enclosingScope;
    Map<String, Symbol> members = new HashMap<String, Symbol>();

    public MethodSymbol(String name, VariableSymbol[] orderedAtgs, Scope encloScope) {
        super(name);
        this.enclosingScope = encloScope;

        if(orderedAtgs != null) {
            for(VariableSymbol v : orderedAtgs) {
                define(v);
            }
        }
    }

    public String getScopeName() {
        return name;
    }

    public Scope getEnclosingScope() {
        return enclosingScope;
    }
    
    public void define(Symbol sym) {
        members.put(sym.name, sym);
    }

    public Symbol resolve(String name) {
        Symbol s = members.get(name);

        if(s!=null)
            return s;

        if(enclosingScope != null)
            return enclosingScope.resolve(name);
        
        return null;
    }   
}