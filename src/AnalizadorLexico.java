import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {
    private TablaSimbolos tablaSimbolos = new TablaSimbolos();
    private int lineaActual = 1;

    // Columnas: 0=L, 1=D, 2=_, 3=Esp, 4=., 5=", 6=Símbolos Simples (;(){}[]+-*/), 7=Símbolos Compuesto (< > ! =)
    private static final int[][] TABLA = {
            // L   D   _  Esp  .   "  SymS SymC
            {  1,  2,  1,  0, -1,  8, 11, 12 }, // S0: Inicio
            {  1,  1,  1,  3, -1, -1,  3,  3 }, // S1: ID
            { -1,  2, -1,  4,  5, -1,  4,  4 }, // S2: NUM
            { -1, -1, -1, -1, -1, -1, -1, -1 }, // S3: Aceptar ID
            { -1, -1, -1, -1, -1, -1, -1, -1 }, // S4: Aceptar NUM
            { -1,  6, -1, -1, -1, -1, -1, -1 }, // S5: Punto
            { -1,  6, -1,  7, -1, -1,  7,  7 }, // S6: FLOAT
            { -1, -1, -1, -1, -1, -1, -1, -1 }, // S7: Aceptar FLOAT
            {  8,  8,  8,  8,  8, 10,  8,  8 }, // S8: CADENA
            { -1, -1, -1, -1, -1, -1, -1, -1 }, // S10: Aceptar CADENA
            { -1, -1, -1, -1, -1, -1, -1, -1 }, // S11: Aceptar Símbolo Simple
    };

    private int getCategoria(char c) {

        if (Character.isLetter(c)) return 0;
        if (Character.isDigit(c))  return 1;
        if (c == '_' || c == '$')  return 2;
        if (Character.isWhitespace(c)) return 3;
        if (c == '.') return 4;
        if (c == '"') return 5;

        if (";(){}[]+-*/".indexOf(c) != -1)
            return 6;

        return -1;
    }

    public List<Token> escanear(String entrada) {

        List<Token> tokens = new ArrayList<>();
        int estadoActual = 0;
        StringBuilder lexemaActual = new StringBuilder();
        int i = 0;

        while (i < entrada.length()) {

            char c = entrada.charAt(i);

            if ("<>!=".indexOf(c) != -1) {

                String lexema;

                if (i + 1 < entrada.length() && entrada.charAt(i + 1) == '=') {
                    lexema = "" + c + "=";
                    i += 2;
                } else {
                    lexema = "" + c;
                    i++;
                }

                Simbolo simb = tablaSimbolos.insertarOBuscar(lexema, "COMP_OP", lineaActual);
                tokens.add(new Token(simb.tipoToken, lexema));
                continue;
            }

            int cat = getCategoria(c);

            if (cat == -1) {
                System.err.println("Error Léxico: '" + c + "'");
                i++;
                continue;
            }

            int siguienteEstado = TABLA[estadoActual][cat];

            if (siguienteEstado == -1) {
                System.err.println("Error Léxico: '" + c + "'");
                estadoActual = 0;
                lexemaActual.setLength(0);
                i++;
                continue;
            }

            if (siguienteEstado == 11) {
                String lexema = String.valueOf(c);
                Simbolo simb = tablaSimbolos.insertarOBuscar(lexema, "SYMBOL", lineaActual);
                tokens.add(new Token(simb.tipoToken, lexema));
                estadoActual = 0;
                i++;
            }

            else if (siguienteEstado == 3 || siguienteEstado == 4 || siguienteEstado == 7) {

                String lexema = lexemaActual.toString();

                String tipo = "ID";
                if (siguienteEstado == 4) tipo = "NUM";
                if (siguienteEstado == 7) tipo = "FLOAT";

                Simbolo simb = tablaSimbolos.insertarOBuscar(lexema, tipo, lineaActual);
                tokens.add(new Token(simb.tipoToken, lexema));

                lexemaActual.setLength(0);
                estadoActual = 0;
            }

            else if (siguienteEstado == 10) {
                lexemaActual.append(c);
                String lexema = lexemaActual.toString();

                tokens.add(new Token("CADENA", lexema));
                tablaSimbolos.insertarOBuscar(lexema, "CADENA", lineaActual);

                lexemaActual.setLength(0);
                estadoActual = 0;
                i++;
            }

            else {
                if (siguienteEstado != 0)
                    lexemaActual.append(c);

                estadoActual = siguienteEstado;
                i++;
            }
        }

        return tokens;
    }

    public static void main(String[] args) {
        AnalizadorLexico lexer = new AnalizadorLexico();
        String codigo = "$total = 150;\n" +
                "precioUnitario = 25.5;\n" +
                "$contador1 = 0;\n" +
                "\n" +
                "if ($contador1 <= 10) {\n" +
                "    mensaje = \"Total a pagar\";\n" +
                "    totalFinal = $total * precioUnitario;\n" +
                "}\n" +
                "\n" +
                "resultado@ = 50;\n" +
                "numero..error = 3.14.15;\n";

        System.out.println("Entrada: " + codigo);
        List<Token> resultado = lexer.escanear(codigo);

        for (Token t : resultado) System.out.println(t);
        lexer.tablaSimbolos.mostrarTabla();
    }
}