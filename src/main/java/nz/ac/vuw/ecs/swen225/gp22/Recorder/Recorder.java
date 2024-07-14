package main.java.nz.ac.vuw.ecs.swen225.gp22.Recorder;

import java.io.*;
import java.util.*;


import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.otherInfo.Direction;

import main.java.nz.ac.vuw.ecs.swen225.gp22.domain.Domain;


public  class Recorder {
/**
 * Created with IntelliJ IDEA. Description: User: Yujie Huang Date: 2022-09-18
 * including saving chap moves into a txt file
 * loading txt file content into chap moves
 * chap moves is a Map that consists every steps of chap's movement
 * Directory of recorder files
 */
    private static String directory = "main/java/nz/ac/vuw/ecs/swen225/gp22/Recording";
    private static Direction dir;

    // Saving game for recording purpose into txt file
    public static void Quicksave(Domain game, String fileName) {

        if(game.moves.isEmpty()) {
            System.err.println("Can't record nothing...");
            return;
        }
        File file = new File(fileName);


        BufferedWriter bf = null;
        try {


            bf = new BufferedWriter(new FileWriter(file));


            for (Map.Entry<Integer, Direction> entry :
                    game.moves.entrySet()) {


                bf.write(entry.getKey() + ":"
                        + entry.getValue());

                // new line
                bf.newLine();
            }

            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {

                bf.close();
            }
            catch (Exception e) {
            }
        }


    }

// loading game for recording purpose
    public static HashMap<Integer,Direction> Quickload(File file) {

        HashMap<Integer, Direction> recordmoves
                = new HashMap<Integer, Direction>();
        BufferedReader br = null;

        try {


            br = new BufferedReader(new FileReader(file));

            String line = null;


            while ((line = br.readLine()) != null) {


                String[] parts = line.split(":");


                int index = Integer.valueOf(parts[0].trim());
                String dirtemp = parts[1].trim();
                // transform string into direction
                if(dirtemp.equals("Up")){
                     dir = Direction.Up;
                }
                if(dirtemp.equals("Right")){
                     dir = Direction.Right;
                }
                if(dirtemp.equals("Down")){
                     dir = Direction.Down;
                }
                 if(dirtemp.equals("Down")){
                     dir = Direction.Down;
                }


                    recordmoves.put(index, dir);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {


            if (br != null) {
                try {
                    br.close();
                }
                catch (Exception e) {
                };
            }
        }

        return recordmoves;
    }
}

