package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles;

import java.awt.Point;

/**
 * @Description: wall for game, can move in
 * @Author: smx_Morgan
 * @Date: 2022/9/19 11:21
 */
public class WallTile  extends Tile {

  public WallTile(Point point) {
    this.point = point;
    this.walkable = false;
    this.haveAction =false;
    this.path = "src/main/resources/picture/Wall.png";
  }

  @Override
  public String toString() {
    return "Wall Tile{"+ super.toString() + "}";
  }
  @Override
  public String getname() {
    return "WallTile";
  }
}

