/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import enums.GameStateEnum;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javax.swing.JFrame;

/**
 *
 * @author Tomasz
 */
public class PRClient {
    public static final int DIMENSIONX = 1280;
    public static final int DIMENSIONY = 720;
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        Dimension dimension = new Dimension(DIMENSIONX, DIMENSIONY);
        frame.setBounds(new Rectangle(dimension));
        frame.setTitle("KanterStrajk");
        frame.setResizable(false);
        frame.setFocusable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Game game = new Game(dimension);
        new Thread(game).start();
        frame.getContentPane().add(game);
        
     
    }
    

    
}
