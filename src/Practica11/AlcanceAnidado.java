public class AlcanceAnidado {
    public static void main(String args[]) {
        Scope currentScope;

        currentScope = new GlobalScope();

        currentScope.define(new BuiltInTypeSymbol("int"));
        currentScope.define(new BuiltInTypeSymbol("float"));
        currentScope.define(new BuiltInTypeSymbol("void"));
        currentScope.define(new BuiltInTypeSymbol("return"));

        //Struct A
        StructSymbol ss = new StructSymbol("A",currentScope);
        currentScope.define(ss);
        currentScope = ss;

        //int x
        BuiltInTypeSymbol t = (BuiltInTypeSymbol)currentScope.resolve("int");
        if(t==null){}
        currentScope.define(new VariableSymbol("x",t));
        System.out.printf("%s contiene: %s\n",currentScope.getScopeName(),ss.toString());

        //float y
        t = (BuiltInTypeSymbol)currentScope.resolve("float");
        if(t==null){}
        currentScope.define(new VariableSymbol("y",t));

        currentScope = currentScope.getEnclosingScope(); //Sale del struct
        
        //Void g()
        BuiltInTypeSymbol rt = (BuiltInTypeSymbol)currentScope.resolve("void");
        if(rt==null){}
        VariableSymbol vr[] = {new VariableSymbol("return",rt)};

        MethodSymbol m = new MethodSymbol("g",vr,currentScope);
        currentScope.define(m);
        currentScope = m;
        currentScope = new LocalScope(currentScope);

        //A
        ss = (StructSymbol)currentScope.resolve("A");
        if(ss==null){}

        currentScope.define(new VariableSymbol("a",ss));

        //a.x=1
        ss = (StructSymbol)currentScope.resolve("a");
        ss.resolveMember("x");
        currentScope = currentScope.getEnclosingScope(); //Sale del local

        System.out.println("Funciona Correctamente!");
    }
}