package main.java.nz.ac.vuw.ecs.swen225.gp22.persistency;




import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Domain;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Chap;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Player.Monster;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items.Keys;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.FreeTile;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.Info_Field;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.LockDoor;
import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.tiles.Tile;
import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created with IntelliJ IDEA. Description: User: Yujie Huang Date: 2022-09-18
 * gameLoader class is for saving game purpose by saving domain tiles into a xml file
 *
 */
public final class gameSaver {
    // creating a new file
    public static void saveGame(Domain game, String savename) {
        Document doc = new Document(new Element("level"));
        Element temp1 = saveChap(game);
        doc.getRootElement().addContent(temp1);
        // saving monsters
        for(Monster monster : game.getMonsters()){
            int x = monster.getPoint().x;
            int y = monster.getPoint().y;
            Element monstertemp = new Element("monster");
            Element x1 = new Element("x");
            Element y1 = new Element("y");
            x1.addContent(String.valueOf(x));
            y1.addContent(String.valueOf(y));
            monstertemp.addContent(x1);
            monstertemp.addContent(y1);
            doc.getRootElement().addContent(monstertemp);

        }
        for (int i = 0; i < game.tiles.length; i++) {
            Element row = new Element("row");
            for (int j = 0; j <= game.tiles[i].length; j++) {
                try {
                    Element temp = processTile(game.tiles[j][i]);
                    row.addContent(temp);
                } catch (Exception e) { e.printStackTrace();}
            }
            doc.getRootElement().addContent(row);
            saveDoc(doc, savename);
        }

    }

    /**
     * saving  chap
     */
    private static Element saveChap(Domain dom){
        int x = dom.chap.getPoint().x;
        int y = dom.chap.getPoint().y;
        Element chaptemp = new Element("chap");
       Element x1 = new Element("x");
        Element y1 = new Element("y");
        x1.addContent(String.valueOf(x));
        y1.addContent(String.valueOf(y));
        chaptemp.addContent(x1);
        chaptemp.addContent(y1);

        return chaptemp;

    }

    /**
     * processing every tile in the tiles and transform them into xml file
     */
    private static Element processTile(Tile tile) throws Exception {
        Element returner = new Element("tile");
        if(tile.getname().equals("WallTile")){
            returner.setAttribute("type","wall");
            returner.addContent("");
            return returner;
        } else if(tile.getname().equals("Exit")){
            returner.setAttribute("type","exit");
            return returner;
        } else if(tile.getname().equals("water")){
            returner.setAttribute("type","water");
            return returner;
        } else if(tile.getname().equals("iron")){
            returner.setAttribute("type","iron");
            return returner;
        } else if(tile.getname().equals("Exitlock")){
            returner.setAttribute("type","finaldoor");
            return returner;
        } else if(tile.getname().equals("Info_field")){
            Info_Field temp = (Info_Field) tile;
            returner.setAttribute("type","info");
            returner.addContent(temp.getInfo());
            System.out.println(temp.getInfo());
            return returner;
        } else if(tile.getname().equals("LockDoor")){
            LockDoor temp = (LockDoor) tile;
            returner.setAttribute("type","door");
            returner.addContent(getHue(temp.getColour()));
            return returner;
        } else if(tile.getname().equals("FreeTile")){
            FreeTile temp = (FreeTile) tile;
            returner.setAttribute("type","free");
            if(temp.getItem()!=null){
                Element tempitem = new Element(temp.getItem().getname());
               if(temp.getItem().getname().equals("redkey")){
                    tempitem.setName("key");
                    tempitem.addContent("0");

                }
                else if(temp.getItem().getname().equals("bluekey")){
                    tempitem.setName("key");
                    tempitem.addContent("240");

                }
               else if(temp.getItem().getname().equals("greenkey")){
                    tempitem.setName("key");
                    tempitem.addContent("120");

                }



                returner.addContent(tempitem);
            }
            return returner;
        } else{
            throw new Exception("invalid tile found: "+ tile.toString());
        }

    }

    /**
     * return a number from colour
     */
    private static String getHue(Color color){
        float[] hsb;
        hsb = Color.RGBtoHSB(color.getRed(),color.getGreen(),color.getBlue(),null);
        hsb[0] = hsb[0]*360;
        System.out.println(hsb[0]);
        return String.valueOf((int)hsb[0]);
    }

    /**
     * saving Document
     */
    private static void saveDoc(Document doc, String savename){
        Path.of("levels/" + savename + ".xml");
        Format format = Format.getPrettyFormat();
        format.setExpandEmptyElements(true);
        XMLOutputter xmlOutput = new XMLOutputter(format);
        try {
            xmlOutput.output(doc, new FileWriter("levels/" + savename + ".xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

