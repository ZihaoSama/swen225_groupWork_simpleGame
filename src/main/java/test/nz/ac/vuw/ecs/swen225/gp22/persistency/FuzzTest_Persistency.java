package main.java.test.nz.ac.vuw.ecs.swen225.gp22.persistency;

import static main.java.nz.ac.vuw.ecs.swen225.gp22.persistency.gameLoader.levelloader;
import static main.java.nz.ac.vuw.ecs.swen225.gp22.persistency.gameSaver.saveGame;

import main.java.nz.ac.vuw.ecs.swen225.gp22.application.Main;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Domain;
import org.junit.jupiter.api.Test;

/**
 * @Description: Generate input to detect violations of the game load and save
 * @Author: Yiming Liang
 * @Date: 2022/9/20
 */

public class FuzzTest_Persistency {
  @Test
  public void test0_LoadLevel1(){
    Domain domain = null;
    try {
      domain = levelloader("level1");
    } catch (Exception e) {
      e.printStackTrace();
    }

    if(domain != null) {
      assert domain.demoAsString_Persistency().equals(
                "|K|-|?|W|-|-|-|O|\n"
              + "|-|-|D|W|X|W|W|W|\n"
              + "|-|W|-|-|-|D|-|-|\n"
              + "|D|W|-|W|P|K|W|T|\n"
              + "|T|W|-|W|W|-|W|W|\n"
              + "|W|W|-|T|W|-|-|-|\n"
              + "|K|-|-|W|W|?|-|-|\n");
      saveGame(domain, "level1_test");
    }
  }

  @Test
  public void test1_SaveLevel1(){
    Domain domain = null;
    try {
      domain = levelloader("level2");
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (domain != null) {
      assert domain.demoAsString_Persistency().equals(
                "|W|W|W|W|W|W|W|W|W|W|W|W|\n"
              + "|W|T|-|-|-|-|-|-|-|-|T|W|\n"
              + "|W|-|-|-|-|-|-|-|-|-|-|W|\n"
              + "|W|-|-|-|-|R|W|-|-|-|-|W|\n"
              + "|W|M|-|-|-|R|W|-|-|-|-|W|\n"
              + "|W|-|-|-|W|B|W|-|-|-|-|W|\n"
              + "|W|-|-|-|W|-|W|-|-|-|-|W|\n"
              + "|W|-|-|-|W|-|W|-|W|-|-|W|\n"
              + "|W|-|-|W|R|B|R|B|R|-|-|W|\n"
              + "|W|-|-|-|W|W|B|W|-|-|-|W|\n"
              + "|W|-|-|-|W|W|P|W|-|-|-|W|\n"
              + "|W|-|-|W|W|O|X|-|-|-|-|W|\n"
              + "|W|-|-|W|W|W|-|R|B|M|-|W|\n"
              + "|W|-|-|-|-|-|-|-|-|-|-|W|\n"
              + "|W|T|-|-|-|-|-|-|-|-|T|W|\n"
              + "|W|W|W|W|W|W|W|W|W|W|W|W|\n");
      saveGame(domain, "level2_test");
    }
  }
}
