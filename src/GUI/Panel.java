package GUI;

import Main.Main;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

import MathP.Point;
import ObjRender.Entity;
import ObjRender.Face;
import ObjRender.Ray;

public class Panel extends JPanel {
    public static double FOV = 100;
    public static double newResolution = 0.05;
    private static final int RENDER_DISTANCE = 1000;
    public static boolean print = false;
    private final JFrame parent;
    public Color[][] frame;

    public Panel(JFrame parent){
        this.parent = parent;
    }

    public void paintComponent(Graphics g) {
        double resolution = Math.sqrt(newResolution);
        LinkedList<Face> faces = new LinkedList<>();
        double length = (parent.getWidth() * resolution);    //The horizontal number of rays being sent out
        double height = (parent.getHeight() * resolution);   //The vertical number of rays being sent out
        double angleOffset = FOV / length;                  //The yaw change in the vectors angle every "pixel"
        double angleOffsetY[] = new double[(int)length];
        double deltaX = parent.getWidth() / length;          //The horizontal size of a pixel given the 3d resolution
        double deltaY = parent.getHeight() / height;         //The horizontal size of a pixel given the 3d resolution
        frame = new Color[(int) Math.ceil(parent.getWidth()*resolution)][(int) Math.ceil(parent.getHeight()*resolution)];
        Point rot = Main.cameraRot.clone();

        long currentTime = System.currentTimeMillis();
        String printFile = "";
//        for(Entity e: Main.objects){
//            for(Face f:e.getFaces()){
////                Point p = f.getCenter();
////                Vector v = Vector.getVector(Main.cameraPos, p);
////                double dist = v.getRealNormal();
////                double pitch = Math.toDegrees(Math.asin(v.getT().getZ()/dist))-90;
////                double pan = Math.toDegrees(Math.acos(v.getT().getY()/dist))-90;
////                if (pan > rot.getX() && pan-rot.getX() < FOV)
////                    if(pitch < rot.getY() && pitch-rot.getY() < FOV)
////                        if(dist > 5)
//                                faces.add(f);
//
//            }
//        }
//
//        faces.sort(Face::compareTo);
        rot.setX(Main.cameraRot.getX()+FOV/2);
        for (int x = 0; x < length; x++) {
            rot.setY(Main.cameraRot.getY()+FOV/2);
            for (int y = 0; y < height; y++) {
                    Ray r = new Ray(Main.cameraPos.clone(),rot.clone());

                    Face f = r.run(RENDER_DISTANCE);
                    if (f != null){
                        frame[x][y] = f.getMaterial();

                    }
                    else
                        frame[x][y] = Color.white;
                    if (print)
                        printFile += (r.getPos())+"\n";
//                System.out.println(((((rot.getY())))));
                rot.setY(rot.getY() - angleOffset);
//                rot.setY(Main.cameraRot.getY()-asin((sin(FOV/2)*2/height) - sin(rot.getY()-Main.cameraRot.getY())));
            }
//            if (rot.getX() < 0){
//                double angleX = -(Math.abs(rot.getX()) % 360);
//                rot.setX(angleX+360);
//            }else
//                rot.setX(rot.getX() % 360);
//
//            if (Main.cameraRot.getX() < 0){
//                double angleX = -(Math.abs(Main.cameraRot.getX()) % 360);
//                Main.cameraRot.setX(angleX+360);
//            }else
//                Main.cameraRot.setX(Main.cameraRot.getX() % 360);

            rot.setX(rot.getX() - angleOffset);
//            rot.setX(Main.cameraRot.getX()+acos((Math.abs(cos(FOV/2))*2/length) + cos(rot.getX()-Main.cameraRot.getX())));
            //System.out.println(rot.getX());
        }
        if (print){
            PrintWriter printW = null;
            try {
//                printW = new PrintWriter(new File("C:\\Users\\fazam\\3D Objects\\test.xyz"));
//                printW.print("");
//                printW.close();
//                printW = new PrintWriter(new File("C:\\Users\\fazam\\3D Objects\\test.xyz"));
//                printW.println(printFile);
//                printW.print(Main.cameraPos);
//                printW.flush();
//                printW.close();
                System.out.println(printFile);
                System.out.println(Main.cameraPos);
            } catch (Exception e) {
                e.printStackTrace();
            }
            print = false;
        }

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < height; y++) {
                g.setColor(frame[x][y]);
                g.fillRect(
                        (int)(x* deltaX),(int)(y* deltaY),
                        (int)Math.ceil(deltaX),(int)Math.ceil(deltaY));
            }
        }
        double fps = 1000.0/((System.currentTimeMillis() - currentTime));

        g.setColor(Color.black);
        g.drawString("Pos: "+Main.cameraPos.toString(),10,10);
        g.drawString("Rot: "+Main.cameraRot.toString(),10,20);
        g.drawString("FOV: "+FOV,10,30);
        g.drawString("RES: "+(int)(newResolution*100)+"%",10,40);
        g.drawString("FPS: "+((Double.isInfinite(fps)) ? "Infinite" : (int)(fps)),10,50);
        g.drawString("FRAME: "+Main.frame,10,60);
        Main.frame++;
    }
    public static double sin(double angle){
        return Math.sin(Math.toRadians(angle));
    }

    public static double asin(double a){
        return Math.toDegrees(Math.asin(a));
    }
    public static double cos(double angle){
        return Math.cos(Math.toRadians(angle));
    }

    public static double acos(double a){
        return Math.toDegrees(Math.acos(a));
    }
}