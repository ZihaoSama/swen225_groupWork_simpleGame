package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.actions.sounds;

import main.java.nz.ac.vuw.ecs.swen225.gp22.renderer.SoundEffect;

/**
 * @program: group-work
 * @description: sound of death
 * @author: ZihaoSama
 * @create: 2022-09-27 22:28
 **/
public class DeathSounds {
    public static void playSounds(){
        SoundEffect.DEATH.play();
    }
}
