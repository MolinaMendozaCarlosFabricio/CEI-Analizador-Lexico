import java.util.List;

class Parser {
    private List<Token> tokens; private int p = 0;
    public Parser(List<Token> t) { this.tokens = t; }
    private Token actual() { return p < tokens.size() ? tokens.get(p) : new Token("EOF", ""); }
    private void consumir() { p++; }

    public NodoAST parsear() { return expresion(); }
    private NodoAST expresion() {
        NodoAST n = termino();
        while (actual().tipo.equals("SUMA") || actual().tipo.equals("RESTA")) {
            String op = actual().lexeme; consumir(); n = new NodoOperacion(op, n, termino());
        }
        return n;
    }
    private NodoAST termino() {
        NodoAST n = factor();
        while (actual().tipo.equals("MULTIPLICATION") || actual().tipo.equals("DIVITION")) {
            String op = actual().lexeme; consumir(); n = new NodoOperacion(op, n, factor());
        }
        return n;
    }
    private NodoAST factor() {
        Token t = actual();
        if (t.tipo.equals("NUM") || t.tipo.equals("FLOAT") || t.tipo.equals("ID")) {
            consumir(); return new NodoValor(t.lexeme);
        } else if (t.tipo.equals("OPEN_PARENT")) {
            consumir(); NodoAST n = expresion();
            if(actual().tipo.equals("CLOSE_PARENT")) consumir(); return n;
        }
        return new NodoValor("?");
    }
}