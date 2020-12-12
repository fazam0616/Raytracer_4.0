package Main;
import GUI.Window;
import MathP.Matrix;
import MathP.Point;
import ObjRender.Entity;
import ObjRender.Face;
import ObjRender.Parser;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static LinkedList<Entity> objects = new LinkedList<>();
    public static Point cameraRot = new Point(0,0,0);
    public static Point cameraPos = new Point(0,0,0);
    public static Window window;
    public static Robot r;
    public static int frame = 0;

    public static void main(String[] args) throws InterruptedException, AWTException, IOException {
        r = new Robot();

        window = new Window(600,600);

        String objData = "";
        String mtlData = "";
        Scanner s = new Scanner(Main.class.getResourceAsStream("/ObjRender/Objects/test_square2/tinker.obj"));
        while (s.hasNextLine()){
            objData += s.nextLine()+"\n";
        }

        //f = new File();
        s = new Scanner(Main.class.getResourceAsStream("/ObjRender/Objects/test_square2/obj.mtl"));
        while (s.hasNextLine()){
            mtlData += s.nextLine()+"\n";
        }

        Entity h = Parser.OBJReader(objData,mtlData);
//        h = Parser.OBJReader(objData,mtlData);
//        h.move(new Point(0,100,0));
//        h = Parser.OBJReader(objData,mtlData);
//        h.move(new Point(0,0,100));
//        h = Parser.OBJReader(objData,mtlData);
//        h.move(new Point(100,0,0));
//        h = Parser.OBJReader(objData,mtlData);
//        h.move(new Point(0,-100,0));
//        h = Parser.OBJReader(objData,mtlData);
//        h.move(new Point(0,0,-100));
//        h = Parser.OBJReader(objData,mtlData);
//        h.move(new Point(-100,0,0));

        window.repaint();


//        objects.get(0).rotate(new Point(0,0,0));
        while (true){
            objects.sort(Entity::compareTo);
            for (Entity e:objects){
                e.getFaces().sort(Face::compareTo);
            }
            if (!window.paused){
                //h.move(new Point(0,0,0.01));
                moveCamera();
                window.repaint();
            }

            Thread.sleep(1);
        }
    }

    public static void moveCamera(){
        cameraPos.setX(cameraPos.getX()+window.forwardDirection*Math.cos(Math.toRadians(cameraRot.getX())));
        cameraPos.setY(cameraPos.getY()+window.forwardDirection*Math.sin(Math.toRadians(cameraRot.getX())));

        cameraPos.setX(cameraPos.getX()-window.strafeDirection*Math.cos(Math.toRadians(cameraRot.getX()+90)));
        cameraPos.setY(cameraPos.getY()-window.strafeDirection*Math.sin(Math.toRadians(cameraRot.getX()+90)));

        cameraPos.setZ(cameraPos.getZ()+window.flyDirection);
    }
}
