import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {

    // 2. Definición de la Tabla de Transición
    // Columnas: 0=Letra, 1=Dígito, 2=Espacio/Otro, 3=Punto
    // Estados: 0=Inicio, 1=ID, 2=NUM, 3=Aceptar ID, 4=Aceptar NUM, 5=Punto, 6=Float, 7=Aceptar Float
    private static final int[][] TABLA = {
            //  L   D   O   P
            {  1,  2,  1,  0, -1 }, // S0 Inicio
            {  1,  1,  1,  3, -1 }, // S1 ID
            { -1,  2, -1,  4,  5 }, // S2 Entero
            { -1, -1, -1, -1, -1 }, // S3 Aceptar ID
            { -1, -1, -1, -1, -1 }, // S4 Aceptar NUM
            { -1,  6, -1, -1, -1 }, // S5 Punto decimal
            { -1,  6, -1,  7, -1 }  // S6 Decimal
    };

    // Mapeo de categorías
    private int getCategoria(char c) {
        if (Character.isLetter(c)) return 0;
        if (Character.isDigit(c))  return 1;
        if (c == '_')              return 2;
        if (c == '.')              return 4;
        return 3;
    }

    public List<Token> escanear(String entrada) {
        List<Token> tokens = new ArrayList<>();
        int estadoActual = 0;
        StringBuilder lexemaActual = new StringBuilder();
        int i = 0;

        // Simulamos el manejo del buffer de entrada con un puntero 'i'
        while (i < entrada.length()) {
            char c = entrada.charAt(i);
            int cat = getCategoria(c);
            int siguienteEstado = TABLA[estadoActual][cat];

            if (siguienteEstado == -1) {
                System.err.println("Error Léxico: Carácter inesperado '" + c + "'");
                break;
            }

            // Lógica de transición
            if (siguienteEstado == 3) { // Aceptar ID
                tokens.add(new Token("ID", lexemaActual.toString()));
                lexemaActual.setLength(0); // Limpiar buffer
                estadoActual = 0;
                // No incrementamos 'i' para que el delimitador se procese en S0
            } else if (siguienteEstado == 4) { // Aceptar NUM
                tokens.add(new Token("NUM", lexemaActual.toString()));
                lexemaActual.setLength(0);
                estadoActual = 0;
            } else if (siguienteEstado == 7) {
                tokens.add(new Token("FLOAT", lexemaActual.toString()));
                lexemaActual.setLength(0);
                estadoActual = 0;
            } else {
                // Seguir acumulando caracteres en el estado actual (S1 o S2)
                if (siguienteEstado != 0) {
                    lexemaActual.append(c);
                }
                estadoActual = siguienteEstado;
                i++; // Avanzar puntero
            }
        }

        // Procesar el último token si quedó algo en el buffer al terminar el string
        if (lexemaActual.length() > 0) {
            String tipo;
            if (estadoActual == 1) tipo = "ID";
            else if (estadoActual == 2) tipo = "NUM";
            else if (estadoActual == 6) tipo = "FLOAT";
            else tipo = "ERROR";
            tokens.add(new Token(tipo, lexemaActual.toString()));
        }

        return tokens;
    }

    public static void main(String[] args) {
        AnalizadorLexico lexer = new AnalizadorLexico();
        String codigo = "Suma1 = 100.1; contador = 25; patito1 = 50; temperatura = 24.5; x = 5; y = 10; ";

        System.out.println("Entrada: " + codigo);
        List<Token> resultado = lexer.escanear(codigo);

        System.out.println("\nTokens encontrados:");
        for (Token t : resultado) {
            System.out.println(t);
        }
    }
}