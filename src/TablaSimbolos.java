import java.util.HashMap;

class TablaSimbolos {
    private HashMap<String, Simbolo> tabla = new HashMap<>();
    public TablaSimbolos() {
        String[][] reservadas = {
                {"if", "KEY_IF"}, {"else", "KEY_ELSE"}, {"while", "KEY_WHILE"}, {"for", "KEY_FOR"},
                {"int", "KEY_INT"}, {"float", "KEY_FLOAT"}, {"string", "KEY_STRING"},
                {";", "END_SENTENCE"}, {"=", "KEY_ASSIGNATION"}, {"(", "OPEN_PARENT"}, {")", "CLOSE_PARENT"},
                {"+", "SUMA"}, {"-", "RESTA"}, {"*", "MULTIPLICATION"}, {"/", "DIVITION"},
                {"<", "KEY_LESS"}, {">", "KEY_GREATER"}, {"<=", "KEY_LESS_EQUAL"}, {">=", "KEY_GREATER_EQUAL"},
                {"!=", "KEY_DIFFERENT"}, {"==", "KEY_EQUAL"}
        };
        for (String[] r : reservadas) tabla.put(r[0], new Simbolo(r[0], r[1]));
    }
    public Simbolo insertarOBuscar(String lex, String tipoDef) {
        if (!tabla.containsKey(lex)) tabla.put(lex, new Simbolo(lex, tipoDef));
        return tabla.get(lex);
    }
    public void mostrar() {
        System.out.println("\n--- TABLA DE SÍMBOLOS ---");
        tabla.forEach((k, v) -> System.out.println("Lexema: [" + k + "] | Tipo: " + v.tipoToken));
    }
}