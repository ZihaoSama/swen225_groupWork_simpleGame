package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items;

/**
 * @Description: treasure for this game
 * @Author: smx_Morgan
 * @Date: 2022/9/19 15:08
 */
public class Treasure extends Item {

  public Treasure() {
    this.path = "src/main/resources/picture/Treasure.png";
  }

  @Override
  public String toString() {
    return "Treasure";
  }
  @Override
  public String getname(){
    return "treasure";
  }
}
