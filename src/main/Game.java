/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import elements.Projectile;
import elements.PowerUp;
import elements.Player;
import elements.Block;
import enums.GameStateEnum;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import menu.MenuButton;
import java.util.UUID;
import javax.swing.text.Position;

/**
 *
 * @author Tomasz
 */
public class Game extends JPanel implements Runnable, KeyListener, ActionListener, MouseListener, MouseMotionListener {

    //TODO 
    /*
    Przy zabiciu ajko killer jest brany gracz z przedostatniego strzala bo juz na koniec nie przesyla pakietu
    IP z Menu
    Port z Menu
    
    */
    
    
    //stałe do menu
    public static final int BUTTONS_ORIGIN_X = 80;
    public static final int BUTTONS_ORIGIN_Y = 200;
    public static final int BUTTONS_DISTANCE = 60;
    public static final int BUTTON_WIDTH = 200;
    public static final int BUTTON_HEIGHT = 50;
    public static final int EXTENDED_SETTINGS = 1;
    public static final int EXTENDED_PLAYER = 2;
    private boolean displayExtendedMenu = false;
    private int currentExtendedMenu = 0;

    Model model;
    Player player;
    
    
    String serverIp = "153.19.216.62";
    int serverPort = 2137;
    
    
    
    
    UUID id;
    Map<Integer, Boolean> keyMap = new HashMap<Integer, Boolean>();
    Point mouse = new Point();
    int fps = 60;
    GameStateEnum gameState;
    boolean shot = false;


    
       
    private List<MenuButton> buttons = new CopyOnWriteArrayList();
    private MenuButton newGameButton, playerButton, settingsButton, quitButton, playerButtonChange, playerSettingsChange;
    private String ip = "153.19.216.62", port = "2137", playerName = "Terminator";
    private BufferedImage bgImage = null, transparentImage = null, failureImage = null, winImage = null;


    public Game(Dimension dimension) throws IOException {
        setSize(dimension);
        setFocusable(true);
        model = null;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        gameState = GameStateEnum.MENU;
        int[] keys = {KeyEvent.VK_A, KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER};
        for (int key : keys) {
            keyMap.put(key, Boolean.FALSE);
        }
        
        //przyciski
        newGameButton = new MenuButton(BUTTONS_ORIGIN_X, BUTTONS_ORIGIN_Y, BUTTON_WIDTH, BUTTON_HEIGHT, "NOWA GRA");
        buttons.add(newGameButton);
        playerButton = new MenuButton(BUTTONS_ORIGIN_X, BUTTONS_ORIGIN_Y + BUTTONS_DISTANCE, BUTTON_WIDTH, BUTTON_HEIGHT, "GRACZ");
        buttons.add(playerButton);
        settingsButton = new MenuButton(BUTTONS_ORIGIN_X, BUTTONS_ORIGIN_Y + 2 * BUTTONS_DISTANCE, BUTTON_WIDTH, BUTTON_HEIGHT, "USTAWIENIA");
        buttons.add(settingsButton);
        quitButton = new MenuButton(BUTTONS_ORIGIN_X, BUTTONS_ORIGIN_Y + 3 * BUTTONS_DISTANCE, BUTTON_WIDTH, BUTTON_HEIGHT, "WYJŚCIE");
        buttons.add(quitButton);
        playerButtonChange = new MenuButton(BUTTONS_ORIGIN_X + BUTTON_WIDTH + 50, BUTTONS_ORIGIN_Y + BUTTONS_DISTANCE * 3, BUTTON_WIDTH, BUTTON_HEIGHT, "ZMIEŃ");
        buttons.add(playerButtonChange);
        playerSettingsChange = new MenuButton(BUTTONS_ORIGIN_X + BUTTON_WIDTH + 50, BUTTONS_ORIGIN_Y + BUTTONS_DISTANCE * 3, BUTTON_WIDTH, BUTTON_HEIGHT, "ZMIEŃ");
        buttons.add(playerSettingsChange);

        
        bgImage = ImageIO.read(Game.class.getResource("/bg41.png"));
        transparentImage = ImageIO.read(Game.class.getResource("/transparent.png"));
        winImage = ImageIO.read(Game.class.getResource("/win.png"));
        failureImage = ImageIO.read(Game.class.getResource("/failure.png"));

    }


    @Override
    public void run() {
        //newGame();
        long start;
        while (true) {
            start = System.nanoTime();
            requestFocusInWindow();   

            revalidate();
            repaint();

            while (System.nanoTime() - start < 1e9 / fps);
        }

    }
    
    public synchronized void playerUpdate() {
        player.updatePosition(keyMap.get(KeyEvent.VK_A), keyMap.get(KeyEvent.VK_W), keyMap.get(KeyEvent.VK_D), keyMap.get(KeyEvent.VK_S));
            player.updateDirection(mouse.x, mouse.y);
            if (shot) {
                player.shot();
                shot = false;
            }
            
    }
    
    public synchronized void setModel(Model model) {
        this.model = model;
        //player = model.getPlayers().get(model.getPlayerIndexById(id));
    }
    
    public synchronized Player getPlayer() {
        return player;
    }
    
    public synchronized void setPlayer(Player player) {
        this.player = player;
    }
    
    public synchronized void setGameState(GameStateEnum gameState) {
        this.gameState = gameState;
    }
    
    public synchronized GameStateEnum getGameState() {
        return gameState;
    }
    

    //Jezeli w modelu jedna tablica to mozna to uproscic
    //Funckja dla serwera
    //Ktore kolizje pierwsze? bo w trakcie kolidowania listy moga malec
    //powerupy, strzaly, gracze
    //zamienic na zwykle foreach bo updaty modelu w innej metodzie
    public void paint(Graphics g) {
        switch (gameState) {
            case MENU: {
                //narysowanie tła
                g.drawImage(bgImage, 0, 0, null);
                //narysowanie pprzycisków
                for (MenuButton button : buttons) {
                    if (!button.equals(playerButtonChange) && !button.equals(playerSettingsChange)) {
                        button.drawButton(g);
                    }
                }
                //narysowanie tytułu
                g.setColor(Color.WHITE);
                g.setFont(new Font("Verdana", Font.PLAIN, 50));
                g.drawString("COUNTER STRIKE 6.9", BUTTONS_ORIGIN_X, 130);
                //wyświetlenie dodatkowych opcji
                if (displayExtendedMenu) {
                    //narysowanie tła dodatkowych opcji
                    g.drawImage(transparentImage, 300, BUTTONS_ORIGIN_Y, 600, 400, null);
                    g.setFont(new Font("Verdana", Font.PLAIN, 20));
                    switch (currentExtendedMenu) {
                        case EXTENDED_SETTINGS:
                            g.drawString("IP: " + ip, BUTTONS_ORIGIN_X + BUTTON_WIDTH + BUTTON_HEIGHT, BUTTONS_ORIGIN_Y + BUTTON_HEIGHT);
                            g.drawString("Port: " + port, BUTTONS_ORIGIN_X + BUTTON_WIDTH + BUTTON_HEIGHT, BUTTONS_ORIGIN_Y + BUTTONS_DISTANCE + BUTTON_HEIGHT);
                            playerButtonChange.drawButton(g);
                            break;
                        case EXTENDED_PLAYER:
                            g.drawString("Gracz: " + playerName, BUTTONS_ORIGIN_X + BUTTON_WIDTH + BUTTON_HEIGHT, BUTTONS_ORIGIN_Y + BUTTON_HEIGHT);
                            playerSettingsChange.drawButton(g);
                            break;
                    }
                } else {
                    currentExtendedMenu = 0;
                }
            }
            break;
            case PLAY:
                if (model != null) {
                    g.setColor(Color.white);
                    Rectangle boardBounds = g.getClipBounds();
                    g.fillRect(boardBounds.x, boardBounds.y, boardBounds.width, boardBounds.height);
                    for (Player player : model.getPlayers()) {
                        //player.updatePosition();
                        player.draw(g);
                    }

                    for (Projectile projectile : model.getProjectiles()) {
                        projectile.updatePosition(); //docelowo w logice serwera serwer
                        projectile.draw(g);
                    }
                    for (PowerUp powerUp : model.getPowerUps()) {
                        //powerUp.updatePosition();
                        powerUp.draw(g);
                    }
                    for (Block block : model.getBlocks()) {//**************************************
                        block.draw(g);
                    }
                    player.drawMyStats(g);
                }
                break;
            case WIN:
                g.drawImage(winImage, 0, 0, null);
                g.setColor(new Color(10, 73, 154));
                g.setFont(new Font("Verdana", Font.PLAIN, 90));
                g.drawString("WYGRAŁEŚ!", BUTTONS_ORIGIN_X + 250, 580);
                break;
            case LOSE:
                g.drawImage(failureImage, 0, 0, null);
                
                //przegrałeś
                g.setColor(new Color(193, 15, 19));
                g.setFont(new Font("Verdana", Font.PLAIN, 90));
                g.drawString("PRZEGRAŁEŚ!", BUTTONS_ORIGIN_X + 250, 170);
                
                //zabił cię
                g.setFont(new Font("Verdana", Font.PLAIN, 50));
                g.drawString("zabił cię " + player.getKillerName(), BUTTONS_ORIGIN_X + 290, 300);
                break;
                
        }
    }

    

    @Override
    public void keyTyped(KeyEvent e) { //Wcisniecie klawisza piszacego
        //System.out.println("keyTyped");
        switch (gameState) {
            case MENU:
                break;
            case PLAY:
                break;
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) { //Wcisniecie klawisza
        keyMap.put(e.getExtendedKeyCode(), Boolean.TRUE); 
    }

    @Override
    public void keyReleased(KeyEvent e) { //Puszczenie klawisza
        keyMap.put(e.getExtendedKeyCode(), Boolean.FALSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) { //Obsluga przyciskow itp
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent e) { //Wcisniecie + puszczenie
        //System.out.println("mouseClicked " + Math.atan2(e.getX(),e.getY()));       
    }

    @Override
    public void mousePressed(MouseEvent e) { //Wcisniecie
        if (e.getButton() == MouseEvent.BUTTON1) {
            keyMap.put(KeyEvent.VK_ENTER, Boolean.TRUE);
            
        }

        switch (gameState) {
            case MENU:
                for (MenuButton button : buttons) {
                    if (button.clicked(e.getX(), e.getY())) {
                        try {
                            handleMenuButton(button);
                        } catch (IOException ex) {
                            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                break;
            case PLAY:
                if (keyMap.get(KeyEvent.VK_ENTER).equals(Boolean.TRUE)) {
                    shot = true;
                    /*sendingTask.updatePlayer(player);
                    synchronized (player) {
                        sending.run();
                    }*/
                    
                }
                break;
        }
    }

   private void handleMenuButton(MenuButton button) throws IOException {
        if (button.equals(quitButton)) {
            displayExtendedMenu = false;
            System.exit(0);
        } else if (button.equals(settingsButton)) {
            displayExtendedMenu = true;
            currentExtendedMenu = EXTENDED_SETTINGS;
        } else if (button.equals(newGameButton)) {
            displayExtendedMenu = false;
            player = new Player(new Point(130,130), Color.gray, Color.darkGray, playerName);
            id = player.getId();
            gameState = GameStateEnum.PLAY;
            new Thread(new Connecting(serverIp, serverPort, this, id)).start(); 
        } else if (button.equals(playerButton)) {
            currentExtendedMenu = EXTENDED_PLAYER;
            displayExtendedMenu = true;
        } else if (button.equals(playerButtonChange) && currentExtendedMenu == EXTENDED_PLAYER) {
            //wyświetlenie mesydżboźa - pobranie nazwy gracza
            String text = JOptionPane.showInputDialog("Podaj nazwę gracza.");
            if (text != null) {
                playerName = text;
            }
        } else if (button.equals(playerSettingsChange) && currentExtendedMenu == EXTENDED_SETTINGS) {
            //wyświetlnie messedgeboxa - pobranie ip oraz portu
            String text = JOptionPane.showInputDialog("Podaj adres IP serwera.");
            if (text != null) {
                ip = text;
                serverIp = text;
            }
            String text2 = JOptionPane.showInputDialog("Podaj port.");
            if (text2 != null) {
                port = text2;
                serverPort = Integer.parseInt(text2);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) { //Puszczenie
        if (e.getButton() == MouseEvent.BUTTON1) {
            keyMap.put(KeyEvent.VK_ENTER, Boolean.FALSE);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) { //Wjechanie w okienko
    }

    @Override
    public void mouseExited(MouseEvent e) { //Wyjechanie poza okienko
    }

    @Override
    public void mouseDragged(MouseEvent e) { //??
        //System.out.println("mouseDragged");
        mouseMoved(e);
        //if (SwingUtilities.isLeftMouseButton(e))
        //mousePressed(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouse.x = e.getX();
        mouse.y = e.getY();
    }


}
