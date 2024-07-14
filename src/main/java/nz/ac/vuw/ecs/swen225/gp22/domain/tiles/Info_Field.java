package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles;

import java.awt.Point;

/**
 * @Description: info_fild for game
 * @Author: smx_Morgan
 * @Date: 2022/9/19 20:46
 */
public class Info_Field extends Tile {
  String info;

  public Info_Field() {
    this.path = "src/main/resources/picture/Info.png";
  }

  public Info_Field(String info, Point point) {
    this.point = point;
    this.info = info;
    this.walkable = true;
    this.haveAction =true;
    this.path = "src/main/resources/picture/Info.png";
  }

  public String getInfo() {
    return info;
  }

//  public void setInfo(String info) {
//    this.info = info;
//  }
//  public void showInfo(){
//    System.out.println(info);
//  }

  @Override
  public String toString() {
    return "Info field{" + super.toString();
  }
  @Override
  public String getname() {
    return "Info_field";
  }
}
