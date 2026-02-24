package com.mycompany.ajedrez.piezas.estructuras;

import com.mycompany.ajedrez.Tablero;

public class NodoArbol {
    public Tablero estadoTablero;  // Estado del tablero en este nodo
    public NodoArbol izquierdo;    // Hijo izquierdo (primer movimiento posible)
    public NodoArbol derecho;      // Hijo derecho (segundo movimiento posible, o null si no hay)
    public int valor;              // Valor de evaluación

    public NodoArbol(Tablero tablero) {
        this.estadoTablero = tablero;
        this.izquierdo = null;
        this.derecho = null;
        this.valor = 0; 
    }
}