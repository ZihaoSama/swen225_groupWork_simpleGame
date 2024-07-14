package main.java.nz.ac.vuw.ecs.swen225.gp22.persistency;

import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Domain;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Chap;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Monster;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Keys;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Treasure;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.otherInfo.Direction;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.*;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created with IntelliJ IDEA. Description: User: Yujie Huang Date: 2022-09-18
 * gameLoader class is for loading game purpose by reading a xml file into domain tiles
 *
 */
public final class gameLoader {
public static Chap chap;
public static Monster monster;


    public static Domain levelloader(String levelname) throws Exception {
        File file;
        file = new File("levels/"+levelname+".xml");
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(file);               // building the file
        Element rootnode = document.getRootElement();
        Element row = rootnode.getChild("row");
        List<Element> rows = rootnode.getChildren("row");
        Element chaptemp = rootnode.getChild("chap");
        List<Element> monstertemp = rootnode.getChildren("monster");
        int count = 0;
        for(Element tile : row.getChildren("tile")){
            count++;

        }
        int width = count;
        int height =rows.size();
        Tile[][] tiles = new Tile[width][height];

        for (int i = 0; i < rows.size(); i++) {
            System.out.println("wide" +width);
            System.out.println("high" +height);
            ReadRows(rows,tiles);

        }
        Domain game = new Domain(tiles);
        ReadMonster(monstertemp,game);
        ReadMonster(monstertemp,game);
        ReadChap(chaptemp,game);

        return game;
    }
    // adding chap into domain
    public static void ReadChap(Element chaptemp, Domain domain){
        if(chaptemp == null){
            return;
        }
        int x = Integer.parseInt(chaptemp.getChild("x").getText());
        int y = Integer.parseInt(chaptemp.getChild("y").getText());

        chap = new Chap();
        chap.setPoint(new Point(x,y));
        chap.setTile(domain.tiles[x][y]);
        domain.setChap(chap);
    }

  // adding monster into domain
    public static void ReadMonster(List<Element> monstertemp, Domain domain){
        if(monstertemp == null){
            return;
        }
        List monsters = new ArrayList<Monster>();
        for(Element m :monstertemp) {
            int x = Integer.parseInt(m.getChild("x").getText());
            int y = Integer.parseInt(m.getChild("y").getText());

            monster = new Monster(new Point(x, y), Direction.Up);
            monsters.add(monster);
        }

        domain.setMonsters(monsters);
    }

    // reading every rows in xml file and return a set of tiles
    private static Tile[][] ReadRows(List<Element> rows,Tile[][] tiles) throws Exception {
        boolean hasexit =false;
      //  boolean haschap = false;
        boolean hasexitdoor = false;
        Set<Color> doorcolors= new HashSet<>();
        Set<Color> keycolors= new HashSet<>();
        int exitlockx = 0;
        int exitlocky = 0;
        int y = 0;
        int x = 0;
        for(Element row : rows){
            x = 0;
            for (Element tile : row.getChildren("tile")) {

                if (tile.getAttributeValue("type").equals("wall")) { // reading Walltile
                    tiles[x][y] = new WallTile(new Point(x,y));

                } else if (tile.getAttributeValue("type").equals("finaldoor")) { // reading the final door
                    exitlockx = x;
                    exitlocky = y;
                    hasexitdoor = true;
                    tiles[x][y] = new ExitLock(new Point(exitlockx,exitlocky));
                }
                else if(tile.getAttributeValue("type").equals("water")){ // reading water
                    tiles[x][y] = new Water(new Point(x,y));
                }
                else if(tile.getAttributeValue("type").equals("iron")){ // reading iron
                    tiles[x][y] = new IronBox(new Point(x,y));
                }
                else if (tile.getAttributeValue("type").equals("free")) { // reading floor
                    tiles[x][y] = new FreeTile(new Point(x,y));
                    if (tile.getChild("key") != null) {
                        if (tile.getChild("key").getContent().size() == 0) {
                            throw new Exception("key does not have a color specified");
                        }
                        Content content = tile.getChild("key").getContent().get(0); // reading key
                        Color temp = ElementColor(content);
                        keycolors.add(temp);
                        tiles[x][y].setItem( new Keys(temp));
                    } else if (tile.getChild("treasure") != null) { // reading treasure
                        Treasure treasure = new Treasure();
                        tiles[x][y].setItem(treasure);
                    } else if (tile.getChild("chap") != null) {
                        Element chapelem = tile.getChild("chap");
                        for (Element item : chapelem.getChildren()) {
                            if (item.getName().equals("key")) { // reading key
                                if (item.getContent().size() == 0) {
                                    throw new Exception("inventory key does not have a color specified");
                                }
                                Content content = tile.getChild("key").getContent().get(0);
                                Color temp = ElementColor(content);
                            } else if (item.getName().equals("treasure")) {
                            }
                        }
                    }

                } else if (tile.getAttributeValue("type").equals("door")) { // reading door
                    if (tile.getContent().size() == 0) {
                        throw new Exception("door doesn't have a colour");
                    }
                    Content content = tile.getContent().get(0);
                    Color temp = ElementColor(content);

                    doorcolors.add(temp);
                    tiles[x][y] = new LockDoor(temp,new Point(x,y));
                } else if (tile.getAttributeValue("type").equals("exit")) { // reading exit
                    tiles[x][y] = new Exit(new Point());
                    hasexit = true;
                } else if (tile.getAttributeValue("type").equals("info")) { // reading info
                    if (tile.getText().equals("")) {
                        throw new Exception("info no message");
                    } else {
                        tiles[x][y] = new Info_Field(tile.getText(),new Point(x,y));
                    }
                } else {
                    throw new Exception("invalid tile");
                }

                x++;

            }
            y++;
        }

        if(!hasexit){
            throw new Exception("game no exit");
        }
        if(!hasexitdoor) {
            throw new Exception("game no exit door");
        }
        if(!keycolors.containsAll(doorcolors)) {
            throw new Exception("key doesn't match door");
        }
        return tiles;
    }
// transfering number in xml file into colour
    private static Color ElementColor(Content content){
        try{
            float color = Float.parseFloat(content.getValue());
            color /= 360;
            return Color.getHSBColor(color,1,1);

        }catch(IllegalArgumentException e){System.out.println("content color not matched");}
        throw new RuntimeException("ElementColor break");
    }


}

