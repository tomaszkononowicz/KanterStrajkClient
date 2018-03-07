/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import enums.ShapeEnum;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

/**
 *
 * @author Tomasz
 */
public abstract class Element implements Serializable {
    protected Point position;
    protected Point oldPosition;
    //protected Point oldPoint; //Dla funkcji rollBack przy kolizji
    protected Dimension dimension;
    protected double direction;
    protected int speed;
    protected ShapeEnum shape;
    protected Point positionDraw;
    protected int health;
    protected String killerName;
    
    public Element(Point position, Dimension dimension, double direction, ShapeEnum shape, int speed, int health){
        this.position = position;
        this.oldPosition = new Point(position);
        this.dimension = dimension;
        this.positionDraw = new Point(position.x-dimension.width/2, position.y-dimension.height/2);
        this.direction = direction;
        this.shape = shape;
        this.speed = speed;
        this.health = health;
    }
    
    public abstract void draw(Graphics g);
    public abstract void attack(Element defender);
    public boolean colision(Element element) { //dla serwera
        double width = this.dimension.width/2 + element.dimension.width/2;
        double height = this.dimension.height/2 + element.dimension.height/2;
        double distanceX = Math.pow((this.position.x - element.position.x), 2);
        double distanceY = Math.pow((this.position.y - element.position.y), 2);
        double circleDistanceX = Math.abs(this.position.x - element.position.x);//******************************************************
        double circleDistanceY = Math.abs(this.position.y - element.position.y);//******************************************************
        double distance;
        switch (this.shape) {
            case ELIPSE:
                switch (element.shape) {
                    case ELIPSE:
                        distance = Math.sqrt(distanceX + distanceY);
                        if (width < distance) return false;
                        break;
                    case RECTANGLE:
//                        distance = Math.pow((distanceX - element.dimension.width/2), 2) + Math.pow((distanceY - element.dimension.height/2), 2);
//                        if (distance > Math.pow((this.dimension.width/2), 2)) return false;
                        if(circleDistanceX > width)return false;//******************************************************
                        else if(circleDistanceY > height) return false;
                        else if(circleDistanceX <= this.dimension.width/2) return true;
                        else if(circleDistanceY <= this.dimension.height/2) return true;
                        break;
                }
                break;
            case RECTANGLE:
                switch (element.shape) {
                    case ELIPSE:
                        if(circleDistanceX > width)return false;//******************************************************
                        else if(circleDistanceY > height) return false;
                        else if(circleDistanceX <= this.dimension.width/2) return true;
                        else if(circleDistanceY <= this.dimension.height/2) return true;
                        break;
                    case RECTANGLE:
                        if (width < distanceX) return false;
                        if (height < distanceY) return false;
                        break;
                }
                break;
        }
        return true;    
    }
    
    public int getHealth() {
        return health;
    }
        
    public String getKillerName() {
        return killerName;
    }
    
    public void setKillerName(String killerName) {
        this.killerName = killerName;
    }
    
    public void setHealth(int health) {
        this.health = health;
    }
    
    public void kill() {
        health = 0;
    }
    
    public void updateDrawPosition() {
        positionDraw.x = position.x-dimension.width/2;
        positionDraw.y = position.y-dimension.height/2;
    }
    public void rollBackPosition() {
        position = new Point(oldPosition);
    }
    
    public void savePosition() {
        oldPosition = new Point(position);
    }
    
    
    public void moveDirection() {
        Point destination = new Point(); 
        setPosition(destination);  
    }
    
     
    public Point getPosition() {
        return position;
    }
    
    public double getDirection() {
        return direction;
    }
    
    public void setDirection(double direction) {
        this.direction = direction;
    }
    
    public void setPosition(Point position) {
        this.position=position;
    }
    
    public void setPosition(int x, int y) {
        position = new Point(x,y);
    }
    
    public Dimension getDimension(){
        return dimension;
    }
    
}
