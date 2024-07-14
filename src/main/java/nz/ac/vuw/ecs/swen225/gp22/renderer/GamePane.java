package main.java.nz.ac.vuw.ecs.swen225.gp22.renderer;

import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Domain;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Chap;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Monster;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Item;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Keys;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Treasure;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.otherInfo.Direction;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.Info_Field;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.Tile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @program: GroupWork
 * @description: game pane implement
 * @author: ZihaoSama
 * @create: 2022-09-25 01:28
 **/
public class GamePane extends JLayeredPane {

    private Domain domain;
    private Tile[][] tiles;
    private int blockSize = 90;
    private int width;
    private int height;
    private Image bigInfoLabel = Toolkit.getDefaultToolkit().getImage("src/main/resources/picture/sign_large.png");
    private Image recordLabel = Toolkit.getDefaultToolkit().getImage("src/main/resources/picture/rec_picture.png");
    private boolean recordFlag = false;

    public void update(Domain domain){
        this.domain = domain;
        this.tiles = domain.getTiles();
        this.width = domain.getWidth();
        this.height = domain.getHeight();
    }


    public GamePane(Domain domain) {
        this.domain = domain;
        this.tiles = domain.getTiles();
        this.width = domain.getWidth();
        this.height = domain.getHeight();
        setPreferredSize(new Dimension(5 * blockSize, 5 * blockSize));
        setMaximumSize(new Dimension(5 * blockSize, 5 * blockSize));
        setMinimumSize(new Dimension(5 * blockSize, 5 * blockSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, width, height);
        if (tiles != null) {
            Chap chap = domain.getChap();
            Tile chapTile = chap.getTile();
            int minx = chapTile.getPoint().x - 2;
            int minY = chapTile.getPoint().y - 2;
            int maxX = chapTile.getPoint().x + 2;
            int maxY = chapTile.getPoint().y + 2;
            if (minx <= 0) {
                minx = 0;
                maxX = minx + 4;
            }
            if (maxX >= width - 1) {
                maxX = width - 1;
                minx = maxX - 4;
            }

            if (minY <= 0) {
                minY = 0;
                maxY = minY + 4;
            }
            if (maxY >= height - 1) {
                maxY = height - 1;
                minY = maxY - 4;
            }
           // System.out.println("minx: --->" + minx);
          //  System.out.println("maxx: --->" + maxX);
          //  System.out.println("miny: --->" + minY);
          //  System.out.println("maxy: --->" + maxY);
          //  System.out.println("========================================");
            for (int row = minY; row <= maxY ; row++) {
                for (int col = minx; col <= maxX; col++) {
                    // draw image
                    int x = col - minx;
                    int y = row - minY;
                    Tile tile = tiles[col][row];
                    if (tile != null) {
                        g.drawImage(Toolkit.getDefaultToolkit().getImage("src/main/resources/picture/Floor.png"),
                                x *blockSize, y *blockSize,blockSize,blockSize,this);
                        g.drawImage(Toolkit.getDefaultToolkit().getImage(tile.getPath()),
                                x * blockSize, y * blockSize, blockSize, blockSize, this);
                        if (tile.containItem()) {
                            g.drawImage(Toolkit.getDefaultToolkit().getImage(tile.getItem().getPath()),
                                    x * blockSize, y * blockSize, blockSize, blockSize, this);
                        }
                    }

                }
            }
            //draw monster
            List<Monster> monsters = domain.getMonsters();
            for (Monster monster : monsters) {
                int x = monster.getPoint().x;
                int y = monster.getPoint().y;
                if (x <= maxX && x >= minx && y <= maxY && y >= minY) {
                    g.drawImage(Toolkit.getDefaultToolkit().getImage(monster.getPath()),
                            (x - minx) * blockSize, (y - minY) * blockSize, blockSize, blockSize, this);
                }
            }

            //draw player
            g.drawImage(Toolkit.getDefaultToolkit().getImage(chap.getPath()),
                    (chapTile.getPoint().x - minx) * blockSize, (chapTile.getPoint().y - minY) * blockSize, blockSize, blockSize, this);

            Tile tile = tiles[chapTile.getPoint().x][chapTile.getPoint().y];
            if (tile.getClass() == Info_Field.class) {
                g.drawImage(bigInfoLabel, 0, 0, 400, 400,this);
                g.setFont(new Font("", Font.BOLD, 20));
                g.drawString("Collect pickaxes, mine the blocks,", 20, 40);
                g.drawString("collect the emeralds to open ", 20, 80);
                g.drawString(" and reach the portal!", 20, 120);
            }
        }

        if (recordFlag) {
            g.drawImage(recordLabel, 10, 10, 50, 50,this);
        }
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                g.drawImage(Toolkit.getDefaultToolkit().getImage("src/main/resources/picture/Floor.png"),
//                        row * blockSize, col * blockSize, blockSize, blockSize, this);
//            }
//        }
//        g.drawImage(Toolkit.getDefaultToolkit().getImage("src/main/resources/picture/Person/PlayerLeftWalk.gif"),
//                curX * blockSize, curY * blockSize, blockSize, blockSize, this);
    }

    public void showRecord() {
        this.recordFlag = true;
    }

    public void hideRecord() {
        this.recordFlag = false;
    }

}