package TeamProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI {

    private static final int DEFAULT_PADDING = 20;
    private static final int DEFAULT_COLUMN_WIDTH = 30;
    private static final int DEFAULT_ROW_HEIGHT = 30;

    public static void runMaze(Maze maze) {


        // Erstelle neues Fenster:
        JFrame mainFrame = new JFrame();
        mainFrame.setResizable(true);
        mainFrame.setVisible(true);
        mainFrame.setTitle("Info 2 - Particle simulation");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        // Erstelle zusätzliche Ebene, um die Boxen zu zeichnen:
        JPanel canvas = new JPanel() {
            private final Box[] graph = maze.getGraph();;
            private final Player player = maze.getPlayer();

            @Override
            protected void paintComponent(Graphics graphics) {
                // zeichne die Wände der Boxen:
                for (Box box : this.graph) {
                    int xpos = box.getXPos();
                    int ypos = box.getYPos();


                    // zeichne rechte Wand der Box, falls nötig:
                    if (!box.connects(maze.getBoxAtPosition(xpos + 1, ypos)))
                        graphics.drawLine(
                                (xpos + 1) * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                ypos * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING,
                                (xpos + 1) * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                (ypos + 1) * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING);


                    // zeichne untere Wand der Box, falls nötig
                    if (!box.connects(maze.getBoxAtPosition(xpos, ypos + 1)))
                        graphics.drawLine(
                                xpos * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                (ypos + 1) * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING,
                                (xpos + 1) * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                (ypos + 1) * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING);


                    // zeichne linke Wand der Box, falls nötig
                    if (!box.connects(maze.getBoxAtPosition(xpos - 1, ypos)))
                        graphics.drawLine(
                                xpos * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                ypos * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING,
                                xpos * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                (ypos + 1) * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING);


                    // zeichne obere Wand der Box, falls nötig:
                    if (!box.connects(maze.getBoxAtPosition(xpos, ypos - 1)))
                        graphics.drawLine(
                                xpos * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                ypos * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING,
                                (xpos + 1) * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                ypos * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING);
                }


                // zeichne Player:
                graphics.setColor(player.getColor());
                graphics.fillOval(
                        (int) (player.getXPos()* DEFAULT_COLUMN_WIDTH - 0.5 * player.getWidth()) + DEFAULT_PADDING,
                        (int) (player.getYPos() * DEFAULT_ROW_HEIGHT - 0.5 * player.getWidth()) + DEFAULT_PADDING,
                        player.getWidth(),
                        player.getHeight());
            }
        };


        // Lege präferierte Größe der Zeichenebene fest:
        canvas.setPreferredSize(new Dimension(maze.getColumnNum() * DEFAULT_COLUMN_WIDTH + 2 * DEFAULT_PADDING, maze.getRowNum() * DEFAULT_ROW_HEIGHT + 2 * DEFAULT_PADDING));
        // Füge die Zeichenebene in das Fenster ein:
        mainFrame.getContentPane().add(canvas);


        // Erstelle KeyListener für die Tasten WASD
        KeyListener WASDListener = new KeyListener() {
            private final Player player = maze.getPlayer();

            private boolean WPressed = false;
            private boolean APressed = false;
            private boolean SPressed = false;
            private boolean DPressed = false;

            private final Timer t = new Timer(
                20,
                (e) -> {
                    if (this.WPressed) player.setYPos(player.getYPos() - 0.05);
                    if (this.APressed) player.setXPos(player.getXPos() - 0.05);
                    if (this.SPressed) player.setYPos(player.getYPos() + 0.05);
                    if (this.DPressed) player.setXPos(player.getXPos() + 0.05);
                    mainFrame.repaint();
                }
            );

            {
                t.start();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'w') this.WPressed = true;
                if (e.getKeyChar() == 'a') this.APressed = true;
                if (e.getKeyChar() == 's') this.SPressed = true;
                if (e.getKeyChar() == 'd') this.DPressed = true;
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == 'w') this.WPressed = false;
                if (e.getKeyChar() == 'a') this.APressed = false;
                if (e.getKeyChar() == 's') this.SPressed = false;
                if (e.getKeyChar() == 'd') this.DPressed = false;
            }
        };


        // Füge den KeyListener dem Fenster hinzu:
        mainFrame.addKeyListener(WASDListener);
        // Anpassen der Größe des Fensters:
        mainFrame.pack();
    }

    public static void main(String[] args) {
        Maze maze = new Maze(10, 10);
        runMaze(maze);
    }
}
