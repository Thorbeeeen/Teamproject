package TeamProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

public class GUI {

    private static final int DEFAULT_PADDING = 20;
    private static final int DEFAULT_BOX_PADDING = 2;
    private static final int DEFAULT_COLUMN_WIDTH = 30;
    private static final int DEFAULT_ROW_HEIGHT = 30;
    private static final int DEFAULT_CONTAINER_HEIGHT = 50;
    private static final double DEFAULT_MOVEMENT_SPEED = 0.05;


    // Hauptmethode:
    public static void runMaze(Maze maze) {

        final int DEFAULT_LABEL_WIDTH = maze.getColumnNum() * DEFAULT_COLUMN_WIDTH;
        final int DEFAULT_BUTTON_WIDTH = DEFAULT_LABEL_WIDTH / 2;
        final Dimension DEFAULT_BUTTON_DIMENSION = new Dimension(DEFAULT_BUTTON_WIDTH, DEFAULT_CONTAINER_HEIGHT);

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
                double flashlightRadius = maze.getFlashlightRadius();


                // Zeichne Taschenlampe falls getLightBulb = TRUE
                if(flashlightRadius != -1.) {
                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.clearRect(0, 0, getWidth(), getHeight());

                    int playerXPos = (int) (player.getXPos() * DEFAULT_COLUMN_WIDTH) + DEFAULT_PADDING;
                    int playerYPos = (int) (player.getYPos() * DEFAULT_ROW_HEIGHT) + DEFAULT_PADDING;
                    double visibleRadius = flashlightRadius * DEFAULT_COLUMN_WIDTH;



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

                    graphics.setColor(box.getColor());
                    graphics.fillOval(
                            xpos * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING + DEFAULT_BOX_PADDING,
                            ypos * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING + DEFAULT_BOX_PADDING,
                            DEFAULT_COLUMN_WIDTH - 2 * DEFAULT_BOX_PADDING,
                            DEFAULT_ROW_HEIGHT - 2 * DEFAULT_BOX_PADDING
                    );
                    graphics.setColor(Color.BLACK);

                    // zeichne rechte Wand der Box, falls nötig:
                    if (!box.connects(maze.getBoxAtPosition(xpos + 1, ypos))) {
                        graphics.drawLine(
                                (xpos + 1) * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                ypos * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING,
                                (xpos + 1) * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                (ypos + 1) * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING);
                    }


                    // zeichne untere Wand der Box, falls nötig
                    if (!box.connects(maze.getBoxAtPosition(xpos, ypos + 1)))
                        graphics.drawLine(
                                xpos * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                (ypos + 1) * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING,
                                (xpos + 1) * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                (ypos + 1) * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING);


                    // zeichne linke Wand der Box, falls nötig
                    if (!box.connects(maze.getBoxAtPosition(xpos - 1, ypos))){
                        graphics.drawLine(
                                xpos * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                ypos * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING,
                                xpos * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                (ypos + 1) * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING);
                    }


                    // zeichne obere Wand der Box, falls nötig:
                    if (!box.connects(maze.getBoxAtPosition(xpos, ypos - 1))) {
                        graphics.drawLine(
                                xpos * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                ypos * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING,
                                (xpos + 1) * DEFAULT_COLUMN_WIDTH + DEFAULT_PADDING,
                                ypos * DEFAULT_ROW_HEIGHT + DEFAULT_PADDING);
                    }

                    // zeichne Buchstaben:
                    if (box.getItem() != null) {
                        Items item = box.getItem();
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

        // Erstelle Panel, um die Label und Timer darin zu platzieren
        JPanel LabelPanel = new JPanel();
        LabelPanel.setLayout(new FlowLayout());
        LabelPanel.setBackground(Color.WHITE);
        mainFrame.getContentPane().add(LabelPanel, BorderLayout.CENTER);

        // Erstelle Label für die Buchstaben
        JLabel WordLabel = new JLabel();
        WordLabel.setVisible(true);
        WordLabel.setPreferredSize(DEFAULT_BUTTON_DIMENSION);
        WordLabel.setFont(new Font("Arial", Font.BOLD, 15));
        //WordLabel.setBorder(new LineBorder(Color.BLACK, 2));
        WordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        WordLabel.setBackground(Color.WHITE);
        WordLabel.setText("Hello");
        //WordLabel.setText(maze.getDisplayedWord()); // CHANGE: GetWOrd
        LabelPanel.add(WordLabel);

        // Erstelle KeyListener für die Tasten WASD
        KeyListener WASDListener = new KeyListener() {
            private double movementMultiplier = 1.0;
            private boolean WPressed = false;
            private boolean APressed = false;
            private boolean SPressed = false;
            private boolean DPressed = false;

            private final Timer t = new Timer(
                20,
                (_) -> {
                    if (this.WPressed) maze.movePlayer(0., -movementMultiplier * DEFAULT_MOVEMENT_SPEED);
                    if (this.APressed) maze.movePlayer(- movementMultiplier * DEFAULT_MOVEMENT_SPEED, 0.);
                    if (this.SPressed) maze.movePlayer(0., movementMultiplier * DEFAULT_MOVEMENT_SPEED);
                    if (this.DPressed) maze.movePlayer(movementMultiplier * DEFAULT_MOVEMENT_SPEED, 0.);
                    if (maze.getState() == 1) WordLabel.setText("Glückwunsch! Du hast das Ziel erreicht!");
                    if (maze.getState() == 2) WordLabel.setText("Yeah! du hast \"" + maze.getWord() + "\" gefunden");
                    else WordLabel.setText(maze.getDisplayedWord());
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
                if (e.getKeyChar() == 'n') maze.teleportPlayer();
                if (e.getKeyChar() == 'm') movementMultiplier = 1.8;
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
                if (e.getKeyChar() == 'm') movementMultiplier = 1.;
            }
        };

        // Füge den KeyListener dem Fenster hinzu:
        mainFrame.addKeyListener(WASDListener);

        // Erstelle Panel, um die Boxen darin zu platzieren
        JPanel ButtonPanel = new JPanel();
        ButtonPanel.setLayout(new FlowLayout());
        ButtonPanel.setBackground(Color.WHITE);
        mainFrame.getContentPane().add(ButtonPanel, BorderLayout.SOUTH);

        // Erstelle und Platziere Knopf zur Neuerstellung des Labyrinths und füge Funktionalität hinzu:
        JButton ResetButton = new JButton();
        ResetButton.setVisible(true);
        ResetButton.setPreferredSize(DEFAULT_BUTTON_DIMENSION);
        ResetButton.setBackground(Color.WHITE);
        ResetButton.setText("Reset Maze");
        ActionListener ResetListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.reset();
                //WordLabel.setText(maze.getDisplayedWord());
                mainFrame.requestFocus();
            }
        };
        ResetButton.addActionListener(ResetListener);
        ButtonPanel.add(ResetButton);

        // Erstelle, Platziere Knopf zum Einstellen der Schwierigkeit des Labyrinths und füge Funktionalität hinzu:
        JButton DifficultyButton = new JButton();
        DifficultyButton.setVisible(true);
        DifficultyButton.setPreferredSize(DEFAULT_BUTTON_DIMENSION);
        DifficultyButton.setBackground(Color.WHITE);
        DifficultyButton.setText("Modus: " + maze.getDifficultyAsString());
        ActionListener DifficultyListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.changeDifficulty();
                maze.reset();
                WordLabel.setText(maze.getDisplayedWord());
                DifficultyButton.setText("Difficulty: " + maze.getDifficultyAsString());
                mainFrame.requestFocus();
            }
        };
        DifficultyButton.addActionListener(DifficultyListener);
        ButtonPanel.add(DifficultyButton);

        // Anpassen der Größe und Layout des Fensters:
        mainFrame.pack();
    }



    // Main Methode:
    public static void main(String[] args) {
        Maze maze = new Maze(20, 20);
        runMaze(maze);
    }
}
