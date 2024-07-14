package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds;

import main.java.nz.ac.vuw.ecs.swen225.gp22.renderer.SoundEffect;

/**
 * @Description: when move to exitfield
 * @Author: smx_Morgan
 * @Date: 2022/9/21 10:16
 */
public class ExitSounds {
  public static void playSounds(){
    SoundEffect.VICTORY.play();
  }
}
