package main.java.nz.ac.vuw.ecs.swen225.gp22.application;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * Created with IntelliJ IDEA. Description: User: Yusi Cheng Date: 2022-09-21 Time: 9:41
 */
public class HelpView extends JDialog {


  /**
   * Displays the help HTML file for Chips Among Us.
   *
   * @param owner - The JFrame this viewer is linked to.
   */
  public HelpView(JFrame owner) {
    super(owner);
    this.setTitle("Help - Chips Among Us");
    this.setSize(640, 480);
    this.setMinimumSize(new Dimension(320, 480));
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    JEditorPane helpViewer = new JEditorPane();
    URL helpFileUrl = null;
    try {
      helpFileUrl = new File("HelpView_need/help.html").toURI().toURL();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    try {
      helpViewer.setPage(helpFileUrl);
    } catch (IOException e) {
      e.printStackTrace();
    }
    helpViewer.setEditable(false);
    JScrollPane helpViewerScrollable = new JScrollPane(helpViewer);
    helpViewer.setCaretPosition(0);
    helpViewerScrollable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(helpViewerScrollable);
  }
}
