package ObjRender;

import MathP.Octree;
import MathP.Point;

import java.awt.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Parser {
    public static Entity OBJReader(String objData, String mtlData){
        Scanner fileReader = new Scanner(objData);
        Scanner lineReader;
        String line;

        LinkedList<Point> points = new LinkedList<>();
        LinkedList<Face> faces = new LinkedList<>();
        Material currentMat = null;
        LinkedList[] data = mtlReader(mtlData);
        LinkedList names = data[0];
        LinkedList mats = data[1];

        while (fileReader.hasNextLine()){
            line = fileReader.nextLine();
            lineReader = new Scanner(line);
            if (line.length() >= 6){
                if (line.substring(0,6).equals("usemtl")){
                    lineReader.next();
                    currentMat = getMat(names, mats, lineReader.nextLine().trim());
                }
            }
            if (!line.isEmpty()){
                if (line.charAt(0) == 'v' || line.charAt(0) == 'f') {
                    lineReader.next();
                    switch (line.charAt(0)){
                        case 'v':
                            points.add(new Point(
                                    lineReader.nextDouble(),
                                    lineReader.nextDouble(),
                                    lineReader.nextDouble()
                            ));
                            Point p = points.getLast();
                            if (Octree.base == null){
                                double side = Point.max(p);
                                Octree.base = new Octree(new Point(-side + p.getX(), -side + p.getY(), -side + p.getZ()), side);
                            }
                            Octree.base.getCube(p).contain(p);
                            break;
                        case 'f':
                            faces.add(new Face(
                                    points.get(lineReader.nextInt()-1),
                                    points.get(lineReader.nextInt()-1),
                                    points.get(lineReader.nextInt()-1),
                                    currentMat
                            ));
                            break;
                    }
                }
            }
        }
        System.out.println(Octree.base.count());
        System.out.print(Octree.base);
        return new Entity(faces);
    }

    public static Entity OBJReader(String objData){
        Scanner fileReader = new Scanner(objData);
        Scanner lineReader;
        String line;

        LinkedList<Point> points = new LinkedList<>();
        LinkedList<Face> faces = new LinkedList<>();
        Material currentMat = new Material(Color.red);

        while (fileReader.hasNextLine()){
            line = fileReader.nextLine();
            lineReader = new Scanner(line);
            if (line.length() >= 1){
                if (line.charAt(0) == 'v' || line.charAt(0) == 'f') {
                    lineReader.next();
                    switch (line.charAt(0)){
                        case 'v':
                            points.add(new Point(
                                    lineReader.nextDouble(),
                                    lineReader.nextDouble(),
                                    lineReader.nextDouble()
                            ));
                            break;
                        case 'f':
                            faces.add(new Face(
                                    points.get(lineReader.nextInt()-1),
                                    points.get(lineReader.nextInt()-1),
                                    points.get(lineReader.nextInt()-1),
                                    currentMat
                            ));
                            break;
                    }
                }
            }
        }

        return new Entity(faces);
    }

    public static LinkedList[] mtlReader(String mtlData){
        Scanner fileReader = new Scanner(mtlData);
        Scanner lineReader;
        String line;
        Material m;
        Color diffuseColor = null;
        Color ambientColor = null;
        int illum = 0;

        LinkedList<String> names = new LinkedList<>();
        LinkedList<Material> mats = new LinkedList<>();
        while (fileReader.hasNextLine()){
            line = fileReader.nextLine();
            lineReader = new Scanner(line);
            if (line.length() >= 6){
                if (line.substring(0,6).equals("newmtl")){
                    lineReader.next();
                    names.add(lineReader.next().trim());
                    if (diffuseColor != null){
                        m = new Material(diffuseColor);
                        m.setAmbientColor(ambientColor);
                        m.setIllum(illum);
                        mats.add(m);
                    }
                }
            }
            if (line.length() >= 5){
                if (line.substring(0,5).equals("illum")){
                    lineReader.next();
                    illum = (int)lineReader.nextDouble();
                }
            }
            if (line.length() >= 2){
                if (line.charAt(0)=='K'){
                    lineReader.next();
                    Color c = new Color(
                            (int)(lineReader.nextDouble()*255),
                            (int)(lineReader.nextDouble()*255),
                            (int)(lineReader.nextDouble()*255));
                    switch (line.charAt(1)){
                        case 'a':
                            ambientColor = c;
                            break;
                        case 'd':
                            diffuseColor = c;
                            break;
                    }
                }
            }
            if (line.length() >= 1){
                if (line.charAt(0)=='d'){
                    lineReader.next();
                    diffuseColor = new Color(
                            diffuseColor.getRed(),diffuseColor.getGreen(),diffuseColor.getBlue(),
                            (int)(lineReader.nextDouble()*255));
                }
            }
        }
        m = new Material(diffuseColor);
        m.setAmbientColor(ambientColor);
        m.setIllum(illum);
        mats.add(m);

        return new LinkedList[]{names,mats};
    }

    public static Material getMat(LinkedList<String> names, LinkedList<Material> mats, String name){
        int index;
        index = names.indexOf(name);

        return mats.get(index);
    }
}
