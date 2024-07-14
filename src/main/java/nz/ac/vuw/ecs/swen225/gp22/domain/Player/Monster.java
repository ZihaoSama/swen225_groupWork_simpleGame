package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player;

import java.awt.Point;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Domain;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.otherInfo.Direction;

/**
 * @Description: Monster of this game
 * @Author: smx_Morgan
 * @Date: 2022/9/28 20:55
 */
public class Monster {
  private Point point;
  private String path;
  Direction dir;
  public Monster(Point point,Direction dir) {
    this.point = point;
    this.dir = dir;
    //save monster picture
    this.path = "src/main/resources/picture/Monster/Zombie.gif";
  }

  public void setPoint(Point point) {
    this.point = point;
  }

  public Point getPoint() {
    return point;
  }

  public Direction getDir() {
    return dir;
  }

  public void setDir(Direction dir) {
    this.dir = dir;
  }

  public String getPath() {
    return path;
  }
}
