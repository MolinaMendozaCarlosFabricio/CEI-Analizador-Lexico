import java.util.HashMap;

public class TablaSimbolos {
    private HashMap<String, Simbolo> tabla;

    public TablaSimbolos() {
        this.tabla = new HashMap<>();
        // Estructuras básicas
        instalarPalabraReservada("if", "KEY_IF");
        instalarPalabraReservada("else", "KEY_ELSE");
        instalarPalabraReservada("while", "KEY_WHILE");
        instalarPalabraReservada("return", "KEY_RETURN");
        instalarPalabraReservada("then", "KEY_THEN");
        instalarPalabraReservada("case", "KEY_CASE");
        instalarPalabraReservada("break", "KEY_BREAK");
        instalarPalabraReservada("for", "KEY_FOR");
        instalarPalabraReservada("do", "KEY_DO");

        // Clases
        instalarPalabraReservada("class", "KEY_CLASS");
        instalarPalabraReservada("new", "KEY_NEW");
        instalarPalabraReservada("interface", "KEH_INTERFACE");
        instalarPalabraReservada("package", "KEY_PACKAGE");

        // Modificadores de acceso
        instalarPalabraReservada("public", "KEY_PUBLIC");
        instalarPalabraReservada("private", "KEY_PRIVATE");
        instalarPalabraReservada("static", "KEY_STATIC");
        instalarPalabraReservada("final", "KEY_FINAL");
        instalarPalabraReservada("void", "KEY_VOID");
        instalarPalabraReservada("main", "KEY_MAIN");

        // Data types
        instalarPalabraReservada("int", "KEY_INT");
        instalarPalabraReservada("long", "KEY_LONG");
        instalarPalabraReservada("float", "KEY_FLOAT");
        instalarPalabraReservada("double", "KEY_DOUBLE");
        instalarPalabraReservada("char", "KEY_CHAR");
        instalarPalabraReservada("string", "KEY_STRING");
        instalarPalabraReservada("date", "KEY_DATE");
        instalarPalabraReservada("local_date", "LOCAL_DATE");
        instalarPalabraReservada("local_time", "LOCAL_TIME");

        // Delimitadores
        instalarPalabraReservada(";", "END_SENTENCE");
        instalarPalabraReservada("{", "INIT_BLOCK");
        instalarPalabraReservada("}", "END_BLOCK");
        instalarPalabraReservada("(", "OPEN_PARENT");
        instalarPalabraReservada(")", "CLOSE_PARENT");

        instalarPalabraReservada("=", "KEY_ASSIGNATION");

        // Operadores matemáticos
        instalarPalabraReservada("+", "SUMA");
        instalarPalabraReservada("-", "RESTA");
        instalarPalabraReservada("*", "MULTIPLICATION");
        instalarPalabraReservada("/", "DIVITION");

        // Agrega esto al constructor de TablaSimbolos
        instalarPalabraReservada("<", "KEY_LESS");
        instalarPalabraReservada(">", "KEY_GREATER");
        instalarPalabraReservada("<=", "KEY_LESS_EQUAL");
        instalarPalabraReservada(">=", "KEY_GREATER_EQUAL");
        instalarPalabraReservada("!=", "KEY_DIFFERENT");
        instalarPalabraReservada("==", "KEY_EQUAL");

        // Operadores lógicos
        instalarPalabraReservada("and", "AND");
        instalarPalabraReservada("or", "OR");
        instalarPalabraReservada("not", "NEGATION");
    }

    private void instalarPalabraReservada(String lexema, String tokenType) {
        tabla.put(lexema, new Simbolo(lexema, tokenType, 0));
    }

    public Simbolo insertarOBuscar(String lexema, String tipoDefault, int linea) {
        if (tabla.containsKey(lexema)) {
            return tabla.get(lexema);
        }
        Simbolo nuevo = new Simbolo(lexema, tipoDefault, linea);
        tabla.put(lexema, nuevo);
        return nuevo;
    }

    public void mostrarTabla() {
        System.out.println("\n--- Contenido de la Tabla de Símbolos ---");
        tabla.forEach((k, v) ->
                System.out.println("Lexema: " + k + " | Tipo: " + v.tipoToken + " | Línea: " + v.linea));
    }
}
