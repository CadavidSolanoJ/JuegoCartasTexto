package juegocartas;

public class Carta {
    String valor;
    String palo;

    public Carta(String valor, String palo) {
        this.valor = valor;
        this.palo = palo;
    }

    public int obtenerValorNumerico() {
        switch (valor) {
            case "A": case "J": case "Q": case "K": return 10;
            default: return Integer.parseInt(valor);
        }
    }

    public int obtenerPuntaje() {
        return obtenerValorNumerico();
    }

    @Override
    public String toString() {
        return valor + " de " + palo;
    }
}
