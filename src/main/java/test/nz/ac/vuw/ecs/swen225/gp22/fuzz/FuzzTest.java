package main.java.test.nz.ac.vuw.ecs.swen225.gp22.fuzz;

import static main.java.nz.ac.vuw.ecs.swen225.gp22.persistency.gameLoader.levelloader;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.Point;
import java.util.Random;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import main.java.nz.ac.vuw.ecs.swen225.gp22.application.AppView;
import main.java.nz.ac.vuw.ecs.swen225.gp22.application.Main;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Domain;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Chap;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Monster;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds.HitWall;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds.UnlockDoor;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Keys;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.otherInfo.Direction;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.FreeTile;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.IronBox;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.LockDoor;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.Tile;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.Water;

/**
 * @Description: Invoke methods from APP to generate random input that achieves 60% coverage
 * @Author: Yiming Liang
 * @Date: 2022/10/9
 */
public class FuzzTest { // invoke methods in APP to generate random input
//  private AppView game;

  public static void test1(){ // level 1
    setUpLevel("level1");
    AppView game = new AppView(null, false);
    game.startLeve1();
    Domain domain = game.getDomain();
    Chap chap = domain.getChap();
    chap.setDirection(Direction.Down);
    for (int i = 0; i < 1000; i++) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      moveChap(game, domain);
    }
  }

  public static void test2(){ // level 2
    setUpLevel("level2");
    AppView game = new AppView(null, false);
    game.startLeve2();
    Domain domain = game.getDomain();
    Chap chap = domain.getChap();
    chap.setDirection(Direction.Down);
    for (int i = 0; i < 1000; i++) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      moveChap(game, domain);
    }
  }

  @SuppressFBWarnings("DMI_RANDOM_USED_ONLY_ONCE")
  public static void moveChap(AppView game, Domain domain){
    Chap chap = domain.getChap();
    Point currPos  = chap.getPoint();
    Direction currDir = chap.getDirection();
    Tile next = nextDirTile(domain, domain.getTiles()[currPos.x][currPos.y], currDir);
    Random rand = new Random();
    try{
      if(next != null && !(next instanceof Water) && checkNext(domain, next, currDir)){
        game.playerMovement(currDir, false);
      }
      else {
//        int num = (int)(Math.random() * 4);
        chap.setDirection(Direction.values()[rand.nextInt(4)]);
        moveChap(game, domain);
      }
    } catch (IllegalArgumentException e) {

    }

  }

  public static boolean checkNext(Domain domain, Tile tile, Direction dir){
    Chap chap = domain.getChap();
    Tile[][] tiles = domain.getTiles();
    if(tile.isWalkable()){
      return true;
    }else{
      if(tile instanceof LockDoor){
        Keys key = chap.matchKey((LockDoor) tile);
        if(key != null){
          UnlockDoor.playSounds();
          chap.unPickup(key);
          tiles[tile.getPoint().x][tile.getPoint().y] = new FreeTile(tile.getPoint());
          return true;
        }
      }
      if(tile instanceof IronBox){
        Tile next_i = nextDirTile(domain,tile,dir);
        if(next_i instanceof Water){
          tiles[tile.getPoint().x][tile.getPoint().y] = new FreeTile(tile.getPoint());
          tiles[next_i.getPoint().x][next_i.getPoint().y] = new FreeTile(next_i.getPoint());
          return true;
        }
        if(next_i.isWalkable()){
          tiles[tile.getPoint().x][tile.getPoint().y] = new FreeTile(tile.getPoint());
          tiles[next_i.getPoint().x][next_i.getPoint().y] = new IronBox(next_i.getPoint());
          return true;
        }
        return false;
      }
    }
    HitWall.playSounds();
    return false;
  }

  public static Tile nextDirTile(Domain domain, Tile t, Direction d) {
    Tile[][] tiles = domain.getTiles();
    int height = domain.getHeight();
    int width = domain.getWidth();
    switch (d) {
      case Up:
        if(t.getPoint().y <= 0){
          HitWall.playSounds();
          return null;
        }
        return tiles[t.getPoint().x][t.getPoint().y - 1];
      case Down:
        if(t.getPoint().y >= height-1){
          HitWall.playSounds();
          return null;
        }
        return tiles[t.getPoint().x][t.getPoint().y + 1];
      case Left:
        if(t.getPoint().x <= 0){
          HitWall.playSounds();
          return null;
        }
        return tiles[t.getPoint().x - 1][t.getPoint().y];
      case Right:
        if(t.getPoint().x >= width-1){
          HitWall.playSounds();
          return null;
        }
        return tiles[t.getPoint().x + 1][t.getPoint().y];
      default:
        return null;
    }
  }

  public static void setUpLevel(String levelname) {
    try {
      levelloader(levelname); // cannot open level2 directly
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }

  }

  public static void main(String... args) {
//    test1();
    test2();
  }

}
