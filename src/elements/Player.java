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
import java.util.concurrent.CopyOnWriteArrayList;
import static enums.ShapeEnum.ELIPSE;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.UUID;

/**
 *
 * @author Tomasz
 */
public class Player extends Element implements Serializable {

    private CopyOnWriteArrayList<Projectile> projectiles = new CopyOnWriteArrayList<Projectile>(); //obiekty bo gracz wyprodukowal
    private CopyOnWriteArrayList<PowerUp> powerUps = new CopyOnWriteArrayList<PowerUp>(); //referencje bo model(game) wyprodukowal a nie gracz
    private UUID id = UUID.randomUUID();
    private int score;
    private int strength;
    private Color primaryColor;
    private Color secondaryColor;
    private String name;

    public Player(Point position, Color primaryColor, Color secondaryColor, String name) {
        super(position, new Dimension(50, 50), 1, ELIPSE, 3, 100);
        this.score = 0;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.name = name;
        
    }

    public Player(Point position, Dimension dimension, double direction, int speed, int score, int strength, Color primaryColor, Color secondaryColor, String name) {
        super(position, dimension, direction, ELIPSE, speed, 500);
        this.health = 100;
        this.score = score;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.strength = strength;
        this.name = name;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }
    
    public String getName() {
        return name;
    }
    
    public void setPrimaryColor(Color color) {
        this.primaryColor = color;
    }
       
    public UUID getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public CopyOnWriteArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
    
    public void setProjectiles(CopyOnWriteArrayList<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    public CopyOnWriteArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }

    public boolean hasPowerUp(PowerUpEnum type) {
        for (PowerUp p : powerUps) {
            if (p.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public void shot() {
        if (hasPowerUp(PowerUpEnum.TRIPLESHOT)) {
            //nie umiem zaimplementowac cloneable
            Projectile p1 = new Projectile(new Point(position), primaryColor, direction + 0.1, this);
            Projectile p2 = new Projectile(new Point(position), primaryColor, direction, this);
            Projectile p3 = new Projectile(new Point(position), primaryColor, direction - 0.1, this);
            if (hasPowerUp(PowerUpEnum.BIGGERSHOT)) {
                p1.setDimension(new Dimension(13, 13));
                p2.setDimension(new Dimension(16, 16));
                p3.setDimension(new Dimension(13, 13));
            }

            if (hasPowerUp(PowerUpEnum.FASTSHOT)) {
                p1.setSpeed(speed + 1);
                p2.setSpeed(speed + 1);
                p3.setSpeed(speed + 1);
            }
            projectiles.add(p1);
            projectiles.add(p2);
            projectiles.add(p3);
        } else {
            Projectile p = new Projectile(new Point(position), primaryColor, direction, this);
            if (hasPowerUp(PowerUpEnum.BIGGERSHOT)) {
                p.setDimension(new Dimension(15, 15));
            }
            if (hasPowerUp(PowerUpEnum.FASTSHOT)) {
                p.setSpeed(speed + 2);
            }
            projectiles.add(p);
        }
    }

    public void updateDirection(int x, int y) {
        setDirection(Math.atan2(position.x - x, y - position.y));
    }

    public void updatePosition(boolean left, boolean up, boolean right, boolean down) {
        savePosition();//**********************************************
        if (left && right) {
        } else if (up && down) {
        } else if (right && up) {
            position.x += speed;
            position.y -= speed;
        } else if (right && down) {
            position.x += speed;
            position.y += speed;
        } else if (left && up) {
            position.x -= speed;
            position.y -= speed;
        } else if (left && down) {
            position.x -= speed;
            position.y += speed;
        } else if (left) {
            position.x -= speed;
        } else if (right) {
            position.x += speed;
        } else if (up) {
            position.y -= speed;
        } else if (down) {
            position.y += speed;
        }

    }

    public void drawMyStats(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.updateDrawPosition();

        //zycie + zebrane powerupy
        //health--;
        if (health >= 0) {
            g2d.setStroke(new BasicStroke(dimension.width / 12));
            g2d.setColor(Color.RED);
           /*****LUKASZ*****/
            //To musi być na sztywno, bo nie mamy w Playerze odniesienia do planszy (?)
            int healthBarX = 900;
            int healthBarY = 645;
            /****************/

            int heartX = healthBarX - 50;
            int heartY = healthBarY - 5;

            int[] xp = {heartX, heartX + 37, heartX + 19};
            int[] yp = {heartY + 14, heartY + 14, heartY + 30};

            g.fillOval(heartX, heartY, 20, 20);
            g.fillOval(heartX + 17, heartY, 20, 20);
            g.fillPolygon(xp, yp, xp.length);

            g2d.drawRect(healthBarX, healthBarY, 300, 20);
            for (int i = 0; i < health; i++) {
                g2d.drawLine(healthBarX + 3 * i, healthBarY, healthBarX + 3 * i, healthBarY + 20);
            }

            for (PowerUp pu : this.powerUps) {
                g2d.setStroke(new BasicStroke(1));
                g2d.setColor(Color.WHITE);
                /*****LUKASZ*****/
                int startX = 60;
                int startY = healthBarY + 10;
                /****************/
                Dimension puDimension = pu.getDimension();
                int offset = puDimension.width / 12;
                int shift = 100;

                switch (pu.getType()) {
                    case TRIPLESHOT:
                        g2d.fillOval(startX - puDimension.width / 2, startY - puDimension.height / 2, puDimension.width, puDimension.height);
                        g2d.setColor(this.primaryColor);
                        g2d.fillOval(startX, startY, puDimension.width / 3, puDimension.height / 3);
                        g2d.fillOval(startX - puDimension.width / 3, startY, puDimension.width / 3, puDimension.height / 3);
                        g2d.fillOval(startX - puDimension.width / 6, startY - puDimension.height / 3, puDimension.width / 3, puDimension.height / 3);
                        break;
                    case BIGGERSHOT:
                        g2d.fillOval(startX + shift - puDimension.width / 2, startY - puDimension.height / 2, puDimension.width, puDimension.height);
                        g2d.setColor(this.primaryColor);
                        g2d.fillOval(startX + shift - puDimension.width / 4, startY - puDimension.height / 4, puDimension.width / 2, puDimension.height / 2);
                        break;
                    case FASTPLAYER:
                        g2d.fillOval(startX + 2 * shift - puDimension.width / 2, startY - puDimension.height / 2, puDimension.width, puDimension.height);
                        g2d.setColor(this.primaryColor);
                        g2d.drawLine(startX + 2 * shift + puDimension.width / 4, startY, startX + 2 * shift, startY + puDimension.height / 3);
                        g2d.drawLine(startX + 2 * shift + puDimension.width / 4, startY, startX + 2 * shift, startY - puDimension.height / 3);
                        g2d.drawLine(startX + 2 * shift, startY, startX + 2 * shift - puDimension.width / 4, startY + puDimension.height / 3);
                        g2d.drawLine(startX + 2 * shift, startY, startX + 2 * shift - puDimension.width / 4, startY - puDimension.height / 3);
                        break;
                    case SLOWPLAYER:
                        g2d.fillOval(startX + 3 * shift - puDimension.width / 2, startY - puDimension.height / 2, puDimension.width, puDimension.height);
                        g2d.setColor(this.primaryColor);
                        g2d.drawArc(startX + 3 * shift - puDimension.width / 3 - offset, startY - puDimension.height / 6, puDimension.width / 2, puDimension.height / 2, 0, 270);
                        g2d.drawArc(startX + 3 * shift - puDimension.width / 4 - offset, startY - puDimension.height / 16, puDimension.width / 3, puDimension.height / 3, 0, 270);
                        g2d.drawArc(startX + 3 * shift + puDimension.width / 8, startY - puDimension.height / 4, puDimension.width / 4, puDimension.height / 4, 0, 180);
                        g2d.drawArc(startX + 3 * shift - puDimension.width / 16, startY - puDimension.height / 3, puDimension.width / 4, puDimension.height / 4, 0, 75);
                        g2d.drawArc(startX + 3 * shift + puDimension.width / 16, startY - puDimension.height / 3, puDimension.width / 4, puDimension.height / 4, 0, 75);
                        g2d.drawLine(startX + 3 * shift + puDimension.width / 8, startY + puDimension.height / 8, startX + 3 * shift + puDimension.width / 8, startY - puDimension.height / 8);
                        g2d.drawLine(startX + 3 * shift + puDimension.width / 3, startY + puDimension.height / 8, startX + 3 * shift + puDimension.width / 3 + offset - offset / 2, startY - puDimension.height / 8);
                        g2d.drawArc(startX + 3 * shift - puDimension.width / 6, startY - puDimension.height / 6, puDimension.width / 2, puDimension.height / 2, 270, 90);
                        g2d.drawLine(startX + 3 * shift + offset, startY + puDimension.height / 3 + 2, startX + 3 * shift - puDimension.width / 6, startY + puDimension.height / 3 + 2);
                        break;
                    case SMALLERPLAYER:
                        g2d.fillOval(startX + 4 * shift - puDimension.width / 2, startY - puDimension.height / 2, puDimension.width, puDimension.height);
                        g2d.setColor(this.primaryColor);
                        g2d.drawLine(startX + 4 * shift + offset, startY + offset, startX + 4 * shift + puDimension.width / 4 + offset, startY + puDimension.height / 4 + offset);
                        g2d.drawLine(startX + 4 * shift + offset, startY + offset, startX + 4 * shift + puDimension.width / 3 - offset, startY + offset);
                        g2d.drawLine(startX + 4 * shift + offset, startY + offset, startX + 4 * shift + offset, startY + puDimension.height / 3 - offset);
                        g2d.drawLine(startX + 4 * shift - offset, startY + offset, startX + 4 * shift - puDimension.width / 4 - offset, startY + puDimension.height / 4 + offset);
                        g2d.drawLine(startX + 4 * shift - offset, startY + offset, startX + 4 * shift - puDimension.width / 3 + offset, startY + offset);
                        g2d.drawLine(startX + 4 * shift - offset, startY + offset, startX + 4 * shift - offset, startY + puDimension.height / 3 - offset);
                        g2d.drawLine(startX + 4 * shift - offset, startY - offset, startX + 4 * shift - puDimension.width / 4 - offset, startY - puDimension.height / 4 - offset);
                        g2d.drawLine(startX + 4 * shift - offset, startY - offset, startX + 4 * shift - puDimension.width / 3 + offset, startY - offset);
                        g2d.drawLine(startX + 4 * shift - offset, startY - offset, startX + 4 * shift - offset, startY - puDimension.height / 3 + offset);
                        g2d.drawLine(startX + 4 * shift + offset, startY - offset, startX + 4 * shift + puDimension.width / 4 + offset, startY - puDimension.height / 4 - offset);
                        g2d.drawLine(startX + 4 * shift + offset, startY - offset, startX + 4 * shift + puDimension.width / 3 - offset, startY - offset);
                        g2d.drawLine(startX + 4 * shift + offset, startY - offset, startX + 4 * shift + offset, startY - puDimension.height / 3 + offset);
                        break;
                    case FASTSHOT:
                        g2d.fillOval(startX + 5 * shift - puDimension.width / 2, startY - puDimension.height / 2, puDimension.width, puDimension.height);
                        g2d.setColor(this.primaryColor);
                        g2d.fillOval(startX + 5 * shift, startY - puDimension.height / 5, puDimension.width / 3 + offset, puDimension.height / 3 + offset);
                        g2d.drawLine(startX + 5 * shift, startY - puDimension.height / 6, startX + 5 * shift - puDimension.width / 3, startY - puDimension.height / 6);
                        g2d.drawLine(startX + 5 * shift, startY, startX + 5 * shift - puDimension.width / 4, startY);
                        g2d.drawLine(startX + 5 * shift, startY + puDimension.height / 6, startX + 5 * shift - puDimension.width / 3, startY + puDimension.height / 6);
                        break;
                    default:
                        break;
                }

            }
        }
    }
    
    /*****LUKASZ*****/
    public void buffUp(PowerUp pu){
        switch(pu.getType()){
            case FASTPLAYER:
                this.speed = 4;
                break;
            case SLOWPLAYER:
                this.speed = 2;
                break;
            case SMALLERPLAYER:
                this.dimension.height = 40;                
                this.dimension.width = 40;
                break;
        }
    }
    /**************/

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.updateDrawPosition();

        //gracz
        g2d.setColor(primaryColor);
        g2d.fillOval(positionDraw.x, positionDraw.y, dimension.width, dimension.width);

        g2d.setStroke(new BasicStroke(4));
        //strzalka gracza
        g2d.setColor(secondaryColor);

        double arrowPositionX = position.x;
        double arrowPositionY = position.y;
        double arrowSize = dimension.width / 4;

        //punkty strzalki
        double pcx = arrowPositionX + arrowSize * Math.cos(direction + Math.PI / 2);
        double pcy = arrowPositionY + arrowSize * Math.sin(direction + Math.PI / 2);

        double plx = arrowPositionX + arrowSize * Math.cos(direction + Math.PI);
        double ply = arrowPositionY + arrowSize * Math.sin(direction + Math.PI);

        double prx = arrowPositionX + arrowSize * Math.cos(direction);
        double pry = arrowPositionY + arrowSize * Math.sin(direction);

        g2d.drawLine((int) pcx, (int) pcy, (int) plx, (int) ply);
        g2d.drawLine((int) pcx, (int) pcy, (int) prx, (int) pry);
    }

    @Override
    public void attack(Element defender) {
        if (defender instanceof Projectile); else if (defender instanceof PowerUp) {
            ((PowerUp) defender).collect(this);
            /*****LUKASZ*****/
            this.buffUp((PowerUp) defender);
            /****************/
        } else if (defender instanceof Block)//*****************************************
        {
            this.rollBackPosition();
        } else {
            //System.out.println(defender.toString());
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
