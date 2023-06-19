import java.util.*;
public class StructSymbol extends ScopedSymbol implements Scope {
    Map<String, Symbol> fields = new HashMap<String, Symbol>();
    public StructSymbol(String name, Scope parent) {
        super(name, parent);
    }

    public Symbol resolveMember(String name) {
        return fields.get(name);
    }
    public Map<String, Symbol> getMembers() {
        return fields;
    }
    public String print() {
        String str = new String();
        fields.forEach((k,v) -> {
            str.concat(v.toString());
        });
        return str;
    }

}