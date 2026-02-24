
package com.mycompany.ajedrez.piezas;

import com.mycompany.ajedrez.Tablero;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;



public class Ficha {
    
    public int columna, fila;
    public int xPos, yPos;
    
    public boolean isWhite;
    public String nombre;
    public int valor;
    
    public boolean isFirstMove = true;
    
    
    BufferedImage sheet;
    {
        try {
            sheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("piezas.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    protected int sheetScale = sheet.getWidth()/6;
    
    Image sprite;
    
    Tablero tablero;
    
    public Ficha(Tablero tablero) {
        this.tablero = tablero;
    }
    
    public boolean isValidMovement(int columna, int fila) {return true;}
    public boolean moveChocaFicha(int columna, int fila) {return false;}
    
    public Ficha clonar() {
        Ficha copia = null;
        switch (this.nombre) {
            case "Rey":
                copia = new Rey(this.tablero, this.columna, this.fila, this.isWhite);
                break;
            case "Reina":
                copia = new Reina(this.tablero, this.columna, this.fila, this.isWhite);
                break;
            case "Torre":
                copia = new Torre(this.tablero, this.columna, this.fila, this.isWhite);
                break;
            case "Anfil":
                copia = new Anfil(this.tablero, this.columna, this.fila, this.isWhite);
                break;
            case "Caballo":
                copia = new Caballo(this.tablero, this.columna, this.fila, this.isWhite);
                break;
            case "Peon":
                copia = new Peon(this.tablero, this.columna, this.fila, this.isWhite);
                break;
            default:
                throw new IllegalArgumentException("Tipo de ficha desconocido: " + this.nombre);
        }

        // Copiar campos adicionales si no se hacen en el constructor
        copia.isFirstMove = this.isFirstMove;
        copia.xPos = this.xPos;
        copia.yPos = this.yPos;
        // Copia sprite si es necesario, pero como es BufferedImage, podrías omitirlo para simulación
        copia.sprite = this.sprite;

        return copia;
    }

    public void paint(Graphics2D g2d) {
        g2d.drawImage(sprite, xPos, yPos, null);
    }
 
}
