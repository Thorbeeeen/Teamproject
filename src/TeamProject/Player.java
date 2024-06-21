package TeamProject;

import java.awt.*;

public class Player {

    private double xpos;
    private double ypos;

    private int width;
    private int height;

    private Color color;

    public Player(int width, int height) {
        this.xpos = 0.5;
        this.ypos = 0.5;
        this.width =  width;
        this.height = height;
        this.color = new Color(0, 0, 0);
    }

    public double getXPos() {
        return xpos;
    }

    public void setXPos(double xpos) {
        this.xpos = xpos;
    }

    public double getYPos() {
        return ypos;
    }

    public void setYPos(double ypos) {
        this.ypos = ypos;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
