package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds;

import main.java.nz.ac.vuw.ecs.swen225.gp22.renderer.SoundEffect;

/**
 * @Description: pick up items
 * @Author: smx_Morgan
 * @Date: 2022/9/21 10:18
 */
public class PickUp {
  public static void playSounds(){
    SoundEffect.PICK_UP.play();
  }
}
