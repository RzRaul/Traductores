public class Symbol {
    String name;
    Type type;
    public Scope scope;
    public Symbol(String name) { this.name = name;}
    public Symbol(String name, Type type) { this(name); this.type = type; }
    public Symbol(String name, Scope scope) { this(name); this.scope = scope; }
    public String getName() { return name; }
    public String toString() {
        if( type!=null ) return '<'+getName()+":"+type+'>';
        return getName();
    }
}
