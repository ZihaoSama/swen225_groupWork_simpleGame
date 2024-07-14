package main.java.nz.ac.vuw.ecs.swen225.gp22.application;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.ObjectInputStream;

/**
 * Created with IntelliJ IDEA. Description: User: Yusi Cheng Date: 2022-09-21 Time: 3:33
 */
public class Main {

//  private AppView game;

  public Main() {
  }

  public static void main(String... args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    Main gameInstance = new Main();
    gameInstance.quickLoad();
  }

  public void quickLoad() {
//    this.game = new AppView(this, false);
    new AppView(this, false);
  }
}

