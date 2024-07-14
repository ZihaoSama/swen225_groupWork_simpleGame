package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles;

import java.awt.*;

/**
 * @Description: water for game
 * @Author: smx_Morgan
 * @Date: 2022/9/19 11:26
 */
public class Water  extends Tile {

  public Water(Point point) {
    this.walkable = true;
    this.haveAction =true;
    this.point = point;
    this.path = "src/main/resources/picture/water.gif";
  }
  @Override
  public String getname() {
    return "water";
  }
}
