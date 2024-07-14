package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles;

import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Item;

import java.awt.*;
import java.util.Collection;

/**
 * @Description: the tiles of game
 * @Author: smx_Morgan
 * @Date: 2022/9/12 10:59
 */
public abstract class Tile {
  public Item item = null;
  protected Point point;
  protected boolean walkable;
  protected boolean haveAction;
  //save picture path
  protected String path;

  public boolean isHaveAction() {
    return haveAction;
  }

  @Override
  public String toString() {
    return
            "point=" + point +
                    ", item=" + item;
  }

  public boolean containItem() {
    if (item != null) {
      return true;
    }
    return false;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public void removeItem() {
    this.item = null;
  }

  public void setPoint(Point point) {
    this.point = point;
  }

  public Point getPoint() {
    return point;
  }

  public boolean isWalkable() {
    return walkable;
  }

  public String getPath() {
    return path;
  }


  public abstract String getname();
}
