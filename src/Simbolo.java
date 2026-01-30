public class Simbolo {
    String lexema;
    String tipoToken;
    String tipoDato;
    int linea;

    public Simbolo (String lexema, String tipoToken, int linea) {
        this.lexema = lexema;
        this.tipoToken = tipoToken;
        this.linea = linea;
        this.tipoDato = "Desconocido";
    }
}