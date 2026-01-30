import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {
    private TablaSimbolos tamblaSimbolos = new TablaSimbolos();
    private int lineaActual = 1;
    // 2. Definición de la Tabla de Transición
    // Columnas: 0=Letra, 1=Dígito, 2=Espacio/Otro, 3=Punto
    // Estados: 0=Inicio, 1=ID, 2=NUM, 3=Aceptar ID, 4=Aceptar NUM, 5=Punto, 6=Float, 7=Aceptar Float
    private static final int[][] TABLA = {
            // L   D   _   P   .
            {  1,  2,  1,  0, -1 }, // S0 Inicio
            {  1,  1,  1,  3, -1 }, // S1 ID
            { -1,  2, -1,  4,  5 }, // S2 Entero
            { -1, -1, -1, -1, -1 }, // S3 Aceptar ID
            { -1, -1, -1, -1, -1 }, // S4 Aceptar NUM
            { -1,  6, -1, -1, -1 }, // S5 Punto decimal
            { -1,  6, -1,  7, -1 }  // S6 Decimal
    };

    // Verifica que la ID es válida (no solo "_" o "$")
    private boolean idIsValid(String lexema) {
        for(char c: lexema.toCharArray())
            if (Character.isLetter(c)) return true;
        return false;
    }

    // Mapeo de categorías
    private int getCategoria(char c) {
        if (Character.isLetter(c)) return 0;
        if (Character.isDigit(c))  return 1;
        if (c == '_' || c == '$')  return 2;
        if (c == '.')              return 4;
        return 3;
    }

    public List<Token> escanear(String entrada) {
        List<Token> tokens = new ArrayList<>();
        int estadoActual = 0;
        StringBuilder lexemaActual = new StringBuilder();
        int i = 0;

        while (i < entrada.length()) {
            char c = entrada.charAt(i);
            int cat = getCategoria(c);
            int siguienteEstado = TABLA[estadoActual][cat];

            if (siguienteEstado == -1) {
                System.err.println("Error Léxico: Carácter inesperado '" + c + "'");
                estadoActual = 0;
                lexemaActual.setLength(0);
                i++;
                continue;
            }

            if (siguienteEstado == 3) { // Aceptar ID [cite: 277]
                String lexema = lexemaActual.toString();
                if(idIsValid(lexema)){
                    // Consultar tabla para ver si es ID o KEYWORD
                    Simbolo simb = tamblaSimbolos.insertarOBuscar(lexema, "ID", lineaActual);
                    tokens.add(new Token(simb.tipoToken, lexema));
                }
                lexemaActual.setLength(0);
                estadoActual = 0;
            } else if (siguienteEstado == 4) { // Aceptar NUM
                String lexema = lexemaActual.toString();
                tamblaSimbolos.insertarOBuscar(lexema, "NUM", lineaActual);
                tokens.add(new Token("NUM", lexema));
                lexemaActual.setLength(0);
                estadoActual = 0;
            } else if (siguienteEstado == 7) { // Aceptar FLOAT
                String lexema = lexemaActual.toString();
                tamblaSimbolos.insertarOBuscar(lexema, "FLOAT", lineaActual);
                tokens.add(new Token("FLOAT", lexema));
                lexemaActual.setLength(0);
                estadoActual = 0;
            } else {
                if (siguienteEstado != 0) {
                    lexemaActual.append(c);
                }
                estadoActual = siguienteEstado;
                i++;
            }
        }
        return tokens;
    }

    public static void main(String[] args) {
        AnalizadorLexico lexer = new AnalizadorLexico();
        String codigo = "int suma = 100.1; if (contador == 25) suma = 50;";

        System.out.println("Entrada: " + codigo);
        List<Token> resultado = lexer.escanear(codigo);

        System.out.println("\nTokens encontrados:");
        for (Token t : resultado) {
            System.out.println(t);
        }

        // Desplegar contenido de la tabla [cite: 273]
        lexer.tamblaSimbolos.mostrarTabla();
    }
}