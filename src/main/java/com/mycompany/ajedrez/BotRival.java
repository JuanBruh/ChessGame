package com.mycompany.ajedrez;

import com.mycompany.ajedrez.Tablero;
import com.mycompany.ajedrez.Move;
import com.mycompany.ajedrez.piezas.Ficha;
import com.mycompany.ajedrez.piezas.estructuras.NodoArbol;
import java.util.ArrayList;

public class BotRival {
    private Tablero tablero;  // Referencia al tablero principal
    private boolean esBotNegras;

    public BotRival(Tablero tablero) {
        this.tablero = tablero;
        this.esBotNegras = true;
    }

    public void hacerMovimiento() {
        System.out.println("esTurnoBlancas: " + tablero.esTurnoBlancas() + ", esBotNegras: " + esBotNegras);
        if (!tablero.esTurnoBlancas() == esBotNegras) return;

        System.out.println("Bot intentando mover");

        NodoArbol raiz = generarArbol(tablero, 2);
        NodoArbol mejorHijo = minimax(raiz, true);
        if (mejorHijo != null) {
            Move movimientoElegido = encontrarMovimiento(raiz.estadoTablero, mejorHijo.estadoTablero);
            if (movimientoElegido != null) {
                tablero.makeMove(movimientoElegido);
                tablero.repaint();
            }
        }
    }

    // Generar árbol binario: cada nodo tiene hasta 2 hijos (movimientos posibles)
    private NodoArbol generarArbol(Tablero estadoActual, int profundidad) {
        NodoArbol nodo = new NodoArbol(estadoActual);
        if (profundidad == 0) {
            nodo.valor = evaluarEstado(estadoActual);  // Evaluar hoja
            return nodo;
        }

        // Generar movimientos posibles para el turno actual
        ArrayList<Move> movimientosPosibles = obtenerMovimientosPosibles(estadoActual);
        if (movimientosPosibles.size() >= 1) {
            // Hijo izquierdo: primer movimiento
            Tablero estadoIzq = simularMovimiento(estadoActual, movimientosPosibles.get(0));
            nodo.izquierdo = generarArbol(estadoIzq, profundidad - 1);
        }
        if (movimientosPosibles.size() >= 2) {
            // Hijo derecho: segundo movimiento
            Tablero estadoDer = simularMovimiento(estadoActual, movimientosPosibles.get(1));
            nodo.derecho = generarArbol(estadoDer, profundidad - 1);
        }

        return nodo;
    }

    private NodoArbol minimax(NodoArbol nodo, boolean maximizar) {
        if (nodo.izquierdo == null && nodo.derecho == null) {
            return nodo;
        }

        NodoArbol mejorHijo = null;
        int mejorValor = maximizar ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        if (nodo.izquierdo != null) {
            NodoArbol candidato = minimax(nodo.izquierdo, !maximizar);
            if ((maximizar && candidato.valor > mejorValor) || (!maximizar && candidato.valor < mejorValor)) {
                mejorValor = candidato.valor;
                mejorHijo = nodo.izquierdo;
            }
        }
        if (nodo.derecho != null) {
            NodoArbol candidato = minimax(nodo.derecho, !maximizar);
            if ((maximizar && candidato.valor > mejorValor) || (!maximizar && candidato.valor < mejorValor)) {
                mejorValor = candidato.valor;
                mejorHijo = nodo.derecho;
            }
        }

        return mejorHijo;
    }

    // Evaluar el estado del tablero
    private int evaluarEstado(Tablero tablero) {
        int score = 0;
        for (Ficha f : tablero.fichasLista) {
            int valor = switch (f.nombre) {
                case "Peon" -> 1;
                case "Caballo", "Anfil" -> 3;
                case "Torre" -> 5;
                case "Reina" -> 9;
                default -> 0;
            };
            score += f.isWhite ? -valor : valor;  // Positivo para bot (negras), negativo para blancas
        }
        return score;
    }

    // Obtener lista de movimientos válidos
    private ArrayList<Move> obtenerMovimientosPosibles(Tablero tablero) {
        ArrayList<Move> movimientos = new ArrayList<>();
        for (Ficha f : tablero.fichasLista) {
            if (f.isWhite == tablero.esTurnoBlancas()) {
                for (int c = 0; c < 8; c++) {
                    for (int f2 = 0; f2 < 8; f2++) {
                        Move m = new Move(tablero, f, c, f2);
                        if (tablero.isValidMove(m)) {
                            movimientos.add(m);
                            System.out.println("Movimientos encontrados" + movimientos.size());
                        }
                    }
                }
            }
        }
        return movimientos;    
    }


    private Tablero simularMovimiento(Tablero original, Move move) {
        Tablero copia = original.copiar();  // Asume que tienes un método copiar() en Tablero
        copia.makeMove(move);
        return copia;
    }

    // Encontrar el movimiento que lleva de un estado a otro (comparando diferencias)
    private Move encontrarMovimiento(Tablero antes, Tablero despues) {
        // Lógica simple: compara posiciones de piezas (puedes mejorar esto)
        for (Ficha fAntes : antes.fichasLista) {
            for (Ficha fDespues : despues.fichasLista) {
                if (fAntes.nombre.equals(fDespues.nombre) && fAntes.isWhite == fDespues.isWhite &&
                    (fAntes.columna != fDespues.columna || fAntes.fila != fDespues.fila)) {
                    return new Move(antes, fAntes, fDespues.columna, fDespues.fila);
                }
            }
        }
        return null;
    }
}