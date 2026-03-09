import java.util.Map;

abstract class NodoAST {
    abstract void imprimir(String prefijo, boolean esIzquierdo);
    abstract double evaluar(Map<String, Double> memoria); // <--- NUEVO MÉTODO
}