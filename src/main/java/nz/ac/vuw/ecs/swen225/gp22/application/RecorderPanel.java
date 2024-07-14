package main.java.nz.ac.vuw.ecs.swen225.gp22.application;

import main.java.nz.ac.vuw.ecs.swen225.gp22.Recorder.Recorder;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Domain;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.otherInfo.Direction;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import javax.swing.*;

import static main.java.nz.ac.vuw.ecs.swen225.gp22.persistency.gameLoader.chap;

/**
 * Created with IntelliJ IDEA. Description: User: Yusi Cheng Date: 2022-10-12 Time: 2:10
 */
public class RecorderPanel extends JPanel {
  private static HashMap<Integer, Direction> moves = new HashMap<Integer, Direction>();
  private static int index = 0;
  private JButton leftButton;
  private JButton autoButton;
  private JButton rightButton;
  private JButton slowerButton;
  private JButton standardButton;
  private JButton fasterButton;
  private JButton selectButton;
  private AppView appView;
  private static int replaySpeed = 1000;
  private JFileChooser fileChooser;
  public RecorderPanel(AppView appView) {
    this.appView = appView;
    leftButton = new JButton("<");
    autoButton = new JButton("AUTO");
    rightButton = new JButton(">");
    slowerButton = new JButton("SLOWER");
    standardButton = new JButton("STANDARD");
    fasterButton = new JButton("FASTER");
    selectButton = new JButton("Select");
    selectButton.setVisible(true);
    leftButton.setVisible(true);
    autoButton.setVisible(true);
    rightButton.setVisible(true);
    slowerButton.setVisible(true);
    standardButton.setVisible(true);
    fasterButton.setVisible(true);
    this.setPreferredSize(new Dimension(600, 45));
    this.add(selectButton);
    this.add(leftButton);
    this.add(autoButton);
    this.add(rightButton);
    this.add(slowerButton);
    this.add(standardButton);
    this.add(fasterButton);
    setBackground(new Color(132, 166, 231));
    this.setVisible(false);
    this.addAction();
  }

  public void showPanel() {
    this.setVisible(true);
  }

  public void hidePanel() {
    this.setVisible(false);
  }

  public void addAction() {
    leftButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        backward(appView.getDomain());
      }
    });



    autoButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < moves.size(); i++) {
          forward(appView.getDomain());

          Delay(replaySpeed);
        }
      }
    });

    rightButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        forward(appView.getDomain());
      }
    });

    slowerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (replaySpeed > 500) {
          replaySpeed -= 500;
        }
      }
    });

    standardButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });
    selectButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(appView.getMainWindow());
        File file = fileChooser.getSelectedFile();
        System.out.println(file);
        moves = Recorder.Quickload(file);

      }
    });

    fasterButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (replaySpeed <= 10000) {
          replaySpeed += 500;
        }
      }
    });
  }

  private void backward(Domain game) {
    if( index>=0 &&!moves.isEmpty()) {

      game.move(oppositeDirection(moves.get(index)));
      index--;

    } else if(moves.isEmpty()) {
      System.out.println("U have to record first");
    }

  }

  private void forward(Domain game) {
    if( index<moves.size() &&!moves.isEmpty()) {

       game.move(moves.get(index));

            index++;

    } else if(moves.isEmpty()) {
      System.out.println("U have to record first");
    }

  }
  private Direction oppositeDirection(Direction currentdir){
    if(currentdir.equals(Direction.Up)){
      return Direction.Down;
    }
    if(currentdir.equals(Direction.Down)){
      return Direction.Up;
    }
    if(currentdir.equals(Direction.Right)){
      return Direction.Left;
    }
    return Direction.Right;
  }

  static void Delay(int ms) {
    Long waitTime = System.currentTimeMillis() + ms;
    while (System.currentTimeMillis() < waitTime) {
      //do nothing
    }
  }
}
