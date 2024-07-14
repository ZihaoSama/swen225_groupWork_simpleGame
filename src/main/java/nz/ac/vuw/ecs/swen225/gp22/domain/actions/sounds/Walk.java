package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds;

import main.java.nz.ac.vuw.ecs.swen225.gp22.renderer.SoundEffect;

/**
 * @Description: walkSound
 * @Author: smx_Morgan
 * @Date: 2022/9/21 10:15
 */
public class Walk {
  public static void playSounds(){
    SoundEffect.STEP1.play();
    SoundEffect.STEP2.play();
    SoundEffect.STEP3.play();
    SoundEffect.STEP4.play();
    SoundEffect.STEP5.play();
  }

}
