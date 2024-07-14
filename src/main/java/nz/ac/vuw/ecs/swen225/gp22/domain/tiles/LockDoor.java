package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles;

import java.awt.Color;
import java.awt.Point;

import static java.awt.Color.*;

/**
 * @Description: locked door
 * @Author: smx_Morgan
 * @Date: 2022/9/19 14:32
 */
public class  LockDoor extends Tile {
  Color colour;
  public LockDoor(Color colour, Point point) {
    this.point = point;
    this.colour = colour;
    this.walkable = false;
    this.haveAction =false;
    if(colour.equals(GREEN)) {
      this.path = "src/main/resources/picture/Door/green_door.png";
    } else if(colour.equals(RED)) {
      this.path = "src/main/resources/picture/Door/red_door.png";
    } else if(colour.equals(YELLOW)) {
      this.path = "src/main/resources/picture/Door/yellow_door.png";
    } else if(colour.equals(BLUE)) {
      this.path = "src/main/resources/picture/Door/blue_door.png";
    }
  }

  public Color getColour() {
    return colour;
  }

  @Override
  public String toString() {
    if(this.colour.equals(Color.BLUE)){
      return "Blue LockDoor {" + super.toString();
    }
    if(this.colour.equals(RED)){
      return "Red LockDoor {" + super.toString();
    }
    if(this.colour.equals(GREEN)){
      return "Green LockDoor {" + super.toString();
    }
    if(this.colour.equals(YELLOW)){
      return "Yellow LockDoor {" + super.toString();
    }
    return "Door";
  }
  @Override
  public String getname() {
    return "LockDoor";
  }
}
