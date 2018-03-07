package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import main.Game;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alexp
 */
public class MenuButton
{
    private int x, y, width, height;
    private static BufferedImage img;
    private String text;
              
    static
    {   
        try        
        {
            img = ImageIO.read(Game.class.getResource("/button.png"));
        } catch (IOException ex)
        {
            Logger.getLogger(MenuButton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public MenuButton(int x, int y, int width, int height, String text) throws IOException
    {             
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }
    
    public boolean clicked(int clickx, int clicky)
    {
        if (clickx >= x && clickx <= x + width && clicky >= y && clicky <= y + height)
        {
            return true;
        }
        else return false;
    }
    
    public void drawButton(Graphics g)
    {        
        g.drawImage(img, x, y, width, height, null);        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Verdana", Font.PLAIN, 20));
        g.drawString(text, x + 10, y + 33);
    }
}
