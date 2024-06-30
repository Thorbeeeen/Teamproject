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

    public double getWidth() {return width;}

    public void setWidth(int width) {
        this.width = width;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean touchesItem(Items item){
        double left_X = this.getXPos();
        double right_X = this.getXPos() + this.getWidth();
        double top_Y = this.getYPos();
        double bottom_Y = this.getYPos() + this.getHeight();

        double itemLeft_X = item.getXPos();
        double itemRight_X = item.getXPos() + 1; // Assuming each item occupies one cell
        double itemTop_Y = item.getYPos();
        double itemBottom_Y = item.getYPos() + 1;

        return left_X < itemRight_X && right_X > itemLeft_X && top_Y < itemBottom_Y && bottom_Y  > itemTop_Y;
    }

}
