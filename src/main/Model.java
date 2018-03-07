/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import elements.Projectile;
import elements.Element;
import elements.PowerUp;
import elements.Player;
import elements.Block;
import java.awt.List;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.UUID;
import java.util.logging.Logger;


/**
 *
 * @author Tomasz
 */
public class Model implements Serializable{
    private CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<Player>();
    private CopyOnWriteArrayList<Projectile> projectiles = new CopyOnWriteArrayList<Projectile>();
    private CopyOnWriteArrayList<PowerUp> powerUps = new CopyOnWriteArrayList<PowerUp>();
    private CopyOnWriteArrayList<Block> blocks = new CopyOnWriteArrayList<Block>(); //****************************
    private int maxPlayers = 0;

    
    public synchronized CopyOnWriteArrayList<Player> getPlayers() {
        return players;
    }
    
    public synchronized CopyOnWriteArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
        
    public synchronized CopyOnWriteArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }
    
    public synchronized CopyOnWriteArrayList<Block> getBlocks(){ //*******************************
        return blocks;
    }
    
    public synchronized void incrementMaxPlayers(){
        maxPlayers++;
    }
    
    public synchronized void setMaxPlayers(int i) {
        maxPlayers = i;
    }
    
    public synchronized int getMaxPlayers() {
        return maxPlayers;
    }
    
    //Metody dla serwera
    public synchronized void add(Player p) {
        players.add(p);
    }
    
    public synchronized void remove(Player p) {
        int index = this.getPlayerIndexById(p.getId());
        if (index>=0) players.remove(index);
    }
    
    public synchronized void add(PowerUp b) {
        powerUps.add(b);
    }
    public synchronized void add(Block b) { //**************************************
        blocks.add(b);
    }
    public synchronized void collisions() {        
       for (Player player : players) {
            CopyOnWriteArrayList<Element> defenders = searchCollisions(player);
            for (Element defender : defenders) {
                if (defender != null)
                    player.attack(defender);
            }
        }
   
        for (Projectile projectile : projectiles) {
            CopyOnWriteArrayList<Element> defenders = searchCollisions(projectile);
            for (Element defender : defenders) {
                if (defender != null)
                    projectile.attack(defender);
            }
        }
 

    }
    
    public synchronized CopyOnWriteArrayList<Element> searchCollisions(Element element) {
        CopyOnWriteArrayList<Element> defenders = new CopyOnWriteArrayList<Element>();
        for (Projectile p : getProjectiles()) {
            if (!element.equals(p) && element.colision(p)) defenders.add(p);
        }
        for (Player p : players) {
            if (!element.equals(p) && element.colision(p)) defenders.add(p);
        }
        for (PowerUp p : powerUps) {
            if (!element.equals(p) && element.colision(p)) defenders.add(p);
        }
        
         for (Block p : blocks) {
            if (!element.equals(p) && element.colision(p)) defenders.add(p);//****************************
        }
        
         return defenders;
    }
    
    public synchronized int getPlayerIndexById(UUID id) {
        int size = players.size();
        for (int i = 0; i < size; i++) {
            if (players.get(i).getId().compareTo(id) == 0) return i;
        }       
        return -1;
    }
    
    
    public synchronized void update() {
        int size;

        size = getPlayers().size();
        for (int i = 0; i < size; i++) {
            Player player = getPlayers().get(i);
            if (player.getHealth() <= 0) {
                getPlayers().remove(player);
                i--;
            } else {
                CopyOnWriteArrayList<Projectile> tmp = player.getProjectiles();
                int sizeProjectiles = tmp.size();
                for (int proj=0; proj<sizeProjectiles; proj++) {
                    getProjectiles().add(tmp.get(proj));
                    sizeProjectiles = tmp.size();
                }
                
                

                
                
                    //model.getProjectiles().addAll(player.getProjectiles());
                
                player.getProjectiles().clear();
            }
            size = getPlayers().size();
        }
     
        size = getProjectiles().size();
        for (int i = 0; i < size; i++) {
            Projectile projectile = getProjectiles().get(i);
            if (projectile.getHealth() <= 0) {
                getProjectiles().remove(projectile);
                i--;
            } else {
                projectile.updatePosition();
            }
            size = getProjectiles().size();
        }


        size = getPowerUps().size();
        for (int i = 0; i < size; i++) {
            PowerUp powerUp = getPowerUps().get(i);
            if (powerUp.getOwner() != null) {
                powerUp.getOwner().getPowerUps().add(powerUp);
                getPowerUps().remove(powerUp);
                i--;
            } else if (powerUp.getHealth() <= 0) {
                getPowerUps().remove(powerUp);
                i--;
            }
            size = getPowerUps().size();
        }
        
        size = getBlocks().size();
        for (int i = 0; i < size; i++) {//************************************
            Block block = getBlocks().get(i);
            if (block.getHealth() <= 0) {
                getBlocks().remove(block);
                i--;
            }
            size = getBlocks().size();
        }
    }


}
