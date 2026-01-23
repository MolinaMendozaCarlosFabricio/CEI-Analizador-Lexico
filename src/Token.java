// Código en Java, analizador léxico base
// AnalizadorLexico.java

import java.util.ArrayList;
import java.util.List;

// 1. Clase para representar el Token (Componente Léxico)
class Token {
    String tipo;
    String lexeme;

    public Token(String tipo, String lexeme) {
        this.tipo = tipo;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return String.format("<%s, \"%s\">", tipo, lexeme);
    }
}