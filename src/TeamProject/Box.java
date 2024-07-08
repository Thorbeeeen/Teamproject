package TeamProject;

import javax.swing.text.BoxView;
import java.awt.*;
import java.util.*;

public class Box {

    public static final Color DEFAULT_PORTAL_COLOR = Color.MAGENTA;

    private final int xpos;
    private final int ypos;
    private final LinkedList<Box> neighbors;
    private Box portal;
    private Color portalColor;
    private Items item;

    public Box(int xpos, int ypos, LinkedList<Box> neighbors, Box portal) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.neighbors = neighbors;
        this.portal = portal;
        this.portalColor = DEFAULT_PORTAL_COLOR;
        this.item = null;
    }

    public Box(int xpos, int ypos) {
        this(xpos, ypos, new LinkedList<Box>(), null);
    }

    public int getXPos() {
        return xpos;
    }

    public int getYPos() {
        return ypos;
    }

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public Box getPortal() {
        return this.portal;
    }

    public void setPortal(Box portal) {
        this.portal = portal;
    }

    public Color getPortalColor() {
        return this.portalColor;
    }

    public void setPortalColor(Color portalColor) {
        this.portalColor = portalColor;
    }



    public void addPortal(Box portal) {
        this.portal = portal;
        portal.setPortal(this);
    }

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



    /**
     * Implementierung um die Boxen im Debug Modus schnell zu unterscheiden.
     */
    @Override
    public String toString() {
        return "Box(" + xpos + ", " + ypos + ")";
    }
}
