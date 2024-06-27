package TeamProject;

import java.awt.*;

public class Player {

    private double xpos;
    private double ypos;

    private double width;
    private double height;

    private Color color;

    public Player(double width, double height) {
        this.xpos = 0.5;
        this.ypos = 0.5;
        this.width =  width;
        this.height = height;
        this.color = Color.BLACK;
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

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getWidth() {
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
