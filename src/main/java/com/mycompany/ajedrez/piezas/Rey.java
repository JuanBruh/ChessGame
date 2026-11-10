package com.mycompany.ajedrez.piezas;

import com.mycompany.ajedrez.Tablero;
import java.awt.image.BufferedImage;

public class Rey extends Ficha {

    public Rey(Tablero tablero, int columna, int fila, boolean isWhite) {
        super(tablero);
        this.columna = columna;
        this.fila = fila;
        this.xPos = columna * tablero.titleSize;
        this.yPos = fila * tablero.titleSize;

        this.isWhite = isWhite;
        this.nombre = "Rey";

        this.sprite = sheet.getSubimage(0 * sheetScale, 
                isWhite ? 0 : sheetScale, 
                sheetScale, sheetScale)
            .getScaledInstance(tablero.titleSize, tablero.titleSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int columna, int fila) {
        int dx = Math.abs(columna - this.columna);
        int dy = Math.abs(fila - this.fila);

        // Movimiento normal del rey: máximo 1 casilla
        if (dx <= 1 && dy <= 1) {
            return true;
        }

        // Verificar enroque: rey se mueve 2 casillas horizontalmente en su fila inicial
        if (dy == 0 && dx == 2 && fila == this.fila && isFirstMove) {
            return isCastlingValid(columna);
        }

        return false;
    }

    // Método auxiliar para validar el enroque
    private boolean isCastlingValid(int newColumna) {
        // Determinar dirección del enroque (izquierda o derecha)
        int direction = (newColumna > this.columna) ? 1 : -1;
        int rookCol = (direction == 1) ? 7 : 0;  // Columna de la torre (7 para enroque corto, 0 para largo)

        // Verificar que la torre esté en posición y no se haya movido
        Ficha rook = tablero.getFicha(rookCol, this.fila);
        if (rook == null || !rook.nombre.equals("Torre") || !rook.isFirstMove) {
            return false;
        }

        // Verificar que las casillas intermedias estén libres
        int startCol = Math.min(this.columna, rookCol) + 1;
        int endCol = Math.max(this.columna, rookCol) - 1;
        for (int col = startCol; col <= endCol; col++) {
            if (tablero.getFicha(col, this.fila) != null) {
                return false;
            }
        }

        // Verificar que el rey no esté en jaque actualmente
        if (tablero.getScanner().isPositionInCheck(this.isWhite, this.columna, this.fila)) {
            return false;
        }

        // Verificar que el rey no pase por casillas en jaque
        int kingPathCol = this.columna + direction;
        if (tablero.getScanner().isPositionInCheck(this.isWhite, kingPathCol, this.fila)) {
            return false;
        }

        return true;
    }
}
