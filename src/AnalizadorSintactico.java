import java.util.List;

public class AnalizadorSintactico {
    private List<Token> tokens;
    private int pos = 0;

    public AnalizadorSintactico(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token actual() {
        return pos < tokens.size() ? tokens.get(pos) : new Token("EOF", "");
    }

    private void consumir() { pos++; }

    // Punto de entrada para expresiones
    public NodoAST analizarExpresion() {
        return expresion();
    }

    // Maneja Suma y Resta (Menor prioridad)
    private NodoAST expresion() {
        NodoAST nodo = termino();
        while (actual().tipo.equals("SUMA") || actual().tipo.equals("RESTA")) {
            String op = actual().lexeme;
            consumir();
            nodo = new NodoOperacion(op, nodo, termino());
        }
        return nodo;
    }

    // Maneja Multiplicación y División (Mayor prioridad)
    private NodoAST termino() {
        NodoAST nodo = factor();
        while (actual().tipo.equals("MULTIPLICATION") || actual().tipo.equals("DIVITION")) {
            String op = actual().lexeme;
            consumir();
            nodo = new NodoOperacion(op, nodo, factor());
        }
        return nodo;
    }

    // Maneja Números, IDs y Paréntesis
    private NodoAST factor() {
        Token t = actual();
        if (t.tipo.equals("NUM") || t.tipo.equals("FLOAT") || t.tipo.equals("ID")) {
            consumir();
            return new NodoValor(t.lexeme);
        } else if (t.tipo.equals("OPEN_PARENT")) {
            consumir();
            NodoAST nodo = expresion();
            if (actual().tipo.equals("CLOSE_PARENT")) consumir();
            return nodo;
        }
        return null;
    }
}