package com.mycompany.ajedrez.piezas.estructuras;

import javax.swing.JOptionPane;

public class PilaString {
    
    private NodoString UltimoValorIngresado;
    int tamano = 0;
    String Lista = "";
    
    public PilaString() {
        UltimoValorIngresado = null;
        tamano = 0;
    }
    
    public boolean PilaVacia(){
        return UltimoValorIngresado == null;
    }
    
    public void IngresarNodo(String nodo){
        NodoString nuevo_nodo = new NodoString(nodo);
        nuevo_nodo.siguiente = UltimoValorIngresado;
        UltimoValorIngresado = nuevo_nodo;
        tamano++;
    }
    
    public String EliminarNodo() {
        String auxiliar = UltimoValorIngresado.informacion;
        UltimoValorIngresado = UltimoValorIngresado.siguiente;
        tamano--;
        return auxiliar;
    }
    
    public String MostrarUltimoValor() {
        return UltimoValorIngresado.informacion;
    }
    
    public int MostrarTamano() {
        return tamano;
    }
    
    public void VaciarPila() {
        while(!PilaVacia()) {
            EliminarNodo();
        }
    }
    
    public void MostrarContenido() {
        NodoString recorrido = UltimoValorIngresado;
        
        while (recorrido != null) {
            Lista += recorrido.informacion + "\n";
            recorrido = recorrido.siguiente;
        }
        JOptionPane.showMessageDialog(null, Lista);  // Cambié a showMessageDialog para evitar problemas
        Lista = "";
    }
    
    // Nuevo método: Imprimir en consola en lugar de JOptionPane
    public void ImprimirEnConsola() {
        NodoString recorrido = UltimoValorIngresado;
        System.out.println("Historial de movimientos:");
        while (recorrido != null) {
            System.out.println(recorrido.informacion);
            recorrido = recorrido.siguiente;
        }
    }
}

// Clase NodoString (adaptada de Nodo)
class NodoString {
    String informacion;
    NodoString siguiente;
    
    public NodoString(String valor) {
        informacion = valor;
        siguiente = null;
    }
}