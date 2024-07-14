package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles;

import java.awt.Point;

/**
 * @Description: IronBox for water
 * @Author: smx_Morgan
 * @Date: 2022/9/28 21:23
 */
public class IronBox extends Tile{

  public IronBox(Point point) {
    this.point = point;
    this.walkable = false;
    this.haveAction =false;
    this.path = "src/main/resources/picture/iron_box.png";
  }
  @Override
  public String getname() {
    return "iron";
  }
}
