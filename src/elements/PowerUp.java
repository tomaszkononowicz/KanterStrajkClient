/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import enums.PowerUpEnum;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import static enums.ShapeEnum.ELIPSE;
import java.io.Serializable;

/**
 *
 * @author Tomasz
 */


public class PowerUp extends Element implements Serializable{
    private Player owner; //rezerwuje powerUp dla gracza zanim bedzie update modelu
    private PowerUpEnum type;
       
    public PowerUp(Point position, PowerUpEnum type) {
        super(position, new Dimension(30, 30), 0, ELIPSE, 0, 500); 
        this.type = type;
    }
    
    public PowerUp(Point position, PowerUpEnum type, Dimension dimension) {
        super(position, dimension, 0, ELIPSE, 0, 500);
        this.type = type;
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public PowerUpEnum getType() {
        return type;
    }
    
    
    public void collect(Player collector) {
        this.owner = collector;
    }
   
    
@Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.updateDrawPosition();

        //Random r = new Random();
        
        //test, zeby sie pokazalo cokolwiek
        //this.positionDraw; punkt x i y do rysowania
        //this.dimension; wymiar pamietaj ze musi width=height bo to kolo, chyba ze chcemy elipse :D
        //this.direction; z przedzialu od -pi do pi, obrot, raczej bedzie losowy
        g2d.setStroke(new BasicStroke(2));
        int offset = dimension.width / 12;

        int centerX = positionDraw.x + dimension.width / 2;
        int centerY = positionDraw.y + dimension.height / 2;
        switch (type) { 
            case TRIPLESHOT:
                g2d.setColor(Color.RED);
                g2d.fillOval(positionDraw.x, positionDraw.y, dimension.width, dimension.height);
                g2d.setColor(Color.GREEN);
                //g2d.setColor(new Color(r.nextInt()));
                g2d.fillOval(centerX, centerY, dimension.width / 3, dimension.height / 3);
                g2d.fillOval(centerX - dimension.width / 3, centerY, dimension.width / 3, dimension.height / 3);
                g2d.fillOval(centerX - dimension.width / 6, centerY - dimension.height / 3, dimension.width / 3, dimension.height / 3);
                break;
            case BIGGERSHOT:
                g2d.setColor(Color.RED);
                g2d.fillOval(positionDraw.x, positionDraw.y, dimension.width, dimension.height);
                g2d.setColor(Color.GREEN);
                //g2d.setColor(new Color(r.nextInt()));
                g2d.fillOval(centerX - dimension.width / 4, centerY - dimension.height / 4, dimension.width / 2, dimension.height / 2);
                break;
            case FASTPLAYER:
                g2d.setColor(Color.RED);
                g2d.fillOval(positionDraw.x, positionDraw.y, dimension.height, dimension.width);
                g2d.setColor(Color.GREEN);
                //g2d.setColor(new Color(r.nextInt()));
                g2d.drawLine(centerX + dimension.width / 4, centerY, centerX, centerY + dimension.height / 3);
                g2d.drawLine(centerX + dimension.width / 4, centerY, centerX, centerY - dimension.height / 3);
                g2d.drawLine(centerX, centerY, centerX - dimension.width / 4, centerY + dimension.height / 3);
                g2d.drawLine(centerX, centerY, centerX - dimension.width / 4, centerY - dimension.height / 3);
                break;
            case SLOWPLAYER:
                g2d.setColor(Color.RED);
                g2d.fillOval(positionDraw.x, positionDraw.y, dimension.height, dimension.width);
                g2d.setColor(Color.GREEN);
                //g2d.setColor(new Color(r.nextInt()));
                //skorupa
                g2d.drawArc(centerX - dimension.width / 3 - offset, centerY - dimension.height / 6, dimension.width / 2, dimension.height / 2, 0, 270);
                g2d.drawArc(centerX - dimension.width / 4 - offset, centerY - dimension.height / 16, dimension.width / 3, dimension.height / 3, 0, 270);
                //glowa
                g2d.drawArc(centerX + dimension.width / 8, centerY - dimension.height / 4, dimension.width / 4, dimension.height / 4, 0, 180);
                //czulki
                g2d.drawArc(centerX - dimension.width / 16, centerY - dimension.height / 3, dimension.width / 4, dimension.height / 4, 0, 75);
                g2d.drawArc(centerX + dimension.width / 16, centerY - dimension.height / 3, dimension.width / 4, dimension.height / 4, 0, 75);
                //szyja
                g2d.drawLine(centerX + dimension.width / 8, centerY + dimension.height / 8, centerX + dimension.width / 8, centerY - dimension.height / 8);
                g2d.drawLine(centerX + dimension.width / 3, centerY + dimension.height / 8, centerX + dimension.width / 3 + offset - offset / 2, centerY - dimension.height / 8);
                //brzuch
                g2d.drawArc(centerX - dimension.width / 6, centerY - dimension.height / 6, dimension.width / 2, dimension.height / 2, 270, 90);
                g2d.drawLine(centerX + offset, centerY + dimension.height / 3 + 2, centerX - dimension.width / 6, centerY + dimension.height / 3 + 2);
                break;
            case SMALLERPLAYER:
                g2d.setColor(Color.RED);
                g2d.fillOval(positionDraw.x, positionDraw.y, dimension.height, dimension.width);
                g2d.setColor(Color.GREEN);
                //g2d.setColor(new Color(r.nextInt()));
                //prawy dol
                g2d.drawLine(centerX + offset, centerY + offset, centerX + dimension.width / 4 + offset, centerY + dimension.height / 4 + offset);
                g2d.drawLine(centerX + offset, centerY + offset, centerX + dimension.width / 3 - offset, centerY + offset);
                g2d.drawLine(centerX + offset, centerY + offset, centerX + offset, centerY + dimension.height / 3 - offset);
                //lewy dol
                g2d.drawLine(centerX - offset, centerY + offset, centerX - dimension.width / 4 - offset, centerY + dimension.height / 4 + offset);
                g2d.drawLine(centerX - offset, centerY + offset, centerX - dimension.width / 3 + offset, centerY + offset);
                g2d.drawLine(centerX - offset, centerY + offset, centerX - offset, centerY + dimension.height / 3 - offset);
                // gora lewo
                g2d.drawLine(centerX - offset, centerY - offset, centerX - dimension.width / 4 - offset, centerY - dimension.height / 4 - offset);
                g2d.drawLine(centerX - offset, centerY - offset, centerX - dimension.width / 3 + offset, centerY - offset);
                g2d.drawLine(centerX - offset, centerY - offset, centerX - offset, centerY - dimension.height / 3 + offset);
                //gora prawo
                g2d.drawLine(centerX + offset, centerY - offset, centerX + dimension.width / 4 + offset, centerY - dimension.height / 4 - offset);
                g2d.drawLine(centerX + offset, centerY - offset, centerX + dimension.width / 3 - offset, centerY - offset);
                g2d.drawLine(centerX + offset, centerY - offset, centerX + offset, centerY - dimension.height / 3 + offset);
                break;
            case FASTSHOT:
                g2d.setColor(Color.RED);
                g2d.fillOval(positionDraw.x, positionDraw.y, dimension.width, dimension.height);
                g2d.setColor(Color.GREEN);
                //g2d.setColor(new Color(r.nextInt()));
                g2d.fillOval(centerX, centerY - dimension.height / 5, dimension.width / 3 + offset, dimension.height / 3 + offset);
                g2d.drawLine(centerX, centerY - dimension.height / 6, centerX - dimension.width / 3, centerY - dimension.height / 6);
                g2d.drawLine(centerX, centerY, centerX - dimension.width / 4, centerY);
                g2d.drawLine(centerX, centerY + dimension.height / 6, centerX - dimension.width / 3, centerY + dimension.height / 6);
                break;
            default:
                break;
        }
    }

    @Override
    public void attack(Element defender) {
  
        //Bonus nie moze atakowac
        //System.out.println(this.getClass().getName() + " zaatakowalo " + defender.toString());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    
    
}
