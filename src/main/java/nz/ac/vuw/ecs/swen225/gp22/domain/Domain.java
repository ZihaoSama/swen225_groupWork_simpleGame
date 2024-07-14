package main.java.nz.ac.vuw.ecs.swen225.gp22.domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Chap;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Monster;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.graphAction.AtExit;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.graphAction.Gameover;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.graphAction.ShowInfo;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds.ExitSounds;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds.GetInform;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds.HitWall;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds.Walk;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds.UnlockDoor;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Item;
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

/**
 * @Description: domain class is used to run the game.
 * @Author: smx_Morgan
 * @Date: 2022/9/19 11:06
 */
public class Domain {
  public Domain() {
  }
  //basic information
  public static HashMap<Integer, Direction> moves = new HashMap<>();
  public int index = 0;
  public boolean recording = false;
  public Tile[][] tiles;
  private int width;
  private int height;
  public Chap chap;
  private List<Keys> keys = new ArrayList<>();
  public List<Treasure> treasures = new ArrayList<>();
  public List<Monster> monsters = new ArrayList<>();

  public List<Monster> getMonsters() {
    return monsters;
  }

  public void setMonsters(List<Monster> monsters) {
    this.monsters = monsters;
  }

  private Tile exitLock;
  private Tile current;
  //logic
  private int levelId = 1;

  private Timer timer;
  public Domain(Tile[][] tiles) {
    this.tiles = tiles;
    this.width = tiles.length;
    this.height = tiles[0].length;
    current = tiles[width/2][height/2];
    chap = new Chap();
    chap.setPoint(new Point(width/2,height/2 ));
    chap.setTile(current);
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++){
        if(tiles[i][j] != null){
          if (tiles[i][j].containItem()){
            addItem(tiles[i][j].item);
          }
          if (tiles[i][j] instanceof ExitLock){
            exitLock = (ExitLock) tiles[i][j];
          }
        }
      }
    }
  }
  public void addItem(Item item){
    if(item instanceof Keys){
      keys.add((Keys) item);
    }
    if(item instanceof Treasure){
      treasures.add((Treasure) item);
    }
  }
  public Tile[][] getTiles() {
    return tiles;
  }
  public int getWidth() {
    return width;
  }
  public int getHeight() {
    return height;
  }
  public int getLevelId() {
    return levelId;
  }

  /**
   * move the chap
   * @param dir
   */
  public void move(Direction dir){
    if(dir != chap.getDirection()){
      chap.setDirection(dir);//set chap to face this direction.
    }
    Tile next = tileTo(chap.getTile(), dir);
    if(checkNext(next,dir)){
      Walk.playSounds();
      getItem(next);
      chap.setTile(next);
      chap.setPoint(next.getPoint());
      if(next instanceof Water){
        chap.fallWater();
        chap.killed();
        Gameover.over();
      }
      if(monsters.size()>0){
        for (Monster monster:monsters) {
          if(monster.getPoint().equals(chap.getPoint())){
            chap.crackMonster();
            chap.killed();
            Gameover.over();
          }
        }
      }
      //here is where the sounds play
//      Walk.playSounds();
    }
    if(next.isHaveAction()){
      tileAction(next);
    }
    if(recording) {
      moves.put(index, dir);
      System.out.println(moves);
      index++;
    }

  }
  public boolean checkNext(Tile tile,Direction dir){

    if(tile.isWalkable()){
      return true;
    }else{
      if(tile instanceof LockDoor){
        Keys key = chap.matchKey((LockDoor) tile);
        if(key != null){
          UnlockDoor.playSounds();
          chap.unPickup(key);
          this.tiles[tile.getPoint().x][tile.getPoint().y] = new FreeTile(tile.getPoint());
          return true;
        }
      }
      if(tile instanceof IronBox){
        Tile next_i = tileTo(tile,dir);
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
    throw new IllegalArgumentException("chap should not move into next tile"); //enforce preconditions
    //return false;
  }

  public void getItem(Tile tile){
    if(tile.containItem()){
      int collectedTreasureCount = chap.getTreasures().size();
      int collectedKeyCount = chap.getKeys().size();
      Item item = tile.getItem();
      chap.pickup(item);
      // Java asserts for postconditions checks here
      if(item instanceof Treasure){
        int collectedTreasureCount2 = chap.getTreasures().size();
        assert collectedTreasureCount2 == collectedTreasureCount+1;
      }
      if(item instanceof Keys){
        int collectedKeyCount2 = chap.getKeys().size();
        assert collectedKeyCount2 == collectedKeyCount+1;
      }
      //pick up sounds();
//      PickUp.playSounds();
      removeFormDomain(tile);
    }else{
//      System.out.println("there is no treasure here");
    }
  }
  public void removeFormDomain(Tile tile){
    if(tile.getItem() instanceof Keys){
      this.keys.remove(tile.getItem());
      tile.removeItem();
    }
    if(tile.getItem() instanceof Treasure){
      this.treasures.remove(tile.getItem());
      tile.removeItem();
      if(treasures.isEmpty()){
        exitLock = new FreeTile(exitLock.getPoint());
        tiles[exitLock.getPoint().x][exitLock.getPoint().y] = exitLock;
      }
    }
  }
  public void tileAction(Tile tile){
    if(tile instanceof Info_Field){
      //actions if the player at info_Field
      ShowInfo.showInfo();
      GetInform.playSounds();
      System.out.println(((Info_Field) tile).getInfo());
    }
    if(tile instanceof Exit){
      //action if player pass this level
//      ExitSounds.playSounds();
      AtExit.exit();
      levelId += 1;
      ExitSounds.playSounds();
      moveToNextLevel(levelId);
    }
  }
  public void moveToNextLevel(int levelId){

  }
  public Tile tileTo(Tile t, Direction d) {
    switch (d) {
      case Up:
        if(t.getPoint().y <= 0){
          HitWall.playSounds();
          return t;
        }
        return tiles[t.getPoint().x][t.getPoint().y - 1];
      case Down:
        if(t.getPoint().y >= height-1){
          HitWall.playSounds();
          return t;
        }
        return tiles[t.getPoint().x][t.getPoint().y + 1];
      case Left:
        if(t.getPoint().x <= 0){
          HitWall.playSounds();
          return t;
        }
        return tiles[t.getPoint().x - 1][t.getPoint().y];
      case Right:
        if(t.getPoint().x >= width-1){
          HitWall.playSounds();
          return t;
        }
        return tiles[t.getPoint().x + 1][t.getPoint().y];
      default:
        return null;
    }
  }

  public Chap getChap(){
    return this.chap;
  }

  public List<Keys> getKeys(){
    return this.keys;
  }

  public List<Treasure> getTreasures(){
    return this.treasures;
  }

  public int numTreasures() {
    return treasures.size();
  }

  public String toString() {
    String s = "Domain={\n" + "tiles=";
    for (Tile[] x : tiles) {
      for (Tile y : x) {
        s = s + y.toString() + "---";
      }
      s = s + "\n";
    }
    return s +
        "chap=" + chap +
        ", \nkeys=" + keys +
        ", \ntreasures=" + treasures +
        ", \nexitLock=" + exitLock +
        ", \ncurrent=" + current +
        ", \nwidth=" + width +
        ", height=" + height +
        ", levelId=" + levelId +
        ", timer=" + timer +
        "}";
  }

  public String demoAsString_Domain(){
    String s = "";
    for (int i = 0; i < tiles.length; i++){
//       s = s + "---------\n";
       for (int j = 0; j < tiles[0].length; j++){
         String symbol = "-";
         // use tiles[j][i] instead of tile[i][j]
         // because of index's difference between 2D array and Point
         if (tiles[j][i].equals(chap.getTile())) {
           symbol = "P";
         }
         else if (tiles[j][i] instanceof Exit) {
           symbol = "O";
         }
         else if (tiles[j][i] instanceof ExitLock) {
           symbol = "X";
         }
         else if (tiles[j][i] instanceof LockDoor) {
           symbol = "D";
         }
         else if (tiles[j][i] instanceof WallTile) {
           symbol = "W";
         }
         else if (tiles[j][i] instanceof Info_Field) {
           symbol = "?";
         }
         else if (tiles[j][i] instanceof IronBox) {
           symbol = "B";
         }
         else if (tiles[j][i] instanceof Water) {
           symbol = "R";
         }
         else if (tiles[j][i].getItem() instanceof Keys) {
           symbol = "K";
         }
         else if (tiles[j][i].getItem() instanceof Treasure) {
           symbol = "T";
         }
         else {
           for (int k = 0; k < monsters.size(); k++) {
             if (tiles[j][i].getPoint().equals(monsters.get(k).getPoint())) {
               symbol = "M";
             }
           }
         }
        s = s + "|" + symbol;
      }
      s += "|\n";
    }
    return s;
  }

  public String demoAsString_Persistency(){
    String s = "";
    for (int i = 0; i < tiles[0].length; i++){
//       s = s + "---------\n";
      for (int j = 0; j < tiles.length; j++){
        String symbol = "-";
        // use tiles[j][i] instead of tile[i][j]
        // because of index's difference between 2D array and Point
        if (tiles[j][i].equals(chap.getTile())) {
          symbol = "P";
        }
        else if (tiles[j][i] instanceof Exit) {
          symbol = "O";
        }
        else if (tiles[j][i] instanceof ExitLock) {
          symbol = "X";
        }
        else if (tiles[j][i] instanceof LockDoor) {
          symbol = "D";
        }
        else if (tiles[j][i] instanceof WallTile) {
          symbol = "W";
        }
        else if (tiles[j][i] instanceof Info_Field) {
          symbol = "?";
        }
        else if (tiles[j][i] instanceof IronBox) {
          symbol = "B";
        }
        else if (tiles[j][i] instanceof Water) {
          symbol = "R";
        }
        else if (tiles[j][i].getItem() instanceof Keys) {
          symbol = "K";
        }
        else if (tiles[j][i].getItem() instanceof Treasure) {
          symbol = "T";
        }
        else {
          for (int k = 0; k < monsters.size(); k++) {
            if (tiles[j][i].getPoint().equals(monsters.get(k).getPoint())) {
              symbol = "M";
            }
          }
        }
        s = s + "|" + symbol;
      }
      s += "|\n";
    }
    return s;
  }

  public void moveAllMonster(){
    for (Monster monster:this.monsters) {
      moveMonster(monster);
    }
  }
  public void moveMonster(Monster monster){
    Point pos  = monster.getPoint();
    Tile next = tileTo(tiles[pos.x][pos.y],monster.getDir());

    if(next.isWalkable() && !(next instanceof Water)){
      monster.setPoint(next.getPoint());
      if(monster.getPoint().equals(chap.getPoint())){
        chap.crackMonster();
        chap.killed();
        Gameover.over();
      }
    }else{
      if(monster.getDir().equals(Direction.Up)){
        monster.setDir(Direction.Left);
      }else if (monster.getDir().equals(Direction.Left)){
        monster.setDir(Direction.Down);
      }else if (monster.getDir().equals(Direction.Down)){
        monster.setDir(Direction.Right);
      }else if (monster.getDir().equals(Direction.Right)){
        monster.setDir(Direction.Up);
      }
      moveMonster(monster);
    }

  }

  public void swapTile(Tile tile) {
    tiles[tile.getPoint().x][tile.getPoint().y] = tile;
  }

  public void addChap(int x, int y) {
    Tile temp = tiles[x][y];
    chap.setTile(temp);
  }
  public void setChap(Chap chap) {
    Point pos = chap.getPoint();

    this.chap = chap;
    if(pos != null){
      current = tiles[pos.x][pos.y];
      chap.setTile(current);
    }else{
      throw new IllegalStateException("chap need have pos");
    }
  }
}
