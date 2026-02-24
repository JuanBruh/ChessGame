
package com.mycompany.ajedrez;

import com.mycompany.ajedrez.piezas.Ficha;

public class Move {
    int oldColumna;
    int oldFila;
    int newColumna;
    int newFila;
    
    Ficha ficha;
    Ficha captura;
    
    public Move(Tablero tablero, Ficha ficha, int newColumna, int newFila) {
        this.oldColumna = ficha.columna;
        this.oldFila = ficha.fila;
        this.newColumna = newColumna;
        this.newFila = newFila;
        
        this.ficha = ficha;
        this.captura = tablero.getFicha(newColumna, newFila);
    }
    
}
