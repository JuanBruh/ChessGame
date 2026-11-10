package com.mycompany.ajedrez;

import com.mycompany.ajedrez.piezas.*;
import java.util.ArrayList;

public class CheckScanner {

    private Tablero tablero;

    public CheckScanner(Tablero tablero) {
        this.tablero = tablero;
    }

    public boolean isReyCheck(Move move) {

        // --- 1. Simular el movimiento temporal ---
        Ficha ficha = move.ficha;
        Ficha capturada = tablero.getFicha(move.newColumna, move.newFila);

        int oldCol = ficha.columna;
        int oldFila = ficha.fila;

        // quitar la pieza capturada si existe (temporal)
        if (capturada != null) tablero.fichasLista.remove(capturada);

        // mover la pieza temporalmente
        ficha.columna = move.newColumna;
        ficha.fila = move.newFila;

        // --- 2. Encontrar al rey del color de la pieza movida ---
        Ficha rey = tablero.findRey(ficha.isWhite);
        int rCol = rey.columna;
        int rFila = rey.fila;

        // --- 3. Revisar amenazas ---
        boolean check = isPositionInCheck(rey.isWhite, rCol, rFila);

        // --- 4. Revertir movimiento temporal ---
        ficha.columna = oldCol;
        ficha.fila = oldFila;

        if (capturada != null) tablero.fichasLista.add(capturada);

        return check;
    }


    public boolean isPositionInCheck(boolean isWhiteKing, int reyCol, int reyFila) {

        for (Ficha f : tablero.fichasLista) {

            if (f.isWhite == isWhiteKing) continue; // solo remos enemigos

            if (f.isValidMovement(reyCol, reyFila)) {

                // Además necesita no chocar piezas
                if (!f.moveChocaFicha(reyCol, reyFila)) {
                    return true; // el rey está amenazado
                }
            }
        }

        return false;
    }
    
    public boolean isPinned(Ficha ficha) {

        Ficha rey = tablero.findRey(ficha.isWhite);
       
        // Si la ficha es el rey, no puede estar clavada
        if (ficha == rey) {
            return false;
        }

        // Dirección del rey hacia la ficha
        int dirCol = Integer.compare(ficha.columna, rey.columna);
        int dirFila = Integer.compare(ficha.fila, rey.fila);

        // Debe estar alineado en línea recta o diagonal
        boolean alineado = (dirCol == 0 || dirFila == 0 || Math.abs(dirCol) == Math.abs(dirFila));
        if (!alineado) return false;

        // Buscar piezas entre rey y ficha
        int c = rey.columna + dirCol;
        int f = rey.fila + dirFila;

        boolean fichaEncontrada = false;

        while (c >= 0 && c < 8 && f >= 0 && f < 8) {

            Ficha objetivo = tablero.getFicha(c, f);

            if (objetivo != null) {
                if (objetivo == ficha) {
                    fichaEncontrada = true; // encontramos la pieza sospechosa
                } else if (fichaEncontrada) {
                    // Si después de la pieza encontramos una enemiga que ataca en esa línea → CLAVADA
                    if (objetivo.isWhite != ficha.isWhite && piezaAtacaEnLinea(objetivo, rey.columna, rey.fila)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            c += dirCol;
            f += dirFila;
        }

        return false;
    }
    
    private boolean piezaAtacaEnLinea(Ficha atacante, int columna, int fila) {

        switch (atacante.nombre) {
            case "Torre":
                return atacante.columna == columna || atacante.fila == fila;

            case "Anfil":
                return Math.abs(atacante.columna - columna) == Math.abs(atacante.fila - fila);

            case "Reina":
                boolean diagonal = Math.abs(atacante.columna - columna) == Math.abs(atacante.fila - fila);
                boolean recta = atacante.columna == columna || atacante.fila == fila;
                return diagonal || recta;

            default:
                return false;
        }
    }
    
    public boolean moveMaintainsPin(Move move) {
        Ficha ficha = move.ficha;

        // mover temporalmente
        int oldC = ficha.columna;
        int oldF = ficha.fila;
        ficha.columna = move.newColumna;
        ficha.fila = move.newFila;

        // ¿queda el rey en jaque? → movimiento ilegal
        boolean dejaJaque = isReyCheck(move);

        // revertir
        ficha.columna = oldC;
        ficha.fila = oldF;

        return !dejaJaque;
    }
}