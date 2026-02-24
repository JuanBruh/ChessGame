package com.mycompany.ajedrez.piezas;

import com.mycompany.ajedrez.Tablero;
import java.awt.image.BufferedImage;


public class Caballo extends Ficha{
    public Caballo(Tablero tablero, int columna, int fila, boolean isWhite) {
        super(tablero);
        this.columna = columna;
        this.fila = fila;
        this.xPos = columna * tablero.titleSize;
        this.yPos = fila * tablero.titleSize;
        
        this.isWhite = isWhite;
        this.nombre = "Caballo";
        
        this.sprite = sheet.getSubimage(3 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(tablero.titleSize, tablero.titleSize, BufferedImage.SCALE_SMOOTH);
    }
    
    public boolean isValidMovement(int columna, int fila) {
        return Math.abs(columna - this.columna) * Math.abs(fila - this.fila) == 2;
    }
}
