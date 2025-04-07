package juegocartas;

import java.util.*;

public class JuegoDeCartas {
    static List<Carta> mano = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        inicializarCartas();

        int opcion;
        do {
            System.out.println("\n===== MENÚ =====");
            System.out.println("1. Mostrar cartas en mano");
            System.out.println("2. Grupos con el mismo valor");
            System.out.println("3. Calcular puntaje de cartas restantes");
            System.out.println("4. Grupos en escalera sin importar el palo");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    mostrarCartas();
                    break;
                case 2:
                    agruparPorValor();
                    break;
                case 3:
                    calcularPuntajeRestante();
                    break;
                case 4:
                    buscarEscaleras();
                    break;
            }

        } while (opcion != 0);
    }

    private static void inicializarCartas() {
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] palos = {"Corazones", "Diamantes", "Tréboles", "Picas"};

        Set<String> cartasGeneradas = new HashSet<>();
        Random random = new Random();

        while (mano.size() < 10) {
            String valor = valores[random.nextInt(valores.length)];
            String palo = palos[random.nextInt(palos.length)];
            String idCarta = valor + "-" + palo;

            if (!cartasGeneradas.contains(idCarta)) {
                cartasGeneradas.add(idCarta);
                mano.add(new Carta(valor, palo));
            }
        }
    }

    private static void mostrarCartas() {
        System.out.println("Cartas en tu mano:");
        for (Carta carta : mano) {
            System.out.println("- " + carta);
        }
    }

    private static void agruparPorValor() {
        Map<String, List<Carta>> grupos = new HashMap<>();
        for (Carta carta : mano) {
            grupos.computeIfAbsent(carta.valor, k -> new ArrayList<>()).add(carta);
        }

        System.out.println("Grupos con el mismo valor:");
        for (Map.Entry<String, List<Carta>> entry : grupos.entrySet()) {
            if (entry.getValue().size() >= 2) {
                System.out.print("- " + entry.getKey() + ": ");
                System.out.println(entry.getValue());
            }
        }
    }

    private static void buscarEscaleras() {
        // Convertir las cartas en valores numéricos
        Map<Integer, List<Carta>> porValor = new TreeMap<>();
        for (Carta carta : mano) {
            int valor = carta.obtenerValorNumerico();
            porValor.computeIfAbsent(valor, k -> new ArrayList<>()).add(carta);
        }

        List<List<Carta>> escaleras = new ArrayList<>();
        List<Carta> actual = new ArrayList<>();
        int anterior = -2;

        for (int valor : porValor.keySet()) {
            if (valor == anterior + 1) {
                actual.add(porValor.get(valor).get(0));
            } else {
                if (actual.size() >= 3) {
                    escaleras.add(new ArrayList<>(actual));
                }
                actual.clear();
                actual.add(porValor.get(valor).get(0));
            }
            anterior = valor;
        }

        if (actual.size() >= 3) {
            escaleras.add(actual);
        }

        System.out.println("Grupos en escalera sin importar el palo:");
        if (escaleras.isEmpty()) {
            System.out.println("No hay escaleras.");
        } else {
            for (List<Carta> grupo : escaleras) {
                System.out.println("- " + grupo);
            }
        }
    }

    private static void calcularPuntajeRestante() {
        Map<String, List<Carta>> grupos = new HashMap<>();
        for (Carta carta : mano) {
            grupos.computeIfAbsent(carta.valor, k -> new ArrayList<>()).add(carta);
        }

        Set<Carta> enGrupos = new HashSet<>();
        for (List<Carta> grupo : grupos.values()) {
            if (grupo.size() >= 2) {
                enGrupos.addAll(grupo);
            }
        }

        // Agregar también las escaleras encontradas
        Map<Integer, List<Carta>> porValor = new TreeMap<>();
        for (Carta carta : mano) {
            int valor = carta.obtenerValorNumerico();
            porValor.computeIfAbsent(valor, k -> new ArrayList<>()).add(carta);
        }

        List<Carta> actual = new ArrayList<>();
        int anterior = -2;

        for (int valor : porValor.keySet()) {
            if (valor == anterior + 1) {
                actual.add(porValor.get(valor).get(0));
            } else {
                if (actual.size() >= 3) {
                    enGrupos.addAll(actual);
                }
                actual.clear();
                actual.add(porValor.get(valor).get(0));
            }
            anterior = valor;
        }

        if (actual.size() >= 3) {
            enGrupos.addAll(actual);
        }

        int puntaje = 0;
        for (Carta carta : mano) {
            if (!enGrupos.contains(carta)) {
                puntaje += carta.obtenerPuntaje();
            }
        }

        System.out.println("Puntaje total de cartas no agrupadas: " + puntaje);
    }
}