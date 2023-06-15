
public class p2 {
    public static void main(String[] args) {

        ListLexer lexer = new ListLexer("[a, b]");
        Token t = lexer.nextToken();
        while(t.type != Lexer.EOF_TYPE) {
            System.out.println(t);
            t = lexer.nextToken();
        }
        System.out.println(t);
    }
}

