import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalizadorLexico {
    private static Map<String, Double> memoria = new HashMap<>();
    private TablaSimbolos tabla = new TablaSimbolos();

    public List<Token> escanear(String entrada) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        while (i < entrada.length()) {
            char c = entrada.charAt(i);
            if (Character.isWhitespace(c)) { i++; continue; }

            // Números (Enteros y Decimales)
            if (Character.isDigit(c)) {
                StringBuilder num = new StringBuilder();
                boolean esFloat = false;
                while (i < entrada.length() && (Character.isDigit(entrada.charAt(i)) || entrada.charAt(i) == '.')) {
                    if (entrada.charAt(i) == '.') esFloat = true;
                    num.append(entrada.charAt(i)); i++;
                }
                String lex = num.toString();
                tokens.add(new Token(tabla.insertarOBuscar(lex, esFloat ? "FLOAT" : "NUM").tipoToken, lex));
                continue;
            }

            // Identificadores y Palabras Reservadas
            if (Character.isLetter(c) || c == '_' || c == '$') {
                StringBuilder id = new StringBuilder();
                while (i < entrada.length() && (Character.isLetterOrDigit(entrada.charAt(i)) || entrada.charAt(i) == '_')) {
                    id.append(entrada.charAt(i)); i++;
                }
                String lex = id.toString();
                tokens.add(new Token(tabla.insertarOBuscar(lex, "ID").tipoToken, lex));
                continue;
            }

            // Operadores Compuestos y Simples
            if ("<>!=".indexOf(c) != -1) {
                String lex = String.valueOf(c);
                if (i + 1 < entrada.length() && entrada.charAt(i + 1) == '=') {
                    lex += "="; i++;
                }
                i++;
                tokens.add(new Token(tabla.insertarOBuscar(lex, "OP").tipoToken, lex));
                continue;
            }

            // Símbolos Simples
            if (";(){}[]+-*/".indexOf(c) != -1) {
                String lex = String.valueOf(c);
                tokens.add(new Token(tabla.insertarOBuscar(lex, "SYM").tipoToken, lex));
                i++; continue;
            }

            System.err.println("Error Léxico: Caracter desconocido [" + c + "]");
            i++;
        }
        return tokens;
    }

    public static void main(String[] args) {
        AnalizadorLexico lexer = new AnalizadorLexico();
        String codigo = "radio = 10; pi = 3.14; area = pi * (radio * radio);";

        System.out.println("--- 1. LISTA DE TOKENS ---");
        List<Token> tokens = lexer.escanear(codigo);
        tokens.forEach(System.out::println);

        System.out.println("\n--- 2. PROCESAMIENTO Y ÁRBOLES ---");
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).tipo.equals("KEY_ASSIGNATION")) {
                String var = tokens.get(i-1).lexeme;
                List<Token> sub = new ArrayList<>();
                int j = i + 1;
                while (j < tokens.size() && !tokens.get(j).tipo.equals("END_SENTENCE")) sub.add(tokens.get(j++));

                NodoAST arbol = new Parser(sub).parsear();
                double res = arbol.evaluar(lexer.memoria);
                lexer.memoria.put(var, res);

                System.out.println("\nAsignación: " + var + " = " + res);
                arbol.imprimir("  ", false);
                i = j;
            }
        }

        lexer.tabla.mostrar();
        System.out.println("\n--- 3. MEMORIA FINAL (VALORES REALES) ---");
        lexer.memoria.forEach((k, v) -> System.out.println(k + " => " + v));
    }
}