package com.mycompany.ajedrez;

import com.mycompany.ajedrez.piezas.Ficha;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Input extends MouseAdapter {
    
    Tablero tablero;
    
    public Input(Tablero tablero) {
        this.tablero = tablero;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
        int columna = e.getX() / tablero.titleSize;
        int fila = e.getY() / tablero.titleSize;
        
        Ficha fichaXY = tablero.getFicha(columna, fila);
        if (fichaXY != null && fichaXY.isWhite == tablero.esTurnoBlancas()) {  // Solo seleccionar si es turno de esa ficha
            tablero.fichaSeleccionada = fichaXY;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (tablero.fichaSeleccionada != null) {

            int columna = e.getX() / tablero.titleSize;
            int fila = e.getY() / tablero.titleSize;

            Move move = new Move(tablero, tablero.fichaSeleccionada, columna, fila);

            if (tablero.isValidMove(move)) {
                tablero.makeMove(move);
            } else {
                tablero.fichaSeleccionada.xPos = tablero.fichaSeleccionada.columna * tablero.titleSize;
                tablero.fichaSeleccionada.yPos = tablero.fichaSeleccionada.fila * tablero.titleSize;
            }

            tablero.fichaSeleccionada = null;
        }

        tablero.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if (tablero.fichaSeleccionada != null) {

            // Actualiza posición visual
            tablero.fichaSeleccionada.xPos = e.getX() - tablero.titleSize / 2;
            tablero.fichaSeleccionada.yPos = e.getY() - tablero.titleSize / 2;
        }

        tablero.repaint();
    }
}