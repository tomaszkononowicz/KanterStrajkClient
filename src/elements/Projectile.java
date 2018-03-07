/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import static enums.BlockEnum.FRAME;
import static enums.ShapeEnum.*;
import java.io.Serializable;


/**
 *
 * @author Tomasz
 */
public class Projectile extends Element implements Cloneable, Serializable{
    private Color color;
    //Dla lepszych obliczen toru lotu
    private double movementX;
    private double movementY;
    private int strength;
    private Player owner;
    
    public Projectile(Point position, Color color, double direction, Player sender) {
        super(position, new Dimension(10,10), direction, ELIPSE, 3, 1000);
        this.owner = sender;
        this.color = color;
        this.strength = 40;
        movementX = position.x + (owner.dimension.width+20)/2 * Math.cos(direction + Math.PI / 2); //Poparte testami - pocisk spawnuje sie obok gracza a nie w nim
        movementY = position.y + (owner.dimension.height+20)/2 * Math.sin(direction + Math.PI / 2);
        position.x = (int) movementX;
        position.y = (int) movementY;
    }
    
    
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }    
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public void setStrength(int strength) {
        this.strength = strength;
    }
  
    
    @Override
    public void draw(Graphics g) {
        super.updateDrawPosition();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fillOval(positionDraw.x, positionDraw.y, dimension.width, dimension.width);
    }
    
    public void updatePosition() {
        movementX += speed * Math.cos(direction + Math.PI / 2);
        movementY += speed * Math.sin(direction + Math.PI / 2);
        position.x = (int) movementX;
        position.y = (int) movementY;
    }


    public int getStrength() {
        return strength;
    }
  
    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
    
    

    @Override
    public void attack(Element defender) {
        if (owner!=null && defender.equals(owner));
        else if (defender instanceof Projectile) {
          return;
        }
        else if (defender instanceof Block)
        {
            if(((Block) defender).getType() == FRAME) 
                this.kill();
            else
            {
                 defender.health -= this.strength;
                 this.kill();
            }
        }
        else {
            defender.health -= this.strength;
            if (owner!=null) defender.killerName = owner.getName();
            this.kill();
        }
        //System.out.println(defender.health + defender.getClass().toString());
        //if defender to player, sprawdz bonusy
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
