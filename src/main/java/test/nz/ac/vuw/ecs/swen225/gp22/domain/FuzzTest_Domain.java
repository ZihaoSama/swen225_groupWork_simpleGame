package main.java.test.nz.ac.vuw.ecs.swen225.gp22.domain;

import java.awt.Color;
import java.awt.Point;
import java.util.List;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Domain;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Monster;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Keys;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Treasure;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.otherInfo.Direction;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.Exit;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.ExitLock;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.FreeTile;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.Info_Field;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.IronBox;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.LockDoor;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.Tile;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.WallTile;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.Water;
import org.junit.jupiter.api.Test;

/**
 * @Description: Generate input to detect violations of the game logic.
 * @Author: Yiming Liang
 * @Date: 2022/9/20
 */
public class FuzzTest_Domain {
  @Test
  public void increaseTestCoverage() {
    Domain domain = setUpLevel2();
    domain.getTiles()[8][0].setItem(new Keys(Color.GREEN));
    domain.getTiles()[8][1].setItem(new Keys(Color.YELLOW));
    assert domain.getWidth() == 9;
    assert domain.getHeight() == 9;
    assert domain.numTreasures() == 2;

    domain.swapTile(new FreeTile(new Point(1, 1)));
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|K|\n"
            + "|M|-|R|-|B|-|-|-|K|\n"
            + "|-|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|-|-|P|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n");
    domain.addChap(8, 2);
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|K|\n"
            + "|M|-|R|-|B|-|-|-|K|\n"
            + "|-|T|R|-|-|-|-|-|P|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|-|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n");
    domain.toString();
    List<Keys> keys = List.of(new Keys(Color.RED), new Keys(Color.BLUE), new Keys(Color.GREEN), new Keys(Color.YELLOW));
    for (Keys k : keys) {
      k.getname();
      k.getPath();
    }
    for (Treasure t : domain.getTreasures()) { t.getname(); }
  }

  // level 1
  @Test
  public void test00_MoveAgainstWallDoor(){
    Domain domain = setUpLevel1();
    domain.toString();

    domain.move(Direction.Right);
    try {
      domain.move(Direction.Up); // move against door
    } catch (IllegalArgumentException e) {
      System.out.println("catch move against door exception");
    }

    domain.move(Direction.Left);
    domain.move(Direction.Up);
    try {
      domain.move(Direction.Up); // move against wall
    } catch (IllegalArgumentException e) {
      System.out.println("catch move against wall exception");
    }

    assert domain.demoAsString_Domain().equals(
              "|O|X|W|T|\n"
            + "|W|-|P|D|\n"
            + "|W|K|-|-|\n"
            + "|T|D|-|?|\n") : "\n" + domain.demoAsString_Domain();
  }

  @Test
  public void test01_MoveDirection(){
    Domain domain = setUpLevel1();
    domain.move(Direction.Right);
    assert  domain.getChap().getDirection() == Direction.Right;
    domain.move(Direction.Left);
    assert  domain.getChap().getDirection() == Direction.Left;
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|T|\n"
            + "|W|-|K|D|\n"
            + "|W|K|P|-|\n"
            + "|T|D|-|?|\n");
  }

  @Test
  public void test02_pickUpInfo() {
    Domain domain = setUpLevel1();
    assert domain.getTiles()[3][3] instanceof Info_Field;
    domain.move(Direction.Right);
    domain.move(Direction.Down);
    domain.move(Direction.Left);
    assert domain.getTiles()[3][3] instanceof Info_Field;
    assert domain.getChap().getTile().getPoint().equals(new Point(2,3));
  }

  @Test
  public void test03_pickUpKey_useOpenDoor() {
    Domain domain = setUpLevel1();
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getKeys().size() == 2;

    domain.move(Direction.Up);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|T|\n"
            + "|W|-|P|D|\n"
            + "|W|K|-|-|\n"
            + "|T|D|-|?|\n");
    assert domain.getChap().getKeys().size() == 1;
    assert domain.getKeys().size() == 1;

    domain.move(Direction.Right);
    domain.move(Direction.Down);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|T|\n"
            + "|W|-|-|-|\n"
            + "|W|K|-|P|\n"
            + "|T|D|-|?|\n") : "\n"+domain.demoAsString_Domain();
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getKeys().size() == 1;
  }

  @Test
  public void test04_pickUpBothKeys_openDoor() {
    Domain domain = setUpLevel1();
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getKeys().size() == 2;

    domain.move(Direction.Left);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|T|\n"
            + "|W|-|K|D|\n"
            + "|W|P|-|-|\n"
            + "|T|D|-|?|\n");
    assert domain.getChap().getKeys().size() == 1;
    assert domain.getKeys().size() == 1;

    domain.move(Direction.Right);
    domain.move(Direction.Up);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|T|\n"
            + "|W|-|P|D|\n"
            + "|W|-|-|-|\n"
            + "|T|D|-|?|\n");
    assert domain.getChap().getKeys().size() == 2;
    assert domain.getKeys().size() == 0;

    domain.move(Direction.Right);
    domain.move(Direction.Up);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|P|\n"
            + "|W|-|-|-|\n"
            + "|W|-|-|-|\n"
            + "|T|D|-|?|\n") : "\n"+domain.demoAsString_Domain();

    assert domain.getChap().getKeys().size() == 1;
    assert domain.getKeys().size() == 0;
  }

  @Test
  public void test05_pickUpWrongKey_tryOpenDoor() {
    Domain domain = setUpLevel1();

    assert domain.getChap().getKeys().size() == 0;
    assert domain.getKeys().size() == 2;

    domain.move(Direction.Left);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|T|\n"
            + "|W|-|K|D|\n"
            + "|W|P|-|-|\n"
            + "|T|D|-|?|\n");
    domain.move(Direction.Right);
    domain.move(Direction.Right);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|T|\n"
            + "|W|-|K|D|\n"
            + "|W|-|-|P|\n"
            + "|T|D|-|?|\n");

    try {
      domain.move(Direction.Up); // do not move with the wrong key
    } catch (IllegalArgumentException e) {
      System.out.println("catch open door with wrong key exception");
    }

    assert domain.demoAsString_Domain().equals(
              "|O|X|W|T|\n"
            + "|W|-|K|D|\n"
            + "|W|-|-|P|\n"
            + "|T|D|-|?|\n");

    assert domain.getChap().getKeys().size() == 1;
    assert domain.getKeys().size() == 1;
  }

  @Test
  public void test06_pickUpTreasure() {
    Domain domain = setUpLevel1();
    assert domain.getChap().getTreasures().size() == 0;
    assert domain.getTreasures().size() == 2;

    domain.move(Direction.Left);
    domain.move(Direction.Down);
    domain.move(Direction.Left);
    domain.move(Direction.Right);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|T|\n"
            + "|W|-|K|D|\n"
            + "|W|-|-|-|\n"
            + "|-|P|-|?|\n");
    assert domain.getChap().getTreasures().size() == 1;
    assert domain.getTreasures().size() == 1;
  }

  @Test
  public void test07_pickUpAllTreasureToExitLock() {
    Domain domain = setUpLevel1();
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getChap().getTreasures().size() == 0;
    assert domain.getKeys().size() == 2;
    assert domain.getTreasures().size() == 2;

    domain.move(Direction.Up);
    domain.move(Direction.Right);
    domain.move(Direction.Up);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|P|\n"
            + "|W|-|-|-|\n"
            + "|W|K|-|-|\n"
            + "|T|D|-|?|\n");
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getChap().getTreasures().size() == 1;
    assert domain.getKeys().size() == 1;
    assert domain.getTreasures().size() == 1;

    domain.move(Direction.Down);
    domain.move(Direction.Down);
    domain.move(Direction.Left);
    domain.move(Direction.Left);
    domain.move(Direction.Down);
    domain.move(Direction.Left);
    assert domain.demoAsString_Domain().equals(
              "|O|-|W|-|\n"
            + "|W|-|-|-|\n"
            + "|W|-|-|-|\n"
            + "|P|-|-|?|\n") : "\n"+domain.demoAsString_Domain();
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getChap().getTreasures().size() == 2;
    assert domain.getKeys().size() == 0;
    assert domain.getTreasures().size() == 0;

    domain.move(Direction.Right);
    domain.move(Direction.Up);
    domain.move(Direction.Up);
    domain.move(Direction.Up);
    assert domain.demoAsString_Domain().equals(
              "|O|P|W|-|\n"
            + "|W|-|-|-|\n"
            + "|W|-|-|-|\n"
            + "|-|-|-|?|\n") : "\n"+domain.demoAsString_Domain();
  }

  @Test
  public void test08_pickUpPartialTreasureToExitLock() {
    Domain domain = setUpLevel1();
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getChap().getTreasures().size() == 0;
    assert domain.getKeys().size() == 2;
    assert domain.getTreasures().size() == 2;

    domain.move(Direction.Up);
    domain.move(Direction.Right);
    domain.move(Direction.Up);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|P|\n"
            + "|W|-|-|-|\n"
            + "|W|K|-|-|\n"
            + "|T|D|-|?|\n");
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getChap().getTreasures().size() == 1;
    assert domain.getKeys().size() == 1;
    assert domain.getTreasures().size() == 1;

    domain.move(Direction.Down);
    domain.move(Direction.Left);
    domain.move(Direction.Left);
    try {
      domain.move(Direction.Up); // cannot move to exit lock with partial treasure
    } catch(IllegalArgumentException e) {
      System.out.println("catch move to exit lock with partial treasure exception");
    }

    assert domain.demoAsString_Domain().equals(
              "|O|X|W|-|\n"
            + "|W|P|-|-|\n"
            + "|W|K|-|-|\n"
            + "|T|D|-|?|\n");
  }

  @Test
  public void test09_exitNextLevel() {
    Domain domain = setUpLevel1();
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getChap().getTreasures().size() == 0;
    assert domain.getKeys().size() == 2;
    assert domain.getTreasures().size() == 2;

    domain.move(Direction.Up);
    domain.move(Direction.Right);
    domain.move(Direction.Up);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|P|\n"
            + "|W|-|-|-|\n"
            + "|W|K|-|-|\n"
            + "|T|D|-|?|\n");
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getChap().getTreasures().size() == 1;
    assert domain.getKeys().size() == 1;
    assert domain.getTreasures().size() == 1;

    domain.move(Direction.Down);
    domain.move(Direction.Down);
    domain.move(Direction.Left);
    domain.move(Direction.Left);
    domain.move(Direction.Down);
    domain.move(Direction.Left);
    assert domain.demoAsString_Domain().equals(
              "|O|-|W|-|\n"
            + "|W|-|-|-|\n"
            + "|W|-|-|-|\n"
            + "|P|-|-|?|\n") : "\n"+domain.demoAsString_Domain();
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getChap().getTreasures().size() == 2;
    assert domain.getKeys().size() == 0;
    assert domain.getTreasures().size() == 0;

    domain.move(Direction.Right);
    domain.move(Direction.Up);
    domain.move(Direction.Up);
    domain.move(Direction.Up);
    domain.move(Direction.Left);
    assert domain.demoAsString_Domain().equals(
              "|P|-|W|-|\n"
            + "|W|-|-|-|\n"
            + "|W|-|-|-|\n"
            + "|-|-|-|?|\n") : "\n"+domain.demoAsString_Domain();
    assert domain.getLevelId() == 2;
  }

  @Test
  public void test10_testWholeLevel1() {
    Domain domain = setUpLevel1();
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getChap().getTreasures().size() == 0;
    assert domain.getKeys().size() == 2;
    assert domain.getTreasures().size() == 2;

    domain.move(Direction.Left);
    domain.move(Direction.Down);
    domain.move(Direction.Left);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|T|\n"
            + "|W|-|K|D|\n"
            + "|W|-|-|-|\n"
            + "|P|-|-|?|\n");
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getChap().getTreasures().size() == 1;
    assert domain.getKeys().size() == 1;
    assert domain.getTreasures().size() == 1;

    domain.move(Direction.Right);
    domain.move(Direction.Right);
    domain.move(Direction.Right); // info
    domain.move(Direction.Up);
    domain.move(Direction.Left);
    domain.move(Direction.Up);
    domain.move(Direction.Right);
    domain.move(Direction.Up);
    assert domain.demoAsString_Domain().equals(
              "|O|-|W|P|\n"
            + "|W|-|-|-|\n"
            + "|W|-|-|-|\n"
            + "|-|-|-|?|\n");
    assert domain.getChap().getKeys().size() == 0;
    assert domain.getChap().getTreasures().size() == 2;
    assert domain.getKeys().size() == 0;
    assert domain.getTreasures().size() == 0;

    domain.move(Direction.Down);
    domain.move(Direction.Left);
    domain.move(Direction.Left);
    domain.move(Direction.Up);
    domain.move(Direction.Left);
    assert domain.demoAsString_Domain().equals(
              "|P|-|W|-|\n"
            + "|W|-|-|-|\n"
            + "|W|-|-|-|\n"
            + "|-|-|-|?|\n") : "\n"+domain.demoAsString_Domain();
    assert domain.getLevelId() == 2;
  }

  public Domain setUpLevel1(){
    Tile[][] tiles = new Tile[4][4];
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        tiles[i][j] = new FreeTile(new Point(i, j));
      }
    }
    tiles[0][0] = new Exit(new Point(0, 0));
    tiles[0][1] = new WallTile(new Point(0, 1));
    tiles[0][2] = new WallTile(new Point(0, 2));
    tiles[0][3].setItem(new Treasure());
    tiles[1][0] = new ExitLock(new Point(1, 0));
    tiles[1][2].setItem(new Keys(Color.RED));
    tiles[1][3] = new LockDoor(Color.RED, new Point(1, 3));
    tiles[2][0] = new WallTile(new Point(2, 0));
    tiles[2][1].setItem(new Keys(Color.BLUE));
    tiles[3][0].setItem(new Treasure());
    tiles[3][1] = new LockDoor(Color.BLUE, new Point(3, 1));
    tiles[3][3] = new Info_Field("hello", new Point(3, 3));

    Domain domain = new Domain(tiles);
    assert domain.demoAsString_Domain().equals(
              "|O|X|W|T|\n"
            + "|W|-|K|D|\n"
            + "|W|K|P|-|\n"
            + "|T|D|-|?|\n");

    return domain;
  }

  // level 2
  @Test
  public void test00_moveChap() {
    Domain domain = setUpLevel2();
    domain.move(Direction.Right);
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|-|-|-|P|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();
  }

  @Test
  public void test01_pushBox() {
    Domain domain = setUpLevel2();
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|-|-|P|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n");
    domain.move(Direction.Up);
    domain.move(Direction.Left);
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|B|-|-|-|-|\n"
            + "|O|X|B|P|-|-|-|-|-|\n"
            + "|W|W|-|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();
  }

  @Test
  public void test02_pushBoxToWater() {
    Domain domain = setUpLevel2();
    domain.move(Direction.Left);
    domain.move(Direction.Left);
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|P|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();
    domain.move(Direction.Up);
    domain.move(Direction.Up);
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|P|-|-|-|-|-|-|\n"
            + "|O|X|-|T|B|-|-|-|-|\n"
            + "|W|W|-|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();
  }

  @Test
  public void test03_pushBoxAgainstBox() {
    Domain domain = setUpLevel2();
    domain.move(Direction.Up);
    domain.move(Direction.Up);
    domain.move(Direction.Right);
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|B|-|-|-|-|\n"
            + "|O|X|B|T|-|P|-|-|-|\n"
            + "|W|W|-|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();
  }

  @Test
  public void test04_pushBothBoxes() {
    Domain domain = setUpLevel2();
    domain.move(Direction.Up);
    domain.move(Direction.Right);
    domain.move(Direction.Up);
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|B|P|-|-|-|\n"
            + "|O|X|B|T|-|-|-|-|-|\n"
            + "|W|W|-|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();
    domain.move(Direction.Left);
    domain.move(Direction.Up);
    domain.move(Direction.Left);
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|B|-|-|-|-|\n"
            + "|M|R|R|P|-|-|-|-|-|\n"
            + "|-|T|R|B|-|-|-|-|-|\n"
            + "|O|X|B|T|-|-|-|-|-|\n"
            + "|W|W|-|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();
  }

  @Test
  public void test05_pushBoxToExitLock() {
    Domain domain = setUpLevel2();
    domain.move(Direction.Left);
    domain.move(Direction.Up);
    domain.move(Direction.Left);
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|P|B|-|-|-|-|\n"
            + "|W|W|-|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();
  }

  @Test
  public void test06_pushBoxToTreasure() {
    Domain domain = setUpLevel2();
    domain.move(Direction.Right);
    domain.move(Direction.Up);
    domain.move(Direction.Left);
    domain.move(Direction.Down);
    assert domain.getChap().getTreasures().size() == 0;
    assert domain.getTreasures().size() == 2;
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|B|-|-|-|-|-|\n"
            + "|W|W|-|-|P|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();
  }

  @Test
  public void test07_pushBoxToAgainstWall() {
    Domain domain = setUpLevel2();
    domain.move(Direction.Right);
    domain.move(Direction.Up);
    domain.move(Direction.Up);
    domain.move(Direction.Left);
    domain.move(Direction.Down);
    domain.move(Direction.Right);
    domain.move(Direction.Down);
    domain.move(Direction.Left);
    domain.move(Direction.Left);
    domain.move(Direction.Left); // push box against wall
    domain.move(Direction.Down);

    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|-|-|-|-|-|\n"
            + "|W|W|B|-|-|-|-|-|-|\n"
            + "|-|-|-|P|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") :domain.demoAsString_Domain();
  }

  @Test
  public void test08_chapMeetMonster() {
    Domain domain = setUpLevel2();
    domain.move(Direction.Right);
    domain.move(Direction.Down);
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|-|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|P|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();
  }

  @Test
  public void test08_monsterMeetChap() {
    Domain domain = setUpLevel2();
    domain.move(Direction.Right);
    domain.move(Direction.Right);
    domain.move(Direction.Down);
    domain.move(Direction.Down);
    domain.move(Direction.Down);
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|-|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|P|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();

    List<Monster> monsters = domain.getMonsters();
    domain.moveMonster(monsters.get(1));
    domain.moveMonster(monsters.get(1));
    domain.moveMonster(monsters.get(1));
    assert domain.demoAsString_Domain().equals( // Chap dead(Monster above Chap)
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|-|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|P|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();

    domain.move(Direction.Up);
    domain.moveMonster(monsters.get(1));
    assert domain.demoAsString_Domain().equals( // Chap dead(Monster can move, Chap cannot)
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|-|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|W|-|\n"
            + "|-|-|-|-|W|-|P|-|-|\n"
            + "|-|-|-|-|-|-|-|M|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();
  }

  @Test
  public void test09_moveMonsters() {
    Domain domain = setUpLevel2();
    List<Monster> monsters = domain.getMonsters();
    domain.moveAllMonster();
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|-|R|R|-|B|-|-|-|-|\n"
            + "|M|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|-|-|P|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|W|-|\n"
            + "|-|-|-|-|W|M|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n");
    domain.moveMonster(monsters.get(1));
    domain.moveMonster(monsters.get(1));
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|-|R|R|-|B|-|-|-|-|\n"
            + "|M|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|-|-|P|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|M|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n");
    domain.moveMonster(monsters.get(1));
    domain.moveMonster(monsters.get(1));
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|-|R|R|-|B|-|-|-|-|\n"
            + "|M|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|-|-|P|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|W|-|\n"
            + "|-|-|-|-|W|-|-|M|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n");
  }

  @Test
  public void test10_moveToRiver() {
    Domain domain = setUpLevel2();
    domain.move(Direction.Left);
    domain.move(Direction.Up);
    domain.move(Direction.Up);
    try {
      domain.move(Direction.Left); // chap move into water
    } catch (IllegalArgumentException e) {
      System.out.println("catch move into water exception");
    }

    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|P|-|-|-|-|-|-|\n"
            + "|O|X|B|-|B|-|-|-|-|\n"
            + "|W|W|-|-|-|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n") : "\n"+domain.demoAsString_Domain();
  }

  @Test
  public Domain setUpLevel2(){
    Tile[][] tiles = new Tile[9][9];
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        tiles[i][j] = new FreeTile(new Point(i, j));
      }
    }
    tiles[0][3] = new Exit(new Point(0, 3));
    tiles[0][4] = new WallTile(new Point(0, 1));
    tiles[1][0] = new Water(new Point(1, 0));
    tiles[1][1] = new Water(new Point(1, 1));
    tiles[1][2].setItem(new Treasure());
    tiles[1][3] = new ExitLock(new Point(1, 3));
    tiles[1][4] = new WallTile(new Point(1, 4));
    tiles[2][0] = new WallTile(new Point(2, 0));
    tiles[2][1].setItem(new Keys(Color.BLUE));
    tiles[2][0] = new Water(new Point(2, 0));
    tiles[2][1] = new Water(new Point(2, 1));
    tiles[2][2] = new Water(new Point(2, 2));
    tiles[2][3] = new IronBox(new Point(2, 3));
    tiles[3][0] = new IronBox(new Point(3, 0));
    tiles[3][3].setItem(new Treasure());
    tiles[4][1] = new IronBox(new Point(4, 1));
    tiles[4][3] = new IronBox(new Point(4, 3));

    tiles[4][6] = new WallTile(new Point(4, 6));
    tiles[5][8] = new WallTile(new Point(5, 8));
    tiles[7][5] = new WallTile(new Point(7, 5));
    tiles[8][7] = new WallTile(new Point(8, 7));

    Domain domain = new Domain(tiles);
    Monster monster1 = new Monster(new Point(0,1), Direction.Down);
    monster1.setPoint(new Point(0,1));
    Monster monster2 = new Monster(new Point(5,5), Direction.Down);
    monster2.setPoint(new Point(5,5));
    domain.setMonsters(List.of(monster1, monster2));
    assert domain.demoAsString_Domain().equals(
              "|-|R|R|B|-|-|-|-|-|\n"
            + "|M|R|R|-|B|-|-|-|-|\n"
            + "|-|T|R|-|-|-|-|-|-|\n"
            + "|O|X|B|T|B|-|-|-|-|\n"
            + "|W|W|-|-|P|-|-|-|-|\n"
            + "|-|-|-|-|-|M|-|W|-|\n"
            + "|-|-|-|-|W|-|-|-|-|\n"
            + "|-|-|-|-|-|-|-|-|W|\n"
            + "|-|-|-|-|-|W|-|-|-|\n");

    return domain;
  }
}
