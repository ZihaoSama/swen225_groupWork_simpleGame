package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles;

import java.awt.Point;

/**
 * @Description: exitLock for game
 * @Author: smx_Morgan
 * @Date: 2022/9/19 20:51
 */
public class ExitLock extends Tile {

  public ExitLock(Point point) {
    this.point = point;
    this.walkable = false;
    this.haveAction =false;
    this.path = "src/main/resources/picture/Exit_locker.png";
  }

  @Override
  public String toString() {
    return "Exit Lock{" + super.toString();
  }
  @Override
  public String getname() {
    return "Exitlock";
  }
}
