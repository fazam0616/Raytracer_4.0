package ObjRender;

import java.awt.*;

public class Material extends Color {
    private Color ambientColor;
    private int illum;

    public Material (Color c){
        super(c.getRed(),c.getGreen(),c.getBlue(), c.getAlpha());
    }
    public Material(int r, int g, int b) {
        super(r, g, b);
    }

    public Material(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public void setAmbientColor(Color c) {
        this.ambientColor = c;
    }

    public Color getAmbientColor() {
        return ambientColor;
    }

    public int getIllum() {
        return illum;
    }

    public void setIllum(int illum) {
        this.illum = illum;
    }

    public Material clone() throws CloneNotSupportedException {
        Material m =  new Material((Color) super.clone());
        m.setAmbientColor(new Color(ambientColor.getRGB()));
        m.setIllum(illum);
        return m;
    }
}
