package elements;


import enums.BlockEnum;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import static enums.BlockEnum.FRAME;
import static enums.BlockEnum.HARD;
import static enums.BlockEnum.MEDIUM;
import elements.Element;
import enums.ShapeEnum;
import static enums.ShapeEnum.ELIPSE;
import static enums.ShapeEnum.RECTANGLE;
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ravo
 */
public class Block extends Element implements Serializable {

    private BlockEnum type;
    
     public Block(Point position,BlockEnum type, int width, int height) {
           super(position, new Dimension(width, height), 0, RECTANGLE, 0, 500); 
           this.type = type;
           if (type==BlockEnum.MEDIUM) this.health=500;
           if (type==BlockEnum.HARD) this.health=1000;
        
      
        
        
    }
    
    public Block(Point position, Dimension dimension, double direction, ShapeEnum shape, int speed, int health) {
        super(position, dimension, direction, shape, speed, health);
    }

    @Override
    public void draw(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        super.updateDrawPosition();
        if(this.type == FRAME)
        {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(positionDraw.x, positionDraw.y, dimension.width, dimension.height);
        }else if(this.type == MEDIUM)
        {
            
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(positionDraw.x, positionDraw.y, dimension.width, dimension.height);
            
        }else if(this.type == HARD)
        {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(positionDraw.x, positionDraw.y, dimension.width, dimension.height);
        }
        
        
    }

    @Override
    public void attack(Element defender) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public BlockEnum getType()
    {
        return this.type;
    }
}
