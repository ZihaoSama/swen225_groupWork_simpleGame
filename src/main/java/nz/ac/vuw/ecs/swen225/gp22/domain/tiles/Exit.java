package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles;

import java.awt.Point;

/**
 * @Description: chap can move to next level
 * @Author: smx_Morgan
 * @Date: 2022/9/19 11:24
 */
public class Exit  extends Tile {

  public Exit(Point point) {
    this.point = point;
    this.walkable = true;
    this.haveAction =true;
    this.path = "src/main/resources/picture/Exit.png";
  }

  @Override
  public String toString() {
    return "Exit{"+ super.toString() + "}";
  }
  @Override
  public String getname() {
    return "Exit";
  }
}
