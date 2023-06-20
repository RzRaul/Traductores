/**
 * Lista de las estructuras de la gramática y sus expresiones PL/0
 */
public enum Estructura {
    PROGRAM,
    BLOCK,
    CONST_DECLARATION_LIST,
    VAR_DECLARATION_LIST,
    PROCEDURE_DECLARATION,
    CONST_DECLARATION,
    VAR_DECLARATION,
    STATEMENT,
    ASSIGNMENT_STATEMENT,
    CONDITIONAL_STATEMENT,
    LOOP_STATEMENT,
    COMPOUND_STATEMENT,
    PROCEDURE_CALL,
    INPUT_STATEMENT,
    OUTPUT_STATEMENT,
    EXPRESSION,
    TERM,
    FACTOR,
    CONDITION,
    ARRAY_INDEX,
    LOGICAL_EXPRESSION,
    LOGICAL_TERM,
    LOGICAL_FACTOR;
    static {
        PROGRAM.expresions = new Expresion[] {
                 new Expresion(BLOCK, null, 1, Expresion.NO_ALTERNATIVE),
                 new Expresion('.',
                parser -> {
                    CodeGenerator codeGenerator = parser.getCodeGenerator();
                    SymTable symTable = parser.getSymTable();

                    codeGenerator.writeConstantBlock(symTable.getConstantBlock());
                    codeGenerator.writeNumberOfProcedures(symTable.getNumberOffunct());
                    codeGenerator.close();
                },
                2, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };
        
        BLOCK.expresions = new Expresion[] {
                 new Expresion(Estructura.CONST_DECLARATION_LIST, null, 1, 2),
                 new Expresion(3),
                 new Expresion(3),
                 new Expresion(Estructura.VAR_DECLARATION_LIST, null, 4, 5),
                 new Expresion(6),
                 new Expresion(6),
                 new Expresion(Estructura.PROCEDURE_DECLARATION, null, 7, 8),
                 new Expresion(6),
                 new Expresion(9,
                parser -> {
                    CodeGenerator codeGenerator = parser.getCodeGenerator();
                    SymTable symTable = parser.getSymTable();

                    int procedureIndex = symTable.getcurrentProcedureIndex();
                    int variableLength = symTable.getVariableLength();

                    codeGenerator.generateProcedureEntry(procedureIndex, variableLength);
                }),
                 new Expresion(Estructura.STATEMENT,
                parser -> {
                    parser.getCodeGenerator().generateProcedureReturn();

                    parser.getSymTable().endProcedure();
                },
                10, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };
        
        CONST_DECLARATION_LIST.expresions = new Expresion[] {
                 new Expresion(SpecialCharacter.CONST.value, null, 1, Expresion.NO_ALTERNATIVE),
                 new Expresion(CONST_DECLARATION, null, 2, Expresion.NO_ALTERNATIVE),
                 new Expresion(',', null, 1, 3),
                 new Expresion(';', null, 4, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };
        
        VAR_DECLARATION_LIST.expresions = new Expresion[] {
                 new Expresion(SpecialCharacter.VAR.value, null, 1, Expresion.NO_ALTERNATIVE),
                 new Expresion(VAR_DECLARATION, null, 2, Expresion.NO_ALTERNATIVE),
                 new Expresion(',', null, 1, 3),
                 new Expresion(';', null, 4, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };
        
        PROCEDURE_DECLARATION.expresions = new Expresion[] {
                 new Expresion(SpecialCharacter.PROCEDURE.value, null, 1, Expresion.NO_ALTERNATIVE),
                 new Expresion(TokenType.IDENTIFIER,
                parser -> {
                    SymTable symTable = parser.getSymTable();
                    Token token = parser.getLexer().getNextToken();

                    symTable.addProcedure(token);
                },
                2, Expresion.NO_ALTERNATIVE),
                 new Expresion(';', null, 3, Expresion.NO_ALTERNATIVE),
                 new Expresion(BLOCK, null, 4, Expresion.NO_ALTERNATIVE),
                 new Expresion(';', null, 5, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };
        
        CONST_DECLARATION.expresions = new Expresion[] {
                 new Expresion(TokenType.IDENTIFIER,
                parser -> {
                    SymTable symTable = parser.getSymTable();
                    Token token = parser.getLexer().getNextToken();

                    symTable.setConstantName(token);
                },
                1, Expresion.NO_ALTERNATIVE),
                 new Expresion('=', null, 2, Expresion.NO_ALTERNATIVE),
                 new Expresion(TokenType.NUMERAL,
                parser -> {
                    SymTable symTable = parser.getSymTable();
                    long value = parser.getLexer().getNextToken().getNumberValue();

                    symTable.addConstant(value);
                },
                3, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };
        
        VAR_DECLARATION.expresions = new Expresion[] {
                 new Expresion(TokenType.IDENTIFIER,
                parser -> {
                    SymTable symTable = parser.getSymTable();
                    Token token = parser.getLexer().getNextToken();

                    symTable.addVariable(token);
                },
                1, Expresion.NO_ALTERNATIVE),
                 new Expresion('[', null, 2, 4),
                 new Expresion(TokenType.NUMERAL,
                parser -> {
                    long arraySize = parser.getLexer().getNextToken().getNumberValue();

                    parser.getSymTable().makeVariableArray(arraySize);
                },
                3, Expresion.NO_ALTERNATIVE),
                 new Expresion(']', null, 5, Expresion.NO_ALTERNATIVE),
                 new Expresion(5),
                 Expresion.END_EXPRESION
        };
        
        STATEMENT.expresions = new Expresion[] {
                 new Expresion(ASSIGNMENT_STATEMENT, null, 7, 1),
                 new Expresion(CONDITIONAL_STATEMENT, null, 7, 2),
                 new Expresion(LOOP_STATEMENT, null, 7, 3),
                 new Expresion(COMPOUND_STATEMENT, null, 7, 4),
                 new Expresion(PROCEDURE_CALL, null, 7, 5),
                 new Expresion(INPUT_STATEMENT, null, 7, 6),
                 new Expresion(OUTPUT_STATEMENT, null, 7, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };

        ASSIGNMENT_STATEMENT.expresions = new Expresion[] {
                 new Expresion(TokenType.IDENTIFIER,
                parser -> parser.getSymTable().setLastVariableName(parser.getLexer().getNextToken().getStringValue()),
                1, Expresion.NO_ALTERNATIVE),
                 new Expresion(ARRAY_INDEX, null, 3, 2),
                 new Expresion(3,
                parser -> {
                    SymTable symTable = parser.getSymTable();
                    Lexer lexer = parser.getLexer();

                    String identifier = symTable.getLastVariableName();

                    Symbol sym = symTable.findIdentifier(identifier);
                    if (sym!= null){
                        if (sym instanceof VariableSymbol){
                            VariableSymbol variableSym = (VariableSymbol) sym;

                            int displacement = variableSym.getRelativeAddress();
                            int procedureIndex = variableSym.getProcedureIndex();
                            boolean isLocal = symTable.symIsLocal(variableSym);
                            boolean isMain = symTable.symIsInMain(variableSym);

                            parser.getCodeGenerator()
                                    .generatePushVariableAddress(displacement, procedureIndex, isLocal, isMain);
                        }
                        else
                            throw new InvalidIdentifierException(lexer.getNextToken(), "Identifier is not a variable");
                    }
                    else
                        throw new InvalidIdentifierException(lexer.getNextToken(), "Identifier not found");
                }),
                 new Expresion(SpecialCharacter.ASSIGN.value, null, 4, Expresion.NO_ALTERNATIVE),
                 new Expresion(EXPRESSION,
                parser -> parser.getCodeGenerator().generateStoreValue(), 5, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };

        CONDITIONAL_STATEMENT.expresions = new Expresion[] {
                 new Expresion(SpecialCharacter.IF.value, null, 1, Expresion.NO_ALTERNATIVE),
                 new Expresion(LOGICAL_EXPRESSION,
                parser -> {
                    CodeGenerator codeGenerator = parser.getCodeGenerator();

                    codeGenerator.saveCurrentAddress();
                    codeGenerator.generatePreliminaryJNOT();
                }, 2, Expresion.NO_ALTERNATIVE),
                 new Expresion(SpecialCharacter.THEN.value, null, 3, Expresion.NO_ALTERNATIVE),
                 new Expresion(STATEMENT, null, 4, Expresion.NO_ALTERNATIVE),
                 new Expresion(SpecialCharacter.ELSE.value,
                parser -> {
                    CodeGenerator codeGenerator = parser.getCodeGenerator();

                    codeGenerator.completeIFJNOT(true);
                    codeGenerator.saveCurrentAddress();
                    codeGenerator.generatePreliminaryELSEJUMP();
                }, 5, 6),
                 new Expresion(Estructura.STATEMENT,
                parser -> parser.getCodeGenerator().completeELSEJUMP(),
                7, Expresion.NO_ALTERNATIVE),
                 new Expresion(7,
                parser -> parser.getCodeGenerator().completeIFJNOT(false)),
                 Expresion.END_EXPRESION
        };

        LOOP_STATEMENT.expresions = new Expresion[] {
                 new Expresion(SpecialCharacter.WHILE.value,
                parser -> parser.getCodeGenerator().saveCurrentAddress(), 1, Expresion.NO_ALTERNATIVE),
                 new Expresion(LOGICAL_EXPRESSION,
                parser -> {
                    CodeGenerator codeGenerator = parser.getCodeGenerator();

                    codeGenerator.saveCurrentAddress();
                    codeGenerator.generatePreliminaryJNOT();
                }, 2, Expresion.NO_ALTERNATIVE),
                 new Expresion(SpecialCharacter.DO.value, null, 3, Expresion.NO_ALTERNATIVE),
                 new Expresion(STATEMENT,
                parser -> parser.getCodeGenerator().completeWHILE(), 4, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };

        COMPOUND_STATEMENT.expresions = new Expresion[] {
                 new Expresion(SpecialCharacter.BEGIN.value, null, 1, Expresion.NO_ALTERNATIVE),
                 new Expresion(STATEMENT, null, 2, Expresion.NO_ALTERNATIVE),
                 new Expresion(';', null, 1, 3),
                 new Expresion(SpecialCharacter.END.value, null, 4, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };

        PROCEDURE_CALL.expresions = new Expresion[] {
                 new Expresion(SpecialCharacter.CALL.value, null, 1, Expresion.NO_ALTERNATIVE),
                 new Expresion(TokenType.IDENTIFIER,
                parser -> {
                    Token token = parser.getLexer().getNextToken();
                    String identifier = token.getStringValue();

                    SymTable symTable = parser.getSymTable();
                    CodeGenerator codeGenerator = parser.getCodeGenerator();

                    Symbol sym = symTable.findIdentifier(identifier);

                    if (sym instanceof MethodSymbol) {
                        MethodSymbol procedureSym = (MethodSymbol) sym;

                        int index = symTable.getIndexOfProcedure(procedureSym);

                        codeGenerator.generateProcedureCall(index);

                    }
                    else
                        throw new InvalidIdentifierException(token, "Identifier is no procedure name");
                }, 2, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };

        INPUT_STATEMENT.expresions = new Expresion[] {
                 new Expresion('?', null, 1, Expresion.NO_ALTERNATIVE),
                 new Expresion(TokenType.IDENTIFIER,
                (parser) -> parser.getSymTable().setLastVariableName(parser.getLexer().getNextToken().getStringValue()),
                2, Expresion.NO_ALTERNATIVE),
                new Expresion(ARRAY_INDEX,
                parser -> parser.getCodeGenerator().generateGetValue(),4,3),
                 new Expresion(4,
                parser -> {
                    ASSIGNMENT_STATEMENT.expresions[2].getSemanticRoutine().apply(parser);

                    parser.getCodeGenerator().generateGetValue();
                }),
                 Expresion.END_EXPRESION
        };

        OUTPUT_STATEMENT.expresions = new Expresion[] {
                 new Expresion('!', null, 1, Expresion.NO_ALTERNATIVE),
                 new Expresion(TokenType.STRING,
                parser -> parser.getCodeGenerator().generatePutString(parser.getLexer().getNextToken().getStringValue()),
                3, 2),
                 new Expresion(EXPRESSION,
                parser -> parser.getCodeGenerator().generatePutValue(), 3, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };

        EXPRESSION.expresions = new Expresion[] {
                 new Expresion('-', null, 2, 1),
                 new Expresion(TERM, null, 3, Expresion.NO_ALTERNATIVE),
                 new Expresion(TERM,
                parser -> parser.getCodeGenerator().generateNegativeSign(), 3, Expresion.NO_ALTERNATIVE),
                 new Expresion(4),
                 new Expresion('+', null, 5, 6),
                 new Expresion(TERM,
                parser -> parser.getCodeGenerator().generateAddOperator(), 3, Expresion.NO_ALTERNATIVE),
                 new Expresion('-', null, 7, 8),
                 new Expresion(TERM,
                parser -> parser.getCodeGenerator().generateSubtractOperator(), 3, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };

        TERM.expresions = new Expresion[] {
                 new Expresion(FACTOR, null, 1, Expresion.NO_ALTERNATIVE),
                 new Expresion(2),
                 new Expresion('*', null, 3, 4),
                 new Expresion(FACTOR,
                parser -> parser.getCodeGenerator().generateMultiplyOperator(), 1, Expresion.NO_ALTERNATIVE),
                 new Expresion('/', null, 5, 6),
                 new Expresion(FACTOR,
                parser -> parser.getCodeGenerator().generateDivideOperator(), 1, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };

        FACTOR.expresions = new Expresion[] {
                 new Expresion(TokenType.NUMERAL,
                parser -> {
                    SymTable symTable = parser.getSymTable();
                    CodeGenerator codeGenerator = parser.getCodeGenerator();
                    long constantValue = parser.getLexer().getNextToken().getNumberValue();

                    symTable.addConstant(constantValue);
                    int constantIndex = symTable.getIndexOfConstant(constantValue);

                    codeGenerator.generatePushConstant(constantIndex);

                }, 7, 1),
                 new Expresion('(', null, 2, 4),
                 new Expresion(EXPRESSION, null, 3, Expresion.NO_ALTERNATIVE),
                 new Expresion(')', null, 7, Expresion.NO_ALTERNATIVE),
                 new Expresion(TokenType.IDENTIFIER,
                parser -> parser.getSymTable().setLastVariableName(parser.getLexer().getNextToken().getStringValue()),
                5, Expresion.NO_ALTERNATIVE),
                new Expresion(ARRAY_INDEX, 
                parser -> parser.getCodeGenerator().generateSwap(), 7, 6),
                 new Expresion(7,
                parser -> {
                    SymTable symTable = parser.getSymTable();
                    CodeGenerator codeGenerator = parser.getCodeGenerator();
                    Token token = parser.getLexer().getNextToken();
                    String identifier = symTable.getLastVariableName();

                    Symbol sym = symTable.findIdentifier(identifier);
                    if (sym != null){
                        if (sym instanceof VariableSymbol){
                            VariableSymbol variableSym = (VariableSymbol) sym;

                            boolean isLocal = symTable.symIsLocal(variableSym);
                            boolean isMain = symTable.symIsInMain(variableSym);
                            int displacement = variableSym.getRelativeAddress();
                            int procedureIndex = variableSym.getProcedureIndex();

                            codeGenerator.generatePushVariableValue(displacement, procedureIndex, isLocal, isMain);
                        }

                        else if (sym instanceof ConstSymbol){
                            ConstSymbol constantSym = (ConstSymbol) sym;

                            int constantIndex = constantSym.getIndex();

                            codeGenerator.generatePushConstant(constantIndex);
                        }
                        else
                            throw new InvalidIdentifierException(token, "Identifier is not a variable or constant");
                    }
                    else
                        throw new InvalidIdentifierException(token, "Identifier not found");
                }),
                 Expresion.END_EXPRESION
        };

        CONDITION.expresions = new Expresion[] {
                 new Expresion(SpecialCharacter.ODD.value, null, 1, 2),
                 new Expresion(EXPRESSION,
                parser -> parser.getCodeGenerator().generateOdd(), 10, Expresion.NO_ALTERNATIVE),
                 new Expresion(EXPRESSION, null, 3, Expresion.NO_ALTERNATIVE),
                 new Expresion('=',
                parser -> parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue()), 9, 4),
                 new Expresion('#',
                parser -> parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue()), 9, 5),
                 new Expresion('<',
                parser -> parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue()), 9, 6),
                 new Expresion(SpecialCharacter.LESS_OR_EQUAL.value,
                parser -> parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue()), 9, 7),
                 new Expresion('>',
                parser -> parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue()),  9, 8),
                 new Expresion(SpecialCharacter.GREATER_OR_EQUAL.value,
                parser -> parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue()), 9, Expresion.NO_ALTERNATIVE),
                 new Expresion(EXPRESSION,
                parser -> parser.getCodeGenerator().generateComparisonOperator(), 10, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };

        ARRAY_INDEX.expresions = new Expresion[] {
                /* 0 */ new Expresion('[',
                parser -> {
                    SymTable symTable = parser.getSymTable();
                    CodeGenerator codeGenerator = parser.getCodeGenerator();
                    Token token = parser.getLexer().getNextToken();

                    String identifier = symTable.getLastVariableName();

                    Symbol sym = symTable.findIdentifier(identifier);

                    if (sym != null) {
                        if (sym instanceof VariableSymbol) {
                            VariableSymbol variableSymbol = (VariableSymbol) sym;

                            boolean isLocal = symTable.symIsLocal(variableSymbol);
                            boolean isMain = symTable.symIsInMain(variableSymbol);
                            int displacement = variableSymbol.getRelativeAddress();
                            int procedureIndex = variableSymbol.getProcedureIndex();

                            codeGenerator.generatePushVariableAddress(displacement, procedureIndex, isLocal, isMain);
                        }
                        else
                            throw new InvalidIdentifierException(token, "Indexing only possible on variables");
                    }
                    else
                        throw new InvalidIdentifierException(token, "Identifier not found");
                },
                1, Expresion.NO_ALTERNATIVE),
                /* 1 */ new Expresion(EXPRESSION, null, 2, Expresion.NO_ALTERNATIVE),
                /* 2 */ new Expresion(']',
                parser -> {
                    SymTable symTable = parser.getSymTable();
                    symTable.addConstant(4);
                    int index = symTable.getIndexOfConstant(4);

                    CodeGenerator codeGenerator = parser.getCodeGenerator();

                    codeGenerator.generatePushConstant(index);
                    codeGenerator.generateMultiplyOperator();
                    codeGenerator.generateAddOperator();
                },
                3, Expresion.NO_ALTERNATIVE),
                /* 3 */ Expresion.END_EXPRESION
        };
        LOGICAL_EXPRESSION.expresions = new Expresion[] {
                 new Expresion(LOGICAL_TERM, null, 1, Expresion.NO_ALTERNATIVE),
                 new Expresion(SpecialCharacter.OR.value, null, 2, 3),
                 new Expresion(LOGICAL_TERM,
                parser -> parser.getCodeGenerator().generateAddOperator(),
                3, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };

        LOGICAL_TERM.expresions = new Expresion[] {
                 new Expresion(SpecialCharacter.NOT.value, null, 1, 2),
                 new Expresion(LOGICAL_FACTOR,
                parser -> {
                    CodeGenerator codeGenerator = parser.getCodeGenerator();
                    SymTable symTable = parser.getSymTable();

                    symTable.addConstant(1);
                    int index = symTable.getIndexOfConstant(1);

                    codeGenerator.generatePushConstant(index);
                    codeGenerator.setComparisonOperator('<');
                    codeGenerator.generateComparisonOperator();
                }, 3, Expresion.NO_ALTERNATIVE),
                 new Expresion(LOGICAL_FACTOR, null, 3, Expresion.NO_ALTERNATIVE),
                 new Expresion(SpecialCharacter.AND.value, null, 4, 7),
                 new Expresion(SpecialCharacter.NOT.value, null, 5, 6),
                 new Expresion(LOGICAL_FACTOR,
                parser -> {
                    LOGICAL_TERM.expresions[1].getSemanticRoutine().apply(parser);

                    parser.getCodeGenerator().generateMultiplyOperator();
                }, 3, Expresion.NO_ALTERNATIVE),
                 new Expresion(LOGICAL_FACTOR,
                parser -> parser.getCodeGenerator().generateMultiplyOperator(), 3, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };

        LOGICAL_FACTOR.expresions = new Expresion[] {
                 new Expresion(CONDITION, null, 4, 1),
                 new Expresion('{', null, 2, Expresion.NO_ALTERNATIVE),
                 new Expresion(LOGICAL_EXPRESSION, null, 3, Expresion.NO_ALTERNATIVE),
                 new Expresion('}', null, 4, Expresion.NO_ALTERNATIVE),
                 Expresion.END_EXPRESION
        };
    }

    private Expresion[] expresions;

    public Expresion[] getExpresions() {
        return expresions;
    }
}
