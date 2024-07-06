package TeamProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D; // für die "Taschenlampenfunktion"

public class GUI {

    private static final int DEFAULT_PADDING = 20;
    private static final int DEFAULT_COLUMN_WIDTH = 30;
    private static final int DEFAULT_ROW_HEIGHT = 30;
    private static final int DEFAULT_BUTTON_WIDTH = 200;
    private static final int DEFAULT_BUTTON_HEIGHT = 50;

    private static final double DEFAULT_MOVEMENT_SPEED = 0.05;

    private static double VISIBLE_RADIUS = 40; // sichtbarer Radius der Taschenlampe
    private static boolean IS_FLASHLIGHT_ON = false; // Boolean der speichert ob Taschenlampenmodus ein oder aus ist

    private static final Dimension ButtonDimension = new Dimension(DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT);


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
            @Override
            protected void paintComponent(Graphics graphics) {

                super.paintComponent(graphics);

                Box[] graph = maze.getGraph();
                Player player = maze.getPlayer();


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
                for (Box box : graph) {
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

                    // Zeichne Buchstaben
                    if(box.getItem() != null) {
                        Items item = box.getItem();
                        //graphics.setColor(item.getColor());
                        Font font = new Font("Arial", Font.BOLD, 15);
                        graphics.setFont(font);
                        graphics.drawString(item.getLetter(),
                                xpos * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING + 10,
                                ypos * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING + 20);

                    }


                }


                // zeichne Player:
                graphics.setColor(player.getColor());
                graphics.fillOval(
                        (int) (player.getXPos() * DEFAULT_COLUMN_WIDTH) + DEFAULT_PADDING,
                        (int) (player.getYPos() * DEFAULT_ROW_HEIGHT) + DEFAULT_PADDING,
                        (int) (player.getWidth() * DEFAULT_COLUMN_WIDTH),
                        (int) (player.getHeight() * DEFAULT_ROW_HEIGHT));




            }
        };


        // Lege präferierte Größe der Zeichenebene fest:
        canvas.setPreferredSize(new Dimension(maze.getColumnNum() * DEFAULT_COLUMN_WIDTH + 2 * DEFAULT_PADDING, maze.getRowNum() * DEFAULT_ROW_HEIGHT + 2 * DEFAULT_PADDING));
        // Setze Hintergrundfarbe auf Weiß:
        canvas.setBackground(Color.WHITE);
        // Füge die Zeichenebene in das Fenster ein:
        mainFrame.getContentPane().add(canvas, BorderLayout.NORTH);


        // Erstelle KeyListener für die Tasten WASD
        KeyListener WASDListener = new KeyListener() {
            private boolean WPressed = false;
            private boolean APressed = false;
            private boolean SPressed = false;
            private boolean DPressed = false;

            private final Timer t = new Timer(
                20,
                (_) -> {
                    if (this.WPressed) maze.movePlayer(0., -DEFAULT_MOVEMENT_SPEED);
                    if (this.APressed) maze.movePlayer(-DEFAULT_MOVEMENT_SPEED, 0.);
                    if (this.SPressed) maze.movePlayer(0., DEFAULT_MOVEMENT_SPEED);
                    if (this.DPressed) maze.movePlayer(DEFAULT_MOVEMENT_SPEED, 0.);
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


        // Erstelle Panel, um die Boxen darin zu platzieren
        JPanel ButtonPanel = new JPanel();
        ButtonPanel.setLayout(new FlowLayout());
        ButtonPanel.setBackground(Color.WHITE);
        mainFrame.getContentPane().add(ButtonPanel, BorderLayout.SOUTH);

        // Erstelle Panel, um die Label und Timer darin zu platzieren
        JPanel LabelPanel = new JPanel();
        LabelPanel.setLayout(new FlowLayout());
        LabelPanel.setBackground(Color.WHITE);
        mainFrame.getContentPane().add(LabelPanel, BorderLayout.CENTER);


        // Erstelle, Platziere Knopf zum Neuerstellung des Labyrinths und füge Funktionalität hinzu:
        JButton ResetButton = new JButton();
        ResetButton.setVisible(true);
        ResetButton.setPreferredSize(ButtonDimension);
        ResetButton.setBackground(Color.WHITE);
        ResetButton.setText("Reset Maze");
        ActionListener ResetListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.reset();
                mainFrame.requestFocus();
            }
        };
        ResetButton.addActionListener(ResetListener);
        ButtonPanel.add(ResetButton);


        // Erstelle, Platziere Knopf zum Einstellen der Schwierigkeit des Labyrinths und füge Funktionalität hinzu:
        JButton DifficultyButton = new JButton();
        DifficultyButton.setVisible(true);
        DifficultyButton.setPreferredSize(ButtonDimension);
        DifficultyButton.setBackground(Color.WHITE);
        DifficultyButton.setText("Difficulty:");
        ButtonPanel.add(DifficultyButton);


        // Erstelle Button für noch unbekannten Grund:
        JButton Button3 = new JButton();
        Button3.setVisible(true);
        Button3.setPreferredSize(ButtonDimension);
        Button3.setBackground(Color.WHITE);
        Button3.setText("TBD");
        ButtonPanel.add(Button3);

        // Erstelle Label für die Buchstaben
        JLabel Label1 = new JLabel();
        Label1.setVisible(true);
        Label1.setPreferredSize(ButtonDimension);
        Label1.setBackground(Color.WHITE);
        Label1.setText(Maze.getWord());
        LabelPanel.add(Label1);



        // Anpassen der Größe und Layout des Fensters:
        mainFrame.pack();
    }



    // Weitere Hilfsmethoden:

    // Taschenlampenfunktion Getter/Setter
    private boolean getFlashlightOn() {return IS_FLASHLIGHT_ON;}
    private static void setFlashLightOn(double radius) {
        VISIBLE_RADIUS = radius;
         IS_FLASHLIGHT_ON = true;
    }
    private static void setFlashLightOff() {IS_FLASHLIGHT_ON = false;}
//    private void setRainbowOn(){
//        for(int i = 0; i < 100; i++){
//            canvas.setBackground(Color.WHITE);
//        }
//    }


    // Main Methode:
    public static void main(String[] args) {
        //GUI.setFlashLightOn(3.0);
        Maze maze = new Maze(20, 20);
        runMaze(maze);
    }
}
