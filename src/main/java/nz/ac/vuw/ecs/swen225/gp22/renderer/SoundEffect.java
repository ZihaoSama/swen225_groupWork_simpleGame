package main.java.nz.ac.vuw.ecs.swen225.gp22.renderer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
/**
 * @program: GroupWork
 * @description: sound implement: SoundEffect handles the game's SFX.
 * Classes that want to use SFX will call the static variables in this enum and
 * play them via the play() method.
 * @author: ZihaoSama
 * @create: 2022-09-25 01:46
 **/
public enum SoundEffect {
    DEATH("src/main/resources/sounds/Death.wav"),
    DOOR_OPEN("src/main/resources/sounds/Door_open.wav"),
    HIT_WALL("src/main/resources/sounds/Hit_wall.wav"),
    PICK_UP("src/main/resources/sounds/Pick_up.wav"),
    VICTORY("src/main/resources/sounds/Victory.wav"),
    STEP1("src/main/resources/sounds/MoveSounds/step1.wav"),
    STEP2("src/main/resources/sounds/MoveSounds/step2.wav"),
    STEP3("src/main/resources/sounds/MoveSounds/step3.wav"),
    STEP4("src/main/resources/sounds/MoveSounds/step4.wav"),
    STEP5("src/main/resources/sounds/MoveSounds/step5.wav"),
    ;

    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static final Volume volume = Volume.LOW;

    private Clip clip;

    SoundEffect(String soundFileName) {
        // sets the sound effect
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFileName));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        // plays the sound effect
        if (volume != Volume.MUTE) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }
}
