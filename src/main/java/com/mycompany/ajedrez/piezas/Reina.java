package com.mycompany.ajedrez.piezas;

import com.mycompany.ajedrez.Tablero;
import java.awt.image.BufferedImage;


public class Reina extends Ficha{
    public Reina(Tablero tablero, int columna, int fila, boolean isWhite) {
        super(tablero);
        this.columna = columna;
        this.fila = fila;
        this.xPos = columna * tablero.titleSize;
        this.yPos = fila * tablero.titleSize;
        
        this.isWhite = isWhite;
        this.nombre = "Reina";
        
        this.sprite = sheet.getSubimage(1 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(tablero.titleSize, tablero.titleSize, BufferedImage.SCALE_SMOOTH);
    }
    
    public boolean isValidMovement(int columna, int fila) {
        return this.columna == columna || this.fila == fila || Math.abs(this.columna - columna) == Math.abs(this.fila - fila);
    }
    
    public boolean moveChocaFicha(int columna, int fila) {

    // Movimiento horizontal
    if (this.fila == fila || this.columna == columna) {

        // Mover a la izquierda
        if (columna < this.columna) {
            for (int c = this.columna - 1; c > columna; c--) {
                if (tablero.getFicha(c, fila) != null)
                    return true;
            }
        }

        // Mover a la derecha
        if (columna > this.columna) {
            for (int c = this.columna + 1; c < columna; c++) {
                if (tablero.getFicha(c, fila) != null)
                    return true;
            }
        }

        // Mover arriba
        if (fila < this.fila) {
            for (int f = this.fila - 1; f > fila; f--) {
                if (tablero.getFicha(columna, f) != null)
                    return true;
            }
        }

        // Mover abajo
        if (fila > this.fila) {
            for (int f = this.fila + 1; f < fila; f++) {
                if (tablero.getFicha(columna, f) != null)
                    return true;
            }
        }
    } else {
        //Arriba izquierda
        if(this.columna > columna && this.fila > fila)
            for (int i = 1; i < Math.abs(this.columna - columna); i++)
                if (tablero.getFicha(this.columna - i, this.fila - i) != null)
                    return true;
        
        //Arriba derecha
        if(this.columna < columna && this.fila > fila)
            for (int i = 1; i < Math.abs(this.columna - columna); i++)
                if (tablero.getFicha(this.columna + i, this.fila - i) != null)
                    return true;
        
        //Abajo izquierda
        if(this.columna > columna && this.fila < fila)
            for (int i = 1; i < Math.abs(this.columna - columna); i++)
                if (tablero.getFicha(this.columna - i, this.fila + i) != null)
                    return true;
        
        //Abajo derecha
        if(this.columna < columna && this.fila < fila)
            for (int i = 1; i < Math.abs(this.columna - columna); i++)
                if (tablero.getFicha(this.columna + i, this.fila + i) != null)
                    return true;
    }

    return false;
    }
}
