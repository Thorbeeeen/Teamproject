package TeamProject;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GUI {

    private static int DEFAULT_PADDING = 20;
    private static int DEFAULT_COLUMN_WIDTH = 30;
    private static int DEFAULT_ROW_HEIGHT = 30;

    public static void runMaze(Maze maze) {

        int[][][] graph = maze.getGraph();

        JFrame mainFrame = new JFrame();
        mainFrame.setResizable(true);
        mainFrame.setVisible(true);
        mainFrame.setTitle("Info 2 - Particle simulation");
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\thorb\\OneDrive - UT Cloud\\Module\\SS24\\Info2 SS24\\Project 1\\src\\particleSimulation\\AltIcon.png"));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                for (int i = 0; i < graph.length; i++) {
                    for (int j = 0; j < graph[0].length; j++) {
                        for (int k = 0; k < graph[0][0].length; k++) {
                            if (graph[i][j][k] == 1) continue;
                            switch (k) {
                                case 0:
                                    graphics.drawLine(
                                            (i + 1) * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                            j * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING,
                                            (i + 1) * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                            (j + 1) * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING);
                                    break;
                                case 1:
                                    graphics.drawLine(
                                            i * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                            (j + 1) * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING,
                                            (i + 1) * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                            (j + 1) * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING);
                                    break;
                                case 2:
                                    graphics.drawLine(
                                            i * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                            j * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING,
                                            i * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                            (j + 1) * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING);
                                    break;
                                case 3:
                                    graphics.drawLine(
                                            i * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                            j * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING,
                                            (i + 1) * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                            j * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING);
                                    break;
                            }
                        }
                    }
                }
            }
        };

        canvas.setPreferredSize(new Dimension(
                graph.length * DEFAULT_COLUMN_WIDTH + 2 * DEFAULT_PADDING,
                graph[0].length * DEFAULT_ROW_HEIGHT + 2 * DEFAULT_PADDING));
        mainFrame.getContentPane().add(canvas);
        mainFrame.pack();
    }

    public static void main(String[] args) {
        Maze maze = new Maze(20, 20);
        int[][][] graph = maze.getGraph();
        runMaze(maze);
    }
}
