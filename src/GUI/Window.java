package GUI;

import Main.Main;
import ObjRender.Ray;

import javax.swing.*;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class Window extends JFrame implements KeyListener, MouseMotionListener {
    public double movementSpeed = 0.2;

    public double forwardDirection;
    public double strafeDirection;
    public double flyDirection;

    public Robot r;

    public boolean paused = false;
    private double mouseSens = 0.1;

    BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImg, new Point(0, 0), "blank cursor");


    public Window(int l, int h) throws AWTException {
        Panel p = new Panel(this);
        this.setTitle("RayTracer 2.0");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(p);
        this.addKeyListener(this);
        this.addMouseMotionListener(this);
        this.setVisible(true);

        r = new Robot();
        r.mouseMove(
                this.getLocationOnScreen().x+l/2,
                this.getLocationOnScreen().y+h/2);
        this.getContentPane().setCursor(blankCursor);
        this.setSize(l,h);
        this.setResizable(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_W:
                forwardDirection = movementSpeed;
                break;
            case KeyEvent.VK_S:
                forwardDirection = -movementSpeed;
                break;
            case KeyEvent.VK_D:
                strafeDirection = movementSpeed;
                break;
            case KeyEvent.VK_A:strafeDirection = -movementSpeed;
                break;
            case KeyEvent.VK_SPACE:
                flyDirection = movementSpeed;
                break;
            case KeyEvent.VK_SHIFT:
                flyDirection = -movementSpeed;
                break;
            case KeyEvent.VK_MINUS:
                Panel.FOV-=10;
                System.out.println(Panel.FOV);
                break;
            case KeyEvent.VK_EQUALS:
                Panel.FOV+=10;
                System.out.println(Panel.FOV);
                break;
            case KeyEvent.VK_H:
                Main.cameraPos = new MathP.Point(0,0,0);
                Main.cameraRot = new MathP.Point(0,0,0);
                Panel.FOV = 100;
                Panel.newResolution = 0.05;
                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                if (!paused){
                    this.getContentPane().setCursor(blankCursor);
                }
                else{
                    this.getContentPane().setCursor(Cursor.getDefaultCursor());
                }
                break;
            case KeyEvent.VK_P:
                Panel.print = true;
                break;
            case KeyEvent.VK_LEFT:
                Panel.newResolution -= 0.01;
                Panel.newResolution = Panel.newResolution < 0 ? 0 : Panel.newResolution;
                break;
            case KeyEvent.VK_RIGHT:
                Panel.newResolution += 0.01;
                Panel.newResolution = Panel.newResolution > 1 ? 1 : Panel.newResolution;
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_W:
            case KeyEvent.VK_S:
                forwardDirection = 0;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_A:
                strafeDirection = 0;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_SHIFT:
                flyDirection = 0;
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX() - this.getWidth()/2;
        int mouseY = e.getY() - this.getHeight()/2;

        if (!paused){
            r.mouseMove(
                    this.getLocationOnScreen().x+this.getWidth()/2,
                    this.getLocationOnScreen().y+this.getHeight()/2);
            Main.cameraRot.setX((Main.cameraRot.getX()-mouseX*mouseSens)%360);
            double yVal = (Main.cameraRot.getY()-mouseY*mouseSens);
            if (yVal < -90){
                yVal = -90;
            }
            else if (yVal > 90){
                yVal = 90;
            }
            Main.cameraRot.setY(yVal%360);
            Main.cameraRot.setX(Main.cameraRot.getX() < 0 ? 360+Main.cameraRot.getX():Main.cameraRot.getX());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {}
}
