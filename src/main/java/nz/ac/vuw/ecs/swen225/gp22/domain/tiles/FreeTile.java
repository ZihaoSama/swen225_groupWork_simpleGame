package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles;

import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Item;

import java.awt.*;

/**
 * @Description: free tiles
 * @Author: smx_Morgan
 * @Date: 2022/9/19 11:22
 */
public class FreeTile  extends Tile {
//  public FreeTile() {
//    this.walkable = true;
//    this.haveAction =false;
//    this.path = "src/main/resources/picture/Floor.png";
//  }

  public FreeTile(Point point) {
    this.point = point;
    this.walkable = true;
    this.haveAction =false;
    this.path = "src/main/resources/picture/Floor.png";
  }
//  public FreeTile(Item item){
//    this.item = item;
//    this.walkable = true;
//    this.haveAction =false;
//    this.path = "src/main/resources/picture/Floor.png";
//  }

  @Override
  public String toString() {
    if(this.containItem()){
      return item.toString();
    }
    return "Free Tile{" + super.toString();
  }
  @Override
  public String getname() {
    return "FreeTile";
  }
}
