package main.java.nz.ac.vuw.ecs.swen225.gp22.application;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Domain;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Chap;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Monster;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Item;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Keys;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Treasure;

/**
 * Created with IntelliJ IDEA. Description: User: Yusi Cheng Date: 2022-09-29 Time: 0:28
 */
public class InfoPanel extends JLayeredPane {

  private Timer timer;

  private int gameTime;

  private Domain domain;

  private AppView appView;

  //private String treasures;

  public void setDomain(Domain domain) {
    this.domain = domain;
  }

  /**
   * Initialize the information side panel.
   *
   * @param appView -  domain passed through from the gui
   */
  public InfoPanel(AppView appView){
    this.appView = appView;
    this.domain = appView.getDomain();
    setPreferredSize(new Dimension(150, 500));
    this.gameTime = 60;
    //this.treasures = String.valueOf(domain.numTreasures());
    setBackground(new Color(132, 166, 231));
    timer = new Timer(1000, e -> {
      if (gameTime > 0) {
        this.gameTime--;
        this.domain.moveAllMonster();
        if (!domain.chap.isLive()){
          timer.stop();
          JOptionPane.showMessageDialog(null, "you died, please restart");
          appView.restartGame();
          return;
        }
      }else {
        timer.stop();
        JOptionPane.showMessageDialog(appView.getWindow(), " time expired press any key to restart.");
        appView.restartGame();
        domain = appView.getDomain();
        gameTime = 60;
      }
      InfoPanel.this.repaint();
    });

  }

  /**
   * Start counting.
   */
  public void startTimer(){
    if (timer.isRunning()) {
      return;
    }
    timer.start();
  }

  public void stopTimer(){
    timer.stop();
  }

  public void resetTimer(){
    gameTime = 60;
  }

  /**
   * Draw all component in infoPanel.
   */
  @Override
  protected void paintComponent(Graphics g) {
    g.setColor(Color.white);
    g.fillRect(0, 15, 140, 425);
    drawLevelPanel(g);
    drawTimePanel(g);
    drawTreasurePanel(g);
    drawInventoryPane(g);
  }

  /**
   * Draw level panel.
   */
  private void drawLevelPanel(Graphics g){
    g.setColor( new Color(77, 135, 225));
    g.fillRect(5, 20, 130, 100);
    g.setColor(Color.white);
    g.setFont(new Font("普通", Font.PLAIN, 20));
    g.drawString("LEVEL", 42, 40);
    g.setColor(Color.BLACK);
    g.setFont(new Font("普通", Font.PLAIN, 30));
    g.drawString(appView.getCurrentLevel()+"", 63, 100);
    g.setFont(new Font("普通", Font.PLAIN, 10));
  }

  /**
   * Draw time panel.
   */
  private void drawTimePanel(Graphics g){
    g.setColor( new Color(61, 122, 227));
    g.fillRect(5, 125, 130, 100);
    g.setColor(Color.white);
    g.setFont(new Font("普通", Font.PLAIN, 20));
    g.drawString("TIME LEFT", 20, 145);
    g.setColor(Color.BLACK);
    g.setFont(new Font("普通", Font.PLAIN, 30));
    g.drawString(gameTime + "", 55, 210);
    g.setFont(new Font("普通", Font.PLAIN, 10));
  }

  /**
   * Draw treasure panel.
   */
  private void drawTreasurePanel(Graphics g){
    g.setColor(new Color(45, 105, 222));
    g.fillRect(5, 230, 130, 100);
    g.setColor(Color.white);
    g.setFont(new Font("普通", Font.PLAIN, 20));
    g.drawString("TREASURE", 17, 250);
    g.setColor(Color.BLACK);
    g.setFont(new Font("普通", Font.PLAIN, 30));
    g.drawString(domain.numTreasures()+"", 60, 310);
    g.setFont(new Font("普通", Font.PLAIN, 10));
  }

  /**
   * Draw inventory panel.
   */
  private void drawInventoryPane(Graphics g){
    g.setColor(new Color(18, 83, 218));
    g.fillRect(5, 335, 130, 100);
    g.setColor(Color.white);
    g.setFont(new Font("普通", Font.PLAIN, 20));
    g.drawString("INVENTORY", 15, 355);
    //
    g.setColor(Color.WHITE);
    int startX = 10;
    int endX = 130;
    int startY = 370;
    int endY = 430;
    Graphics2D g2 = (Graphics2D) g;
    for (int x = startX; x <= endX ; x = x + 30) {
      g2.setStroke(new BasicStroke(2.0f));
      g2.drawLine(x, startY, x, endY);
    }

    for (int y = startY; y <= endY ; y = y + 30) {
      g2.drawLine(startX, y, endX, y);
    }

    Chap chap = domain.getChap();
    List<Keys> keys = chap.getKeys();
    //System.out.println(keys.size());
    //List<Treasure> treasures = chap.getTreasures();
    //List<Item> items = new ArrayList<>();
    //items.addAll(keys);
    //items.addAll(treasures);

    for (int i = 0; i < keys.size(); i++) {
      Image image = Toolkit.getDefaultToolkit().getImage(keys.get(i).getPath());
      int x;
      if (i < 4) {
        x = 10 + i * 30;
      } else {
        x = 10 + (i - 4) * 20;
      }
      int y;
      if (i < 4){
        y = 370;
      } else {
        y = 400;
      }
      g.drawImage( image, x, y, 30, 30, this);
    }

  }

}
