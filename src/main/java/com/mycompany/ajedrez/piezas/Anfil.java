package com.mycompany.ajedrez.piezas;

import com.mycompany.ajedrez.Tablero;
import java.awt.image.BufferedImage;


public class Anfil extends Ficha{
    public Anfil(Tablero tablero, int columna, int fila, boolean isWhite) {
        super(tablero);
        this.columna = columna;
        this.fila = fila;
        this.xPos = columna * tablero.titleSize;
        this.yPos = fila * tablero.titleSize;
        
        this.isWhite = isWhite;
        this.nombre = "Anfil";
        
        this.sprite = sheet.getSubimage(2 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(tablero.titleSize, tablero.titleSize, BufferedImage.SCALE_SMOOTH);
    }
    
    public boolean isValidMovement(int columna, int fila) {
        return Math.abs(this.columna - columna) == Math.abs(this.fila - fila); 
    }
    
    public boolean moveChocaFicha(int columna, int fila) {
        
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

        return false;
    }
}
