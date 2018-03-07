/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import elements.Player;
import enums.GameStateEnum;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz
 */
public class Connecting implements Runnable {
    private String ip;
    private int port;
    private Game game;
    private UUID id;

    public Connecting(String ip, int port, Game game, UUID id) {
        this.ip = ip;
        this.port = port;
        this.game = game;
        this.id = id;
        
    }
    
    @Override
    public void run() {
        try (Socket socket = new Socket(ip, port)) {
            try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
                out.flush();
                try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                    //System.out.println(game.getPlayer());
                    out.writeObject(game.getPlayer());
                    out.flush();
                    game.setModel((Model)in.readObject());
                    //główna pętla komunikacji z serwerem
                    long previousTimestamp = System.currentTimeMillis();
                    long start;
                    long timeOfWork = 16;
                    int index = 0;
                    while (index>=0 && game.getGameState() == GameStateEnum.PLAY) {
                        start = System.currentTimeMillis();
                        //long newTimestamp = System.currentTimeMillis();
                        //if (previousTimestamp + 20 < newTimestamp) {
                            try {
                                game.playerUpdate();
                                Player player = game.getPlayer();
                                out.writeUnshared(player);
                                out.flush();
                                out.reset();
                                player = null;
                                Model model = (Model) in.readUnshared();
                                game.setModel(model);
                                index = model.getPlayerIndexById(id);
                                if (index>=0) {
                                    Player playerServ = model.getPlayers().get(index);
                                    game.setPlayer(playerServ);
                                    if (model.getPlayers().size() == 1 && model.getMaxPlayers()>1) {
                                        game.setGameState(GameStateEnum.WIN);
                                    }
                                } else {
                                    game.setGameState(GameStateEnum.LOSE);
                                }
                            } catch (Exception e) {
                                System.out.println("Failed update from server");
                            }
                        if (timeOfWork > (System.currentTimeMillis()-start)) try {
                            sleep(timeOfWork - (System.currentTimeMillis()-start) );
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Connecting.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            } catch (SocketException e) {
            } catch (IOException e) {
                //System.out.println("Object stream initilization failure");
                //System.out.println(e.toString());
            } catch (ClassNotFoundException e2){
                //ignore
            }
        } catch (IOException e) {
            game.setGameState(GameStateEnum.MENU);
            System.out.println("Failed to create socket " + ip + ":" + port);
            System.out.println(e.toString());
        }
    }
    
}
