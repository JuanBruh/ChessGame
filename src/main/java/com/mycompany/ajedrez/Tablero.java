package com.mycompany.ajedrez;

import com.mycompany.ajedrez.piezas.Anfil;
import com.mycompany.ajedrez.piezas.Caballo;
import com.mycompany.ajedrez.piezas.Ficha;
import com.mycompany.ajedrez.piezas.Peon;
import com.mycompany.ajedrez.piezas.Reina;
import com.mycompany.ajedrez.piezas.Rey;
import com.mycompany.ajedrez.piezas.Torre;
import com.mycompany.ajedrez.piezas.estructuras.PilaString;
import com.mycompany.ajedrez.BotRival;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Tablero extends JPanel {
    
    public int titleSize = 85;
    
    int columnas = 8;
    int filas = 8;
    
    ArrayList<Ficha> fichasLista = new ArrayList();
    public PilaString historialMovimientos = new PilaString();
    
    public Ficha fichaSeleccionada;
    
    public boolean reyEnJaque = false;
    public Ficha reyEnJaqueFicha = null;
    public BotRival bot;
    
    Input input = new Input(this);
    CheckScanner scanner = new CheckScanner(this);
    
    public int enPassantTile = -1;
    private boolean blancoJuega = true; // true = blancas, false = negras

                    
    public Tablero() {
        this.setPreferredSize(new Dimension(columnas * titleSize, filas * titleSize));
        
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        
        agregarFicha();
        
        this.bot = new BotRival(this);
    }
    
    
    public Ficha getFicha(int columna, int fila) {
        for (Ficha ficha : fichasLista) {
            if (ficha.columna == columna && ficha.fila == fila) {
               return ficha; 
        }
       }        
       return null;
    }
    
    private String coordenadaANotacion(int columna, int fila) {
        char colChar = (char) ('a' + columna);  // 0 -> 'a', 1 -> 'b', etc.
        int filaNum = 8 - fila;  // 0 -> 8, 7 -> 1
        return "" + colChar + filaNum;
    }
    
    public void makeMove(Move move) {
        if (move.captura != null) {
            fichasLista.remove(move.captura);
        }

        // Si es peón en passant / promoción)
        if (move.ficha.nombre.equals("Peon")) {
            movePeon(move);
            return;
        }

        //revisar enroque
        if (move.ficha.nombre.equals("Rey")) {
            int dx = move.newColumna - move.oldColumna;

            if (Math.abs(dx) == 2) {
                int step = dx > 0 ? 1 : -1;
                int rookOldCol = dx > 0 ? 7 : 0;
                int rookNewCol = move.newColumna - step; // torre se coloca al lado del rey
                int rookRow = move.oldFila;

                // mover el rey
                move.ficha.columna = move.newColumna;
                move.ficha.fila = move.newFila;
                move.ficha.xPos = move.newColumna * titleSize;
                move.ficha.yPos = move.newFila * titleSize;
                move.ficha.isFirstMove = false;

                // mover la torre
                Ficha rook = getFicha(rookOldCol, rookRow);
                if (rook != null && rook.nombre.equals("Torre")) {
                    rook.columna = rookNewCol;
                    rook.fila = rookRow;
                    rook.xPos = rook.columna * titleSize;
                    rook.yPos = rook.fila * titleSize;
                    rook.isFirstMove = false;
                }
                String movimiento = (dx > 0) ? "O-O" : "O-O-O";  // Enroque corto (derecha) o largo (izquierda)
                historialMovimientos.IngresarNodo(movimiento);
                cambiarTurno();
                return;
            }
        }
        // Movimiento normal
        move.ficha.columna = move.newColumna;
        move.ficha.fila = move.newFila;
        move.ficha.xPos = move.newColumna * titleSize;
        move.ficha.yPos = move.newFila * titleSize;
        move.ficha.isFirstMove = false;

        // Verificar jaque mate antes de cambiar turno
        if (isCheckmate(!blancoJuega)) {  // !blancoJuega es el turno en jaque (el siguiente)
            if (blancoJuega) {
                System.out.println("blanco gana");
            } else {
                System.out.println("negro gana");
            }
            // Opcional: detener el juego (ej. deshabilitar inputs)
            historialMovimientos.ImprimirEnConsola();
            return;
        }
        
        // Registrar movimiento en historial
        String movimiento = coordenadaANotacion(move.oldColumna, move.oldFila) + "-" + coordenadaANotacion(move.newColumna, move.newFila);
        if (move.captura != null) {
            movimiento = coordenadaANotacion(move.oldColumna, move.oldFila) + "x" + coordenadaANotacion(move.newColumna, move.newFila);
        }
        historialMovimientos.IngresarNodo(movimiento);
        System.out.println("Movimiento registrado: " + movimiento);

        // Cambiar turno después de un movimiento válido
        cambiarTurno();
    }
    
    public Tablero copiar() {
        Tablero copia = new Tablero();  // Crea una nueva instancia (llama a agregarFicha, etc.)

        // Copiar campos básicos
        copia.titleSize = this.titleSize;
        copia.columnas = this.columnas;
        copia.filas = this.filas;
        copia.fichaSeleccionada = null;
        copia.reyEnJaque = this.reyEnJaque;
        copia.reyEnJaqueFicha = this.reyEnJaqueFicha;
        copia.enPassantTile = this.enPassantTile;
        copia.blancoJuega = this.blancoJuega;

        // Copiar lista de fichas
        copia.fichasLista = new ArrayList<>();
        for (Ficha ficha : this.fichasLista) {
            copia.fichasLista.add(ficha.clonar());
        }

        // Copiar historial (vacío para simulación)
        copia.historialMovimientos = new PilaString();

        // Crear scanner para la copia (necesario para validaciones)
        copia.scanner = new CheckScanner(copia);
        copia.input = null;  // No necesario para simulación
        copia.bot = null;    // No copiar bot

        return copia;
    }
    
    public boolean esTurnoBlancas() {
        return blancoJuega;
    }
    
    public void cambiarTurno() {
        blancoJuega = !blancoJuega;
        System.out.println("Cambiando turno a " + (blancoJuega ? "blancas" : "negras"));
        System.out.println("Bot es null? " + (bot == null));
        if (!blancoJuega && bot != null) {  // Si es turno de negras y hay bot
            System.out.println("Llamando al bot...");
            bot.hacerMovimiento();
        }
    }

        
    private void promotePeon(Move move) {
        fichasLista.add(new Reina(this, move.newColumna, move.newFila, move.ficha.isWhite));
        captura(move.ficha);
    }
    
    public void movePeon(Move move) {

        int direction = move.ficha.isWhite ? -1 : 1;

        // EN PASSANT
        if (getTileNum(move.newColumna, move.newFila) == enPassantTile) {
            move.captura = getFicha(move.newColumna, move.newFila - direction);
            fichasLista.remove(move.captura);
        }

        if (Math.abs(move.ficha.fila - move.newFila) == 2) {
            int midFila = move.ficha.fila + direction;
            enPassantTile = getTileNum(move.newColumna, midFila);
        } else {
            enPassantTile = -1;
        }

        // Promoción
        int promotFila = move.ficha.isWhite ? 0 : 7;
        if (move.newFila == promotFila) {
            promotePeon(move);
            return;
        }

        // Movimiento normal
        move.ficha.columna = move.newColumna;
        move.ficha.fila = move.newFila;
        move.ficha.xPos = move.newColumna * titleSize;
        move.ficha.yPos = move.newFila * titleSize;
        move.ficha.isFirstMove = false;

        // Verificar jaque mate antes de cambiar turno
        if (isCheckmate(!blancoJuega)) {
            if (blancoJuega) {
                System.out.println("blanco gana");
            } else {
                System.out.println("negro gana");
            }
            historialMovimientos.ImprimirEnConsola();
            return;
        }
        
        // Registrar movimiento en historial
        String movimiento = coordenadaANotacion(move.oldColumna, move.oldFila) + "-" + coordenadaANotacion(move.newColumna, move.newFila);
        if (move.captura != null) {
            movimiento = coordenadaANotacion(move.oldColumna, move.oldFila) + "x" + coordenadaANotacion(move.newColumna, move.newFila);
        }
        historialMovimientos.IngresarNodo(movimiento);
        System.out.println("Movimiento registrado: " + movimiento);

        // Cambiar turno después de un movimiento válido
        cambiarTurno();
    }


    
    public void captura(Ficha ficha) {
        fichasLista.remove(ficha);
    }
    
    public boolean isValidMove(Move move) {
        // Verificar que sea el turno de la pieza movida
        if (move.ficha.isWhite != blancoJuega) {
            return false;
        }

        // Movimiento ilegal según la pieza
        if (!move.ficha.isValidMovement(move.newColumna, move.newFila)) {
            return false;
        }

        // Ruta bloqueada
        if (move.ficha.moveChocaFicha(move.newColumna, move.newFila)) {
            return false;
        }

        //la casilla destino está ocupada por una ficha propia
        if (sameTeam(move.ficha, move.captura)) {
            return false;
        }

        // Si hay una pieza en el destino
        if (move.captura != null && move.captura == move.ficha) {
            return false;
        }
        
        if (scanner.isPinned(move.ficha)) {
            if (!scanner.moveMaintainsPin(move)) {
                return false;
            }
        }

        // no puede mover si deja al rey en jaque
        if (scanner.isReyCheck(move)) {
        reyEnJaque = true;
        reyEnJaqueFicha = findRey(move.ficha.isWhite);  
        return false; 
        }

        reyEnJaque = false;
        reyEnJaqueFicha = null;

        return true;
    }
    
    public boolean isValidMoveIgnoringTurn(Move move) {
        // Movimiento ilegal según la pieza
        if (!move.ficha.isValidMovement(move.newColumna, move.newFila)) {
            return false;
        }

        // Ruta bloqueada
        if (move.ficha.moveChocaFicha(move.newColumna, move.newFila)) {
            return false;
        }

        //la casilla destino está ocupada por una ficha propia
        if (sameTeam(move.ficha, move.captura)) {
            return false;
        }

        // Si hay una pieza en el destino
        if (move.captura != null && move.captura == move.ficha) {
            return false;
        }
        
        if (scanner.isPinned(move.ficha)) {
            if (!scanner.moveMaintainsPin(move)) {
                return false;
            }
        }

        // no puede mover si deja al rey en jaque
        if (scanner.isReyCheck(move)) {
        reyEnJaque = true;
        reyEnJaqueFicha = findRey(move.ficha.isWhite);  
        return false; 
        }

        reyEnJaque = false;
        reyEnJaqueFicha = null;

        return true;
    }
    
    public boolean sameTeam(Ficha f1, Ficha f2) {
        if (f1 == null || f2 == null) {
            return false;
        }
        return f1.isWhite == f2.isWhite;
    }
    
    public int getTileNum(int columna, int fila) {
        return fila * filas + columna;
    }
    
    Ficha findRey(boolean isWhite) {
        for (Ficha ficha : fichasLista) {
            if (isWhite == ficha.isWhite && ficha.nombre.equals("Rey")) {
                return ficha;
            }
        }
        return null;        
    }
    
    public CheckScanner getScanner() {
        return scanner;
    }

    public boolean isCheckmate(boolean isWhite) {
      ArrayList<Ficha> copiaFichas = new ArrayList<>(fichasLista);
      
      Ficha rey = findRey(isWhite);
      if (rey == null || !scanner.isPositionInCheck(isWhite, rey.columna, rey.fila)) {
          return false;  // No está en jaque, no es mate
      }
      
      // Verificar si hay movimientos legales
      for (Ficha ficha : copiaFichas) {
          if (ficha.isWhite == isWhite) {
              for (int c = 0; c < 8; c++) {
                  for (int f = 0; f < 8; f++) {
                      Move possibleMove = new Move(this, ficha, c, f);
                      if (isValidMoveIgnoringTurn(possibleMove)) {
                          return false;  // Hay al menos un movimiento legal, no es mate
                      }
                  }
              }
          }
      }
      
      System.out.println("Jaque mate para " + (isWhite ? "blanco" : "negro"));
      return true;
    }
  
        
    public void agregarFicha() {
        
        //FICHAS NEGRAS
        
        //Torres negras
        fichasLista.add(new Torre(this, 0, 0, false));
        fichasLista.add(new Torre(this, 7, 0, false));
        
        //caballos negros
        fichasLista.add(new Caballo(this, 1, 0, false));
        fichasLista.add(new Caballo(this, 6, 0, false));
        
        //Anfiles negros
        fichasLista.add(new Anfil(this, 2, 0, false));
        fichasLista.add(new Anfil(this, 5, 0, false));
        
        //REY negritillo
        fichasLista.add(new Rey(this, 3, 0, false));
        
        //Reina negra
        fichasLista.add(new Reina(this, 4, 0, false));
        
        //Peones negros
        fichasLista.add(new Peon(this, 0, 1, false));
        fichasLista.add(new Peon(this, 1, 1, false));
        fichasLista.add(new Peon(this, 2, 1, false));
        fichasLista.add(new Peon(this, 3, 1, false));
        fichasLista.add(new Peon(this, 4, 1, false));
        fichasLista.add(new Peon(this, 5, 1, false));
        fichasLista.add(new Peon(this, 6, 1, false));
        fichasLista.add(new Peon(this, 7, 1, false));
        
        //FICHAS BLANCAS
        
        //Torres blancas
        fichasLista.add(new Torre(this, 0, 7, true));
        fichasLista.add(new Torre(this, 7, 7, true));
        
        //caballos blancos
        fichasLista.add(new Caballo(this, 1, 7, true));
        fichasLista.add(new Caballo(this, 6, 7, true));
        
        //Anfiles blancos
        fichasLista.add(new Anfil(this, 2, 7, true));
        fichasLista.add(new Anfil(this, 5, 7, true));
        
        //Rey blanco
        fichasLista.add(new Rey(this, 3, 7, true));
        
        //Reina blanca falta la reina roja dah
        fichasLista.add(new Reina(this, 4, 7, true));
        
        //Peones blancos
        fichasLista.add(new Peon(this, 0, 6, true));
        fichasLista.add(new Peon(this, 1, 6, true));
        fichasLista.add(new Peon(this, 2, 6, true));
        fichasLista.add(new Peon(this, 3, 6, true));
        fichasLista.add(new Peon(this, 4, 6, true));
        fichasLista.add(new Peon(this, 5, 6, true));
        fichasLista.add(new Peon(this, 6, 6, true));
        fichasLista.add(new Peon(this, 7, 6, true));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        for (int f = 0; f < filas; f++)
            for (int c = 0; c < columnas; c++) {
                g2d.setColor((c + f) % 2 == 0 ? new Color(227, 198, 181) : new Color(157, 105, 57));
                g2d.fillRect(c * titleSize, f * titleSize, titleSize, titleSize);
            }
        
        
        if (fichaSeleccionada != null && fichaSeleccionada.isWhite == blancoJuega) {  // Solo resaltar si es turno de la ficha
            for (int f = 0; f < filas; f++)
                for (int c = 0; c < columnas; c++) {
                    
                    if (isValidMove(new Move(this, fichaSeleccionada, c, f))) {
                        
                        g2d.setColor(new Color(68, 180, 157, 190));
                        g2d.fillRect(c * titleSize, f * titleSize, titleSize, titleSize);
                    }
                    
                }
        }
        
        // Pintar el rey en jaque
        if (reyEnJaque && reyEnJaqueFicha != null) {
            g2d.setColor(new Color(255, 0, 0, 150));
            g2d.fillRect(
                reyEnJaqueFicha.columna * titleSize,
                reyEnJaqueFicha.fila * titleSize,
                titleSize,
                titleSize
            );
        }
        
        // Dibujar etiquetas de columnas (A-H) en la parte inferior
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        for (int c = 0; c < columnas; c++) {
            char label = (char) ('A' + c);  // A para columna 0, B para 1, etc.
            g2d.drawString(String.valueOf(label), c * titleSize + titleSize / 2 - 5, filas * titleSize + 20);
        }
        // Dibujar etiquetas de filas (8-1) en el lado izquierdo
        for (int f = 0; f < filas; f++) {
            int label = 8 - f;  // Fila 0 = 8, fila 7 = 1
            g2d.drawString(String.valueOf(label), -20, f * titleSize + titleSize / 2 + 5);
        }

        //Pinta las fichas
        for (Ficha ficha : fichasLista) {
            ficha.paint(g2d);
        }
    }
    
    //farafa
    //fecha 24/02/2026
}