import java.util.Map;

class NodoValor extends NodoAST {
    String val;
    public NodoValor(String val) { this.val = val; }

    @Override
    double evaluar(Map<String, Double> memoria) {
        try {
            return Double.parseDouble(val); // Es un número
        } catch (NumberFormatException e) {
            // Es una variable, buscamos su valor en memoria
            return memoria.getOrDefault(val, 0.0);
        }
    }

    @Override void imprimir(String p, boolean esIz) { System.out.println(p + (esIz ? "├── " : "└── ") + val); }
}