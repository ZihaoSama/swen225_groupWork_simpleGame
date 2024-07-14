package main.java.nz.ac.vuw.ecs.swen225.gp22.application;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import main.java.nz.ac.vuw.ecs.swen225.gp22.Recorder.Recorder;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Domain;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Chap;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.otherInfo.Direction;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.Exit;
import main.java.nz.ac.vuw.ecs.swen225.gp22.persistency.gameLoader;
import main.java.nz.ac.vuw.ecs.swen225.gp22.persistency.gameSaver;
import main.java.nz.ac.vuw.ecs.swen225.gp22.renderer.GamePane;


/**
 * Created with IntelliJ IDEA. Description: User: Yusi Cheng Date: 2022-09-21 Time: 3:33
 */
public class AppView {
  //main panel and menu
  private final Main game;
  private JFrame window;

  private boolean isPaused = false;
  private GamePane gamePane;
  private HelpView helpView;
  private final JMenu file = new JMenu("File");
  private final JMenu level = new JMenu("Level");
  private final JMenu recorder = new JMenu("Recorder");
  private final JMenu help = new JMenu("Help");
  private final JMenuItem saveGame = new JMenuItem("Save Game");
  private final JMenuItem loadGame = new JMenuItem("Load Game");
  private final JMenuItem level1 = new JMenuItem("new game at level 1");
  private final JMenuItem level2 = new JMenuItem("new game at level 2");
  private final JMenuItem start_recording = new JMenuItem("Start recording");
  private final JMenuItem stop_recording = new JMenuItem("Stop recording");
  private final JMenuItem load_recording = new JMenuItem("Load recording");
  private final JMenuItem viewHelp = new JMenuItem("View Help");

  private String[] levelNames = {"level1", "level2"};
  private int currentLevel = 1;
  private JPanel mainWindow = new JPanel();

  //InfoPanel
  private InfoPanel infoPanel;
  private Domain domain;
  private RecorderPanel recorderPanel;


  /**
   * Initializes the game program window.
   *
   * @param game     - Main program
   * @param isReplay - True if the loaded file is a replay, false if it is not.
   */
  public AppView(Main game,boolean isReplay) {
    this.game = game;
    try {
      this.domain = gameLoader.levelloader(levelNames[currentLevel - 1]);
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.makeWindow();
    this.mainWindow.requestFocus();
    this.mainWindow.requestFocusInWindow();
    this.mainWindow.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        // Keyboard listening for movement
        int keyCode = e.getExtendedKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
          isPaused = !isPaused;
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
          JOptionPane.showMessageDialog(null, "The game will continue!");
          isPaused = !isPaused;
          infoPanel.startTimer();
        }
        //System.out.println(keyCode);
        if (isPaused) {
          infoPanel.stopTimer();
          JOptionPane.showMessageDialog(null, "The game is paused, please click the Esc key again to continue!");
          return;
        }
        infoPanel.startTimer();
        if (keyCode == KeyEvent.VK_UP) {
          playerMovement(Direction.Up, false);
        } else if (keyCode == KeyEvent.VK_DOWN) {
          playerMovement(Direction.Down, false);
        } else if (keyCode == KeyEvent.VK_LEFT) {
          playerMovement(Direction.Left, false);
        } else if (keyCode == KeyEvent.VK_RIGHT) {
          playerMovement(Direction.Right, false);
        }
      }
    });
  }

  public Domain getDomain() {
    return domain;
  }

  public JFrame getWindow() {
    return window;
  }

  public void restartGame(){
    try {
      this.infoPanel.stopTimer();
      this.infoPanel.resetTimer();
      this.domain = gameLoader.levelloader(levelNames[currentLevel - 1]);
      this.gamePane.update(this.domain);
      this.gamePane.repaint();
      this.infoPanel.repaint();;
      this.infoPanel.setDomain(this.domain);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Construct a frame that will display the main game.
   */
  private void makeWindow() {
    this.window = new JFrame("Chap's Challenge");
//    this.window.setPreferredSize(new Dimension(1000, 800));
//    this.window.setLayout(null);
    this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.window.setResizable(false);
    this.window.getRootPane().getActionMap().put("close", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        window.dispose();
      }
    });
    this.window.getRootPane().getActionMap().put("saveAndClose", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        saveGame();
        window.dispose();
      }
    });
    this.window.getRootPane().getActionMap().put("loadGame", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        loadSave();
      }
    });


    this.window.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("control X"), "close");
    this.window.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("control S"), "saveAndClose");
    this.window.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("control R"), "loadGame");
    this.window.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("control 1"), "startGameAtLevel1");
    this.window.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("control 2"), "startGameAtLevel2");
    this.window.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "pause");
    this.window.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitPause");
    this.addToWindow();
    this.window.pack();
    this.window.setLocationRelativeTo(null);
    this.window.setVisible(true);

   /* this.levelLabel.setVisible(true);
    this.levelLabel.setText("Level : 1");*/
  }

  /**
   * Add components to the constructed frame.
   */
  private void addToWindow(){
    this.saveGame.addActionListener(actionEvent -> saveGame());
    this.file.add(this.saveGame);
    this.loadGame.addActionListener(actionEvent -> loadSave());
    this.file.add(this.loadGame);
    this.level1.addActionListener(actionEvent -> startLeve1());
    this.level2.addActionListener(actionEvent -> startLeve2());
    this.level.add(level1);
    this.level.add(level2);
    this.start_recording.addActionListener(actionEvent -> startRecording());
    this.load_recording.addActionListener(actionEvent -> loadRecording());
    this.stop_recording.addActionListener(actionEvent -> stopRecording());
    this.recorder.add(start_recording);
    this.recorder.add(load_recording);
    this.recorder.add(stop_recording);
    this.viewHelp.addActionListener(actionEvent -> showHelpView());
    this.help.add(viewHelp);
    JMenuBar saveLoad = new JMenuBar();
    saveLoad.add(this.file);
    saveLoad.add(this.level);
    saveLoad.add(this.recorder);
    saveLoad.add(help);
    this.window.setJMenuBar(saveLoad);

    this.mainWindow.setMinimumSize(new Dimension(650, 500));
    this.mainWindow.setPreferredSize(new Dimension(650,500));
    this.mainWindow.setBackground(new Color(132, 166, 231));
    gamePane = new GamePane(this.domain);
    recorderPanel = new RecorderPanel(this);
    infoPanel = new InfoPanel(this);
    this.mainWindow.setLayout(new BorderLayout());
    this.mainWindow.add(gamePane, BorderLayout.CENTER);
    this.mainWindow.add(infoPanel, BorderLayout.EAST);
    this.mainWindow.add(recorderPanel, BorderLayout.SOUTH);
    this.window.getContentPane().add(this.mainWindow);
   /* this.mainWindow.setLayout(new FlowLayout(FlowLayout.LEFT));
    this.mainWindow.add(gamePane);
    this.mainWindow.add(infoPanel);
    this.mainWindow.add(recorderPanel);
    this.window.getContentPane().add(this.mainWindow);*/
  }

  /**
   * Controls movement of the player.
   *
   * @param dir - direction player moves
   * @param isFromLog - True if the command is coming from a replay.
   */
  public void playerMovement(Direction dir, boolean isFromLog) {
    infoPanel.startTimer();
    domain.move(dir);
    Chap chap = domain.getChap();
    if (!chap.isLive()) {
      JOptionPane.showMessageDialog(null, "you died, please restart");
      restartGame();
      return;
    }
    if (chap.getTile().getClass() == Exit.class) {
      //System.out.println(" exit ");
      String [] options = {"next level","restart current level"};
      Object choice = JOptionPane.showInputDialog(null, "please select your choice：", "tips", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
      String info = (String) choice;
      if (info.equals(options[0])) {
        this.currentLevel = 2;
        restartGame();
      } else if (info.equals(options[1])) {
        restartGame();
      }
    }
    gamePane.repaint();
    infoPanel.repaint();
  }

  public void startLeve1() {
    this.currentLevel = 1;
    restartGame();
  }

  public void startLeve2() {
    this.currentLevel = 2;
    restartGame();
  }

  private void saveGame() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.showSaveDialog(this.mainWindow);
    File selectedFile = fileChooser.getSelectedFile();
    gameSaver.saveGame(domain, selectedFile.getName());
  }

  private void loadSave() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.showOpenDialog(this.mainWindow);
    File selectedFile = fileChooser.getSelectedFile();
    try {
      this.domain = gameLoader.levelloader(selectedFile.getName().split("[.]")[0]);
      this.gamePane.update(this.domain);
      this.gamePane.repaint();
      this.infoPanel.repaint();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void startRecording() {
    this.gamePane.showRecord();
    this.domain.recording= true;

  }
  private void stopRecording(){
    this.gamePane.hideRecord();
    infoPanel.stopTimer();
    this.domain.recording = false;
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.showSaveDialog(this.mainWindow);
    File selectedFile = fileChooser.getSelectedFile();
    Recorder.Quicksave(domain,selectedFile.getName());

  }

  private void loadRecording() {
    this.recorderPanel.showPanel();
  }

  public int getCurrentLevel(){
    return currentLevel;
  }

  public JPanel getMainWindow(){
    return mainWindow;
  }

  public Main getMain() {
    return this.game;
  }

  /**
   * Show how to play.
   */
  private void showHelpView() {
    //stopTimers();
    this.isPaused = true;
    helpView = new HelpView(this.window);
    helpView.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        super.windowClosed(e);
        isPaused = false;
        //startTimers();
      }
    });
    this.helpView.setVisible(true);
  }
}