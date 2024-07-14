package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds;

import main.java.nz.ac.vuw.ecs.swen225.gp22.renderer.SoundEffect;

/**
 * @Description: unlock
 * @Author: smx_Morgan
 * @Date: 2022/9/21 10:16
 */
public class UnlockDoor {

  public static void playSounds(){
    SoundEffect.DOOR_OPEN.play();
  }

}
