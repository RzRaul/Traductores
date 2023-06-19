public class AlcanceAnidado {
    public static void main(String args[]) {
        Scope currentScope;

        currentScope = new GlobalScope();
        currentScope.define(new BuiltInTypeSymbol("int"));
        currentScope.define(new BuiltInTypeSymbol("float"));
        currentScope.define(new BuiltInTypeSymbol("void"));
        currentScope.define(new BuiltInTypeSymbol("return"));
        
        BuiltInTypeSymbol t1 = (BuiltInTypeSymbol) currentScope.resolve("int");
        if(t1 == null) { System.out.println("Solo se acepta valores tipo int | float | void"); } // Excepcion de tipo
        currentScope.define(new VariableSymbol("i", t1));
        System.out.printf(" %s contenido: %s\n", currentScope.getScopeName(), currentScope.print());

        BuiltInTypeSymbol returnType = (BuiltInTypeSymbol) currentScope.resolve("float");
   
        if(returnType == null) { } // Excepcion de tipo

        BuiltInTypeSymbol t2 = (BuiltInTypeSymbol) currentScope.resolve("int");
        if(t2 == null) { } // Excepcion de tipo

        BuiltInTypeSymbol t3 = (BuiltInTypeSymbol) currentScope.resolve("float");
        if(t3 == null) { } // Excepcion de tipo

        VariableSymbol parameters[] = { new VariableSymbol("x", t2), new VariableSymbol("y", t3)};

        currentScope = new MethodSymbol("f", parameters, currentScope);
        System.out.printf(" %s contenido: %s\n", currentScope.getScopeName(), currentScope.print());

        t1 = (BuiltInTypeSymbol) currentScope.resolve("float");
        if(t1 == null) { } // Excepcion de tipo

        currentScope.define(new VariableSymbol("i", t1));
        System.out.printf(" %s contenido: %s\n", currentScope.getScopeName(), currentScope.print());

        currentScope = new LocalScope(currentScope);

        t1 = (BuiltInTypeSymbol) currentScope.resolve("float");
        if(t1 == null) { } // Excepcion de tipo

        currentScope.define(new VariableSymbol("z", t1));
        System.out.printf(" %s contenido: %s\n", currentScope.getScopeName(), currentScope.print());

        VariableSymbol v1 = (VariableSymbol) currentScope.resolve("x");
        if(v1 ==  null) { } // Excepcion de tipo

        VariableSymbol v2 = (VariableSymbol) currentScope.resolve("y");
        if(v2 == null) { } // Excepcion de tipo

        v1 = (VariableSymbol) currentScope.resolve("i");
        if(v1 == null) { } // Excepcion de tipo

        v2 = (VariableSymbol) currentScope.resolve("z");
        if(v2 == null) { } // Excepcion de tipo

        currentScope = currentScope.getEnclosingScope();

        // Segundo localScope
        currentScope = new LocalScope(currentScope);

        t1 = (BuiltInTypeSymbol) currentScope.resolve("float");
        if(t1 == null) { } // Excepcion de tipo

        currentScope.define(new VariableSymbol("z", t1));
        System.out.printf(" %s contenido: %s\n", currentScope.getScopeName(), currentScope.print());

        VariableSymbol v3 = (VariableSymbol) currentScope.resolve("i");
        if(v3 ==  null) { } // Excepcion de tipo

        VariableSymbol v4 = (VariableSymbol) currentScope.resolve("i");
        if(v4 == null) { } // Excepcion de tipo

        v3 = (VariableSymbol) currentScope.resolve("i");
        if(v3 == null) { } // Excepcion de tipo

        v4 = (VariableSymbol) currentScope.resolve("z");
        if(v4 == null) { } // Excepcion de tipo

        currentScope = currentScope.getEnclosingScope();

        t2 = (BuiltInTypeSymbol) currentScope.resolve("return");
        if(t1 == null) { } // Excepcion de tipo

        currentScope.define(new VariableSymbol("i", t2));
        System.out.printf(" %s contenido: %s\n", currentScope.getScopeName(), currentScope.print());

        currentScope = currentScope.getEnclosingScope();

        BuiltInTypeSymbol returnType2 = (BuiltInTypeSymbol) currentScope.resolve("void");

        if(returnType2 == null) { } // Excepcion de tipo

        currentScope = new MethodSymbol("g", parameters, currentScope);
        System.out.printf(" %s contenido: %s\n", currentScope.getScopeName(), currentScope.print());

        MethodSymbol t4 = (MethodSymbol) currentScope.resolve("f");
        if(t4 == null) { } // Excepcion de tipo

        VariableSymbol v5 = (VariableSymbol) currentScope.resolve("i");
        if(v5 ==  null) { } // Excepcion de tipo

        VariableSymbol v6 = (VariableSymbol) currentScope.resolve("z");
        if(v6 == null) { } // Excepcion de tipo

        currentScope = currentScope.getEnclosingScope();

        System.out.println("Funciona Correctamente!");
    }
}