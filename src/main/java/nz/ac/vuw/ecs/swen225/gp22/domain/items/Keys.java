package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items;

import java.awt.Color;

import static java.awt.Color.*;

/**
 * @Description: keys for game
 * @Author: smx_Morgan
 * @Date: 2022/9/19 15:07
 */
public class Keys extends Item {
  private Color colour;

  public Keys(Color colour) {
    //save image address
    if (colour.equals(BLUE)) {
      this.path = "src/main/resources/picture/key/blue_key.png";
    } else if (colour.equals(GREEN)) {
      this.path = "src/main/resources/picture/key/green_key.png";
    } else if (colour.equals(RED)) {
      this.path = "src/main/resources/picture/key/red_key.png";
    } else if (colour.equals(YELLOW)) {
      this.path = "src/main/resources/picture/key/yellow_key.png";
    }
    this.colour = colour;
  }

  public Color getColour() {
    return colour;
  }

  @Override
  public String toString() {
    if(this.colour.equals(BLUE)){
      return "Blue Key";
    }
    if(this.colour.equals(RED)){
      return "Red Key";
    }
    if(this.colour.equals(GREEN)){
      return "Green Key";
    }
    if(this.colour.equals(Color.YELLOW)){
      return "Yellow Key";
    }
    return "Key";
  }
  public String getname(){
    if(this.colour.equals(BLUE)){
      return "bluekey";
    }
    if(this.colour.equals(RED)){
      return "redkey";
    }
    if(this.colour.equals(GREEN)){
      return "greenkey";
    }
    if(this.colour.equals(Color.YELLOW)){
      return "yellowkey";
    }
    return null;
  }
}
