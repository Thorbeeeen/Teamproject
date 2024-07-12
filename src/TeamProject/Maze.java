package TeamProject;

import java.awt.*;
import java.util.Random;

/**
 * Hauptklasse, die Zustand das Labyrinth speichert und auf Eingaben der Benutzeroberfläche reagiert.
 */
public class Maze {

    private static final double DEFAULT_PLAYER_WIDTH = 0.2;
    private static final double DEFAULT_PLAYER_HEIGHT = 0.2;
    private static final int DEFAULT_RECURSION_LIMIT = 1000000;
    private static final int DIFFICULTY_NUM = 5;

    private final int columnNum;
    private final int rowNum;

    private Player player;
    private Box[] graph;
    private int[][] indexes;

    // Bedeutung von Difficulty:
    //
    // 0 <---> leichte Wörter
    // 1 <---> schwere Wörter
    // 2 <---> ohne Wörter
    // 3 <---> ohne Wörter mit Portal
    // 4 <---> ohne Wörter mit Flashlight
    //
    private int Difficulty = 3;

    private int collectedLetters;
    private String word;
    private String displayedWord = "";

    private double flashlightRadius;

    // Bedeutung von State:
    //
    // 0 <---> Ziel wurde noch nicht erreicht
    // 1 <---> Ziel wurde schon erreicht (Bei Difficulty = 0, 3, 4)
    // 2 <---> Wort wurde schon gefunden (Bei Difficulty = 1, 2)
    //
    private int State = 0;





    public Maze(int columnNum, int rowNum) {
        this.columnNum = columnNum;
        this.rowNum = rowNum;

        // Erstelle Labyrinth mit Spieler und, falls nötig:
        reset();
    }

    public Player getPlayer() {
        return this.player;
    }

    public Box[] getGraph() {
        return this.graph;
    }

    public int getColumnNum() {
        return this.columnNum;
    }

    public int getRowNum() {
        return rowNum;
    }

    public String getDifficultyAsString() {
        return switch (this.Difficulty) {
            case 0 -> "Kurze Wörter";
            case 1 -> "Lange Wörter";
            case 2 -> "Normal";
            case 3 -> "Portal";
            default -> "Nebel";
        };
    }

    public double getFlashlightRadius() {
        return this.flashlightRadius;
    }

    public int getState() {
        return this.State;
    }

    public String getWord() {
        return this.word;
    }

    public String getDisplayedWord() {
        return this.displayedWord;
    }

    public Box getBoxAtPosition(int xpos, int ypos) {
        if (0 <= xpos && xpos < this.columnNum && 0 <= ypos && ypos < this.rowNum) return this.graph[this.indexes[xpos][ypos]];
        else return null;
    }

    private Box getBoxAtPosition(Box[] graph, int xpos, int ypos) {
        for (Box box : graph) {
            if (box.getXPos() == xpos && box.getYPos() == ypos) return box;
        }
        return null;
    }





    public void movePlayer(double deltaX, double deltaY) {
        double leftX = this.player.getXPos();
        double rightX = this.player.getXPos() + this.player.getWidth();
        double upperY = this.player.getYPos();
        double bottomY = this.player.getYPos() + this.player.getHeight();

        Box upperleftBox = getBoxAtPosition((int) leftX, (int) upperY);
        Box upperrightBox = getBoxAtPosition((int) rightX, (int) upperY);
        Box bottomleftBox = getBoxAtPosition((int) leftX, (int) bottomY);
        Box bottomrightBox = getBoxAtPosition((int) rightX, (int) bottomY);

        if (deltaX > 0) {
            if ((int) rightX == (int) (rightX + deltaX)) {
                this.player.setXPos(this.player.getXPos() + deltaX);
            }
            else if (upperrightBox == bottomrightBox && upperrightBox.connects(getBoxAtPosition((int) (rightX + deltaX), (int) upperY))) {
                this.player.setXPos(this.player.getXPos() + deltaX);
            }
        } else if (deltaX < 0) {
            if ((int) leftX == Math.floor(leftX + deltaX)) {
                this.player.setXPos(this.player.getXPos() + deltaX);
            }
            else if (upperleftBox == bottomleftBox && upperleftBox.connects(getBoxAtPosition((int) (leftX + deltaX), (int) upperY))) {
                this.player.setXPos(this.player.getXPos() + deltaX);
            }
        }
        if (deltaY > 0) {
            if ((int) bottomY == (int) (bottomY + deltaY)) {
                this.player.setYPos(this.player.getYPos() + deltaY);
            }
            else if (bottomrightBox == bottomleftBox && bottomleftBox.connects(getBoxAtPosition((int) leftX, (int) (bottomY + deltaY)))) {
                this.player.setYPos(this.player.getYPos() + deltaY);
            }
        } else if (deltaY < 0) {
            if ((int) upperY == Math.floor(upperY + deltaY)) {
                this.player.setYPos(this.player.getYPos() + deltaY);
            }
            else if (upperrightBox == upperleftBox && upperleftBox.connects(getBoxAtPosition((int) leftX, (int) (upperY + deltaY)))) {
                this.player.setYPos(this.player.getYPos() + deltaY);
            }
        }

        // Falls wir auf einem Buchstaben (Items) sind, sammeln wir es
        for(Box box : this.graph){
            Items item = box.getItem();
            if(item != null && this.player.touchesItem(item)){
                updateDisplayedWord(item);
                box.setItem(null);
                this.collectedLetters++;
            }
        }
        updateState();
    }

    // Wenn wir einen Buchstaben finden, wird er auf dem WordLabel an der richtigen Stelle angezeigt
    public void updateDisplayedWord(Items letter) {
        if(letter == null) return;
        char collectedLetter = letter.getLetter().charAt(0);
        StringBuilder updatedWord = new StringBuilder(this.displayedWord);

        for (int i = 0; i < this.word.length(); i++) {
            char originalChar = this.word.charAt(i);
            char displayChar = this.displayedWord.charAt(i);
            if (originalChar == collectedLetter && displayChar == '_') {
                updatedWord.setCharAt(i, collectedLetter);
                break;
            }
        }
        this.displayedWord = updatedWord.toString();
    }

    public void teleportPlayer() {
        Box currentBox = getBoxAtPosition(
                (int) (this.player.getXPos() + 0.5 * this.player.getWidth()),
                (int) (this.player.getYPos() + 0.5 * this.player.getHeight())
        );
        Box currentBoxPortal = currentBox.getPortal();
        if (currentBoxPortal != null) {
            this.player.setXPos(currentBoxPortal.getXPos() + 0.5);
            this.player.setYPos(currentBoxPortal.getYPos() + 0.5);
        }
        updateState();
    }

    public void changeDifficulty() {
        this.Difficulty = (this.Difficulty + 1) % DIFFICULTY_NUM;
    }

    public void reset() {
        this.player = new Player(DEFAULT_PLAYER_WIDTH, DEFAULT_PLAYER_HEIGHT);
        this.graph = createMaze(columnNum, rowNum);
        this.indexes = createIndexes();

        this.collectedLetters = 0;
        Items wordGenerator = new Items();

        switch (this.Difficulty) {
            case 0:
                this.word = wordGenerator.getEasyWord();
                addLetters(this.word);
                initializeLabel(this.word);
                this.flashlightRadius = -1;
                break;
            case 1:
                this.word = wordGenerator.getHardWord();
                addLetters(this.word);
                initializeLabel(this.word);
                this.flashlightRadius = -1;
                break;
            case 2:
                this.word = "";
                this.displayedWord = "";
                this.flashlightRadius = -1;
                this.graph[this.columnNum * this.rowNum - 1].setColor(Color.RED);
                break;
            case 3:
                this.word = "";
                this.displayedWord = "";
                this.flashlightRadius = -1;

                Box firstPortalBox = getBoxAtPosition((int) (this.columnNum * Math.random()), (int) (this.rowNum * Math.random()));
                Box secondPortalBox = getBoxAtPosition((int) (this.columnNum * Math.random()), (int) (this.rowNum * Math.random()));
                while (firstPortalBox == secondPortalBox) secondPortalBox = getBoxAtPosition((int) (this.columnNum * Math.random()), (int) (this.rowNum * Math.random()));
                firstPortalBox.addPortal(secondPortalBox);
                this.graph[this.columnNum * this.rowNum - 1].setColor(Color.RED);
                break;
            case 4:
                this.word = "";
                this.displayedWord = "";
                this.flashlightRadius = 4;
                this.graph[this.columnNum * this.rowNum - 1].setColor(Color.RED);
                break;
        }
    }





    /**
     * Hilfsmethode, die die Characters von this.word in den Boxen von this.graph verteilt.
     */
    private void addLetters(String word){
        Random random = new Random();
        for (int i = 0; i < word.length(); i++) {
            int xpos;
            int ypos;
            do {
                xpos = random.nextInt(this.columnNum);
                ypos = random.nextInt(this.rowNum);
            } while (getBoxAtPosition(xpos, ypos) == null || getBoxAtPosition(xpos, ypos).getItem() != null);
            getBoxAtPosition(xpos, ypos).setItem(new Items(word.substring(i, i + 1), xpos, ypos));
        }
    }

    /**
     * Hilfsmethode, die zu Beginn eine leere Linie der Länge des Wortes nach anzeigt
     */
     public void initializeLabel(String word){
         this.displayedWord = "";
        for(int i = 0; i < word.length(); i++){
            this.displayedWord = displayedWord.concat("_");
        }
     }

    /**
     *  Hilfsmethode, die überprüft ob der Spieler mit dem Durchlaufen des Labyrinths fertig ist
     */
    private void updateState() {
        switch (this.Difficulty) {
            case 0:
            case 1:
                if (this.collectedLetters == this.word.length()) this.State = 2;
            case 2:
            case 3:
            case 4:
                Box currentBox;
                currentBox = getBoxAtPosition(
                        (int) (this.player.getXPos() + 0.5 * this.player.getWidth()),
                        (int) (this.player.getYPos() + 0.5 * this.player.getHeight())
                );
                if (currentBox.getXPos() == this.columnNum - 1 && currentBox.getYPos() == this.rowNum - 1) this.State = 1;
                break;
        }
    }

    /**
    * Hilfsmethode, die ein zufälliges Labyrinth erzeugt, das lösbar ist.
    */
    private Box[] createMaze(int columnNum, int rowNum) {
        Box[] graph = new Box[columnNum * rowNum];
        for (int i = 0; i < columnNum * rowNum; i++) {
            graph[i] = new Box(i % columnNum, i / rowNum);
        }
        while (createPath(graph, graph[0], new Box[] {graph[columnNum * rowNum - 1]}, 0)[0] == 0);
        Box[] unreachableBoxes = computeUnreachableBoxes(graph);
        while (unreachableBoxes.length != 0) {
            Box startingBox = unreachableBoxes[(int) (unreachableBoxes.length * Math.random())];
            while (createPath(graph, startingBox, computeReachableBoxes(graph), 0)[0] == 0);
            unreachableBoxes = computeUnreachableBoxes(graph);
        }
        return graph;
    }

    /**
    * Hilfsmethode die einen zufälligen Weg von der Box in der (xpos + 1)-ten Spalten und (ypos + 1)-ten Reihe zu den in targets gespeicherten Boxen in maze erzeugt.
    * Das i-te Element aus targets speichert hier das i-te target und wird als ein Array der Länge 2 dargestellt, wobei das erste Element die Spaltennummer minus 1 der Box ist 
    * und das zweite Element die Zeilennummer minus 1 der Box ist.
    */
    private int[] createPath(Box[] graph, Box start, Box[] targets, int recursionCounter) {
        for (Box target : targets) {
            if (target == start) return new int[] {1, recursionCounter};
        }

        if (start.countNeighbors() != 0 || recursionCounter > DEFAULT_RECURSION_LIMIT) return new int[] {0, recursionCounter};

        for (int direction : shuffle(new int[] {0, 1, 2, 3})) {
            Box next;

            switch (direction) {
                case 0:
                    if (start.getXPos() == this.columnNum) continue;
                    next = getBoxAtPosition(graph, start.getXPos() + 1, start.getYPos());
                    break;

                case 1:
                    if (start.getYPos() == this.rowNum) continue;
                    next = getBoxAtPosition(graph, start.getXPos(), start.getYPos() + 1);
                    break;

                case 2:
                    if (start.getXPos() == 0) continue;
                    if (start.getYPos() == this.rowNum - 1 || start.getYPos() == 0) continue;
                    next = getBoxAtPosition(graph, start.getXPos() - 1, start.getYPos());
                    break;

                default:
                    if (start.getYPos() == 0) continue;
                    if (start.getXPos() == this.columnNum - 1 || start.getXPos() == 0) continue;
                    next = getBoxAtPosition(graph, start.getXPos(), start.getYPos() - 1);
                    break;
            }

            if (next == null) continue;
            start.addConnection(next);
            int[] foundPath = createPath(graph, next, targets, recursionCounter + 1);
            if (foundPath[0] == 1) {
                next.addConnection(start);
                return foundPath;
            }
            start.delConnection(next);
            recursionCounter = foundPath[1];
        }
        return new int[] {0, recursionCounter};
    }

    /**
    * Hilfsmethode für createMaze, die ein Box-Array mischt, um im erstellten Labyrinth aus createMaze für Zufall zu sorgen.
    */
    private int[] shuffle(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int j = (int) (array.length * Math.random());
            int temp = array[j];
            array[j] = array[i];
            array[i] = temp;
        }
        return array;
    }

    /**
    * Hilfsmethode für createMaze, die alle Boxen berechnet die NICHT von der oberen linken Box erreicht werden können.
    * Die Ausgabe wird wie der Parameter targets in createPath repräsentiert. 
    */
    private Box[] computeUnreachableBoxes(Box[] graph) {
        int length = 0;
        for (Box target : graph) {
            boolean bool = true;
            for (Box box : graph) {
                if (box.connects(target)) {
                    bool = false;
                    break;
                }
            }
            if (bool) length++;
        }
        Box[] result = new Box[length];
        int pos = 0;

        for (Box target : graph) {
            boolean bool = true;
            for (Box box : graph) {
                if (box.connects(target)) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                result[pos] = target;
                pos++;
            }
        }
        return result;
    }

    /**
    * Hilfsmethode für createMaze, die alle Boxen berechnet die von der oberen linken Box erreicht werden können.
    * Die Ausgabe wird wie der Parameter targets in createPath repräsentiert. 
    */
    private Box[] computeReachableBoxes(Box[] graph) {
        int length = 0;
        for (Box target : graph) {
            boolean bool = false;
            for (Box box : graph) {
                if (box.connects(target)) {
                    bool = true;
                    break;
                }
            }
            if (bool) length++;
        }
        Box[] result = new Box[length];
        int pos = 0;

        for (Box target : graph) {
            boolean bool = false;
            for (Box box : graph) {
                if (box.connects(target)) {
                    bool = true;
                    break;
                }
            }
            if (bool) {
                result[pos] = target;
                pos++;
            }
        }
        return result;
    }

    /**
     * Hilfsmethode, die ein 2D-Int-Array arr erstellt,
     * sodass arr[i][j] für gültige i, j den Index enthält an dem die Box der i-ten Zeile und j-ten Spalte in this.graph gespeichert wird
     */
    private int[][] createIndexes() {
        int[][] result = new int[this.columnNum][this.rowNum];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                for (int k = 0; k < this.graph.length; k++) {
                    if (this.graph[k].getXPos() == i && this.graph[k].getYPos() == j) {
                        result[i][j] = k;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Main-Methode, die das Labyrinth visualisiert.
     */
    public static void main(String[] args) {
        GUI.main(new String[0]);
    }
}


