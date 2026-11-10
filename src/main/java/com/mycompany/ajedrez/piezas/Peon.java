package com.mycompany.ajedrez.piezas;

import com.mycompany.ajedrez.Tablero;
import java.awt.image.BufferedImage;


public class Peon extends Ficha{
    public Peon(Tablero tablero, int columna, int fila, boolean isWhite) {
        super(tablero);
        this.columna = columna;
        this.fila = fila;
        this.xPos = columna * tablero.titleSize;
        this.yPos = fila * tablero.titleSize;
        
        this.isWhite = isWhite;
        this.nombre = "Peon";
        
        this.sprite = sheet.getSubimage(5 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(tablero.titleSize, tablero.titleSize, BufferedImage.SCALE_SMOOTH);
    }
    
    public boolean isValidMovement(int columna, int fila) {
        int direction = isWhite ? -1 : 1;

        // Movimiento simple
        if (columna == this.columna && fila == this.fila + direction) {
            if (tablero.getFicha(columna, fila) == null) {
                return true;
            }
        }

        // Movimiento doble
        if (isFirstMove && columna == this.columna && fila == this.fila + (2 * direction)) {
            if (tablero.getFicha(columna, this.fila + direction) == null &&
                tablero.getFicha(columna, fila) == null) {
                return true;
            }
        }

        // Captura diagonal izquierda
        if (columna == this.columna - 1 && fila == this.fila + direction) {
            Ficha objetivo = tablero.getFicha(columna, fila);
            if (objetivo != null && objetivo.isWhite != this.isWhite) {
                return true;
            }
        }

        // Captura diagonal derecha
        if (columna == this.columna + 1 && fila == this.fila + direction) {
            Ficha objetivo = tablero.getFicha(columna, fila);
            if (objetivo != null && objetivo.isWhite != this.isWhite) {
                return true;
            }
        }

        // En passant izquierda
        if (tablero.getTileNum(columna, fila) == tablero.enPassantTile && columna == this.columna - 1 && fila == this.fila + direction) {
            Ficha target = tablero.getFicha(columna, fila - direction);  // Ficha enemiga en su fila original
            if (target != null && target.isWhite != this.isWhite && target.nombre.equals("Peon")) {
                return true;
            }
        }

        // En passant derecha
        if (tablero.getTileNum(columna, fila) == tablero.enPassantTile && columna == this.columna + 1 && fila == this.fila + direction) {
            Ficha target = tablero.getFicha(columna, fila - direction);  // Ficha enemiga en su fila original
            if (target != null && target.isWhite != this.isWhite && target.nombre.equals("Peon")) {
                return true;
            }
        }

        return false;
    }
}