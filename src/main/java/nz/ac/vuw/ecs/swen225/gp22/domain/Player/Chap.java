package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player;

import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds.DeathSounds;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds.PickUp;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Item;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Keys;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Treasure;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.otherInfo.Direction;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.LockDoor;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: player
 * @Author: smx_Morgan
 * @Date: 2022/9/19 15:37
 */
public class Chap {
  private String path;
  public boolean live =true;
  public Chap() {
    //save player picture
    this.path = "src/main/resources/picture/Person/PlayerLeftWalk.gif";
  }
  private List<Keys> keys = new ArrayList<>();
  private List<Treasure> treasures = new ArrayList<>();
  //the orientation of chap (chap的人脸朝向)
  private Direction direction;
  private Point point;
  private Tile tile;

  public List<Keys> getKeys() {
    return keys;
  }

  public List<Treasure> getTreasures() {
    return treasures;
  }
  public void addKeys(Keys key){keys.add(key);}
  public void addTreasure(Treasure treasure){treasures.add(treasure);}

  public void pickup(Item p) {
    PickUp.playSounds();
    if (p instanceof Treasure) {
      treasures.add((Treasure) p);
    } else if (p instanceof Keys) {
      keys.add((Keys) p);
    }
  }
  public void unPickup(Item p){
     if (p instanceof Keys) {
      keys.remove((Keys) p);
    }
  }

  public Point getPoint() {
    return point;
  }

  public void setPoint(Point point) {
    this.point = point;
  }

  public Keys matchKey(LockDoor door){
    for (Keys key:keys) {
      if(door.getColour().equals(key.getColour())){
        return key;
      }
    }
    return null;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public Tile getTile() {
    return tile;
  }

  public void setTile(Tile tile) {
    this.tile = tile;
  }

  @Override
  public String toString() {
    return "Chap{" +
        "tile=" + tile.toString() +
        ", keys=" + keys +
        ", treasures=" + treasures +
        ", direction=" + direction +
        '}';
  }

  //get picture path
  public String getPath() {
    if(isLive()){
      if (direction == Direction.Left) {
        this.path = "src/main/resources/picture/Person/PlayerLeftWalk.gif";
      } else if (direction == Direction.Right) {
        this.path = "src/main/resources/picture/Person/PlayerRightWalk.gif";
      }
    }
    return this.path;
  }

  public void killed() {
    DeathSounds.playSounds();
    this.live = false;
  }

  public void crackMonster(){
    this.path = "src/main/resources/picture/Person/PlayerDeath.gif";
  }

  public void fallWater(){
    this.path = "src/main/resources/picture/Person/Splash.gif";
  }

  public boolean isLive() {
    return live;
  }
}

