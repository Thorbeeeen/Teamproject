package TeamProject;

import java.util.*;

public class Box {

    private final int xpos;
    private final int ypos;
    private final LinkedList<Box> neighbors;
    private Items item;

    public Box(int xpos, int ypos, LinkedList<Box> neighbors) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.neighbors = neighbors;
        this.item = null;
    }

    public Box(int xpos, int ypos) {
        this(xpos, ypos, new LinkedList<Box>());
    }

    public int getXPos() {
        return xpos;
    }

    public int getYPos() {
        return ypos;
    }

    public Items getItem() {return item;}

    public void setItem(Items item) {this.item = item;}

    public int countNeighbors() {
        return this.neighbors.size();
    }

    public void addConnection(Box box) {
        this.neighbors.add(box);
    }

    public void delConnection(Box box) {
        this.neighbors.remove(box);
    }

    public boolean connects(Box box) {
        return this.neighbors.contains(box);
    }

    @Override
    public String toString() {
        return "Box(" + xpos + ", " + ypos + ")";
    }
}
