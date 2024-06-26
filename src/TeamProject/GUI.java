package TeamProject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D; // für die "Taschenlampenfunktion"

public class GUI {

    private static final int DEFAULT_PADDING = 20;
    private static final int DEFAULT_COLUMN_WIDTH = 30;
    private static final int DEFAULT_ROW_HEIGHT = 30;

    private static double VISIBLE_RADIUS = 1.5; // sichtbarer Radius der Taschenlampe
    private static boolean IS_FLASHLIGHT_ON = false; // Boolean der speichert ob Taschenlampenmodus ein oder aus ist


    // Hauptmethode:
    public static void runMaze(Maze maze) {
        // Erstelle neues Fenster:
        JFrame mainFrame = new JFrame();
        mainFrame.setResizable(true);
        mainFrame.setVisible(true);
        mainFrame.setTitle("Info 2 - Teamprojekt");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(new BorderLayout());


        // Erstelle zusätzliche Ebene, um die Boxen zu zeichnen:
        JPanel canvas = new JPanel() {
            private final Box[] graph = maze.getGraph();;
            private final Player player = maze.getPlayer();

            @Override
            protected void paintComponent(Graphics graphics) {


                // Zeichne Taschenlampe falls getLightBulb = TRUE
                if(IS_FLASHLIGHT_ON) {
                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.clearRect(0, 0, getWidth(), getHeight());

                    int playerXPos = (int) (player.getXPos() * DEFAULT_COLUMN_WIDTH) + DEFAULT_PADDING;
                    int playerYPos = (int) (player.getYPos() * DEFAULT_ROW_HEIGHT) + DEFAULT_PADDING;
                    double visibleRadius = VISIBLE_RADIUS * DEFAULT_COLUMN_WIDTH;



                    // Sichtbarer Kreis um Player
                    Shape visibleArea = new Ellipse2D.Double(
                            playerXPos - visibleRadius,
                            playerYPos - visibleRadius,
                            2 * visibleRadius,
                            2 * visibleRadius);


                    // Set Clip zu visibleArea
                    g2d.setClip(visibleArea);
                }



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
                        (int) (player.getXPos() * DEFAULT_COLUMN_WIDTH - 0.5 * player.getWidth()) + DEFAULT_PADDING,
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


        // Erstelle Panel um die Boxen darin zu platzieren
        JPanel ButtonPanel = new JPanel();
        ButtonPanel.setLayout(new FlowLayout());
        ButtonPanel.setBackground(new Color(255, 255, 255));
        mainFrame.getContentPane().add(ButtonPanel, BorderLayout.SOUTH);


        // Erstelle, Platziere Knopf zum Neuerstellung des Labyrinths und füge Funktionalität hinzu:
        JButton ResetButton = new JButton();
        ResetButton.setVisible(true);
        ResetButton.setPreferredSize(new Dimension(200, 50));
        ResetButton.setBackground(new Color(255, 255, 255));
        ResetButton.setText("Reset Maze");
        ButtonPanel.add(ResetButton);


        // Erstelle, Platziere Knopf zum Einstellen der Schwierigkeit des Labyrinths und füge Funktionalität hinzu:
        JButton DifficultyButton = new JButton();
        DifficultyButton.setVisible(true);
        DifficultyButton.setPreferredSize(new Dimension(200, 50));
        DifficultyButton.setBackground(new Color(255, 255, 255));
        DifficultyButton.setText("Difficulty:");
        ButtonPanel.add(DifficultyButton);


        // Erstelle, Platziere Knopf zum Einstellen der Schwierigkeit des Labyrinths und füge Funktionalität hinzu:
        JButton Button3 = new JButton();
        Button3.setVisible(true);
        Button3.setPreferredSize(new Dimension(200, 50));
        Button3.setBackground(new Color(255, 255, 255));
        Button3.setText("TBD");
        ButtonPanel.add(Button3);


        // Anpassen der Größe und Layout des Fensters:
        mainFrame.pack();
    }



    // Weitere Hilfsmethoden:

    // Taschenlampenfunktion Getter/Setter
    private boolean getFlashlightOn(){return IS_FLASHLIGHT_ON;}
    private static void setFlashLightOn(double radius){
        VISIBLE_RADIUS = radius;
        IS_FLASHLIGHT_ON = true;
    }
    private static void setFlashLightOff(){IS_FLASHLIGHT_ON = false;}



    // Main Methode:
    public static void main(String[] args) {
        GUI.setFlashLightOn(1.0);
        Maze maze = new Maze(20, 20);
        runMaze(maze);
    }
}
