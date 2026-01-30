import java.util.HashMap;

public class TablaSimbolos {
    private HashMap<String, Simbolo> tabla;

    public TablaSimbolos() {
        this.tabla = new HashMap<>();
        instalarPalabraReservada("if");
        instalarPalabraReservada("else");
        instalarPalabraReservada("while");
        instalarPalabraReservada("int");
    }

    private void instalarPalabraReservada(String lexema) {
        tabla.put(lexema, new Simbolo(lexema, "KEYWORD", 0));
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
