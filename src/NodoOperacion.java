import java.util.Map;

class NodoOperacion extends NodoAST {
    String op; NodoAST izq, der;
    public NodoOperacion(String op, NodoAST izq, NodoAST der) { this.op = op; this.izq = izq; this.der = der; }

    @Override
    double evaluar(Map<String, Double> memoria) {
        double vIzq = izq.evaluar(memoria);
        double vDer = der.evaluar(memoria);
        switch (op) {
            case "+": return vIzq + vDer;
            case "-": return vIzq - vDer;
            case "*": return vIzq * vDer;
            case "/": return vDer != 0 ? vIzq / vDer : 0;
            default: return 0;
        }
    }

    @Override void imprimir(String p, boolean esIz) {
        System.out.println(p + (esIz ? "├── " : "└── ") + op);
        izq.imprimir(p + (esIz ? "│   " : "    "), true);
        der.imprimir(p + (esIz ? "│   " : "    "), false);
    }
}