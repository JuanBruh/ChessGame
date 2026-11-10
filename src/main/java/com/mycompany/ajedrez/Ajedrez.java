package com.mycompany.ajedrez;

import javax.swing.JFrame;
import java.awt.*;


public class Ajedrez {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(Color.black);
        frame.setMinimumSize(new Dimension(1000, 1000));
        frame.setLocationRelativeTo(null);
        
        Tablero tablero = new Tablero();
        frame.add(tablero);
        
        frame.setVisible(true);
    }
}
