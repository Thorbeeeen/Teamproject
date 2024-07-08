package TeamProject;

import java.util.Random;

public class Maze {

    private static final double DEFAULT_PLAYER_WIDTH = 0.2;
    private static final double DEFAULT_PLAYER_HEIGHT = 0.2;

    private final int columnNum;
    private final int rowNum;

    private Player player;
    private Box[] graph;
    private int[][] indexes;


    private int collectedLetters = 0;
    public String word;


    // Bedeutung von Difficulty:
    //
    // 1 <---> leichte Wörter
    // 2 <---> schwere Wörter
    // 0 <---> ohne Wörter FEHLT NOCH
    // 3 <---> ohne Wörter mit Portal FEHLT NOCH
    // 4 <---> ohne Wörter mit Flashlight FEHLT NOCH
    //
    private int Difficulty = 1;



    public Maze(int columnNum, int rowNum) {
        this.columnNum = columnNum;
        this.rowNum = rowNum;


        // Erstelle Labyrinth mit Spieler:
        this.player = new Player(DEFAULT_PLAYER_WIDTH, DEFAULT_PLAYER_HEIGHT);
        this.graph = createMaze(columnNum, rowNum);
        this.indexes = createIndexes();
        this.graph[30].addPortal(this.graph[165]);


        // Initialisiere die Buchstaben:
        Items wordsGenerator = new Items();
        if(getDifficulty() == 1) this.word = wordsGenerator.getEasyWord();
        if(getDifficulty() == 2) this.word = wordsGenerator.getHardWord();
        else this.word = wordsGenerator.getWord();
        addLetters(word);
    }

    public void setDifficulty(int difficulty) {
        this.Difficulty = difficulty;
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

    public int getDifficulty() {
        return Difficulty;
    }

    public String getWord() {
        return this.word;
    }

    public Box getBoxAtPosition(int xpos, int ypos) {
        if (0 <= xpos && xpos < columnNum && 0 <= ypos && ypos < rowNum) return this.graph[indexes[xpos][ypos]];
        else return null;
    }

    private Box getBoxAtPosition(Box[] graph, int xpos, int ypos) {
        for (Box box : graph) {
            if (box.getXPos() == xpos && box.getYPos() == ypos) return box;
        }
        return null;
    }

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

    public void movePlayer(double deltaX, double deltaY) {
        double leftx = this.player.getXPos();
        double rightx = this.player.getXPos() + this.player.getWidth();
        double uppery = this.player.getYPos();
        double bottomy = this.player.getYPos() + this.player.getHeight();

        Box upperleftBox = getBoxAtPosition((int) leftx, (int) uppery);
        Box upperrightBox = getBoxAtPosition((int) rightx, (int) uppery);
        Box bottomleftBox = getBoxAtPosition((int) leftx, (int) bottomy);
        Box bottomrightBox = getBoxAtPosition((int) rightx, (int) bottomy);

        if (deltaX > 0) {
            if ((int) rightx == (int) (rightx + deltaX)) {
                this.player.setXPos(this.player.getXPos() + deltaX);
            }
            else if (upperrightBox == bottomrightBox && upperrightBox.connects(getBoxAtPosition((int) (rightx + deltaX), (int) uppery))) {
                this.player.setXPos(this.player.getXPos() + deltaX);
            }
        } else if (deltaX < 0) {
            if ((int) leftx == Math.floor(leftx + deltaX)) {
                this.player.setXPos(this.player.getXPos() + deltaX);
            }
            else if (upperleftBox == bottomleftBox && upperleftBox.connects(getBoxAtPosition((int) (leftx + deltaX), (int) uppery))) {
                this.player.setXPos(this.player.getXPos() + deltaX);
            }
        }
        if (deltaY > 0) {
            if ((int) bottomy == (int) (bottomy + deltaY)) {
                this.player.setYPos(this.player.getYPos() + deltaY);
            }
            else if (bottomrightBox == bottomleftBox && bottomleftBox.connects(getBoxAtPosition((int) leftx, (int) (bottomy + deltaY)))) {
                this.player.setYPos(this.player.getYPos() + deltaY);
            }
        } else if (deltaY < 0) {
            if ((int) uppery == Math.floor(uppery + deltaY)) {
                this.player.setYPos(this.player.getYPos() + deltaY);
            }
            else if (upperrightBox == upperleftBox && upperleftBox.connects(getBoxAtPosition((int) leftx, (int) (uppery + deltaY)))) {
                this.player.setYPos(this.player.getYPos() + deltaY);
            }
        }

        // Falls wir auf einem Item sind, sammeln wir es
        for(Box box : this.graph){
            Items item = box.getItem();
            if(item != null && this.player.touchesItem(item)){
                box.setItem(null);
                this.collectedLetters++;
            }
        }
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
    }

    public void reset() {
        this.player = new Player(DEFAULT_PLAYER_WIDTH, DEFAULT_PLAYER_HEIGHT);
        this.graph = createMaze(columnNum, rowNum);
        this.indexes = createIndexes();

        Items wordGenerator = new Items();
        if(getDifficulty() == 1) this.word = wordGenerator.getEasyWord();
        if(getDifficulty() == 2) this.word = wordGenerator.getHardWord();
        else this.word = wordGenerator.getWord();
        addLetters(word);
    }

    /**
    * Hilfsmethode, die ein zufälliges Labyrinth erzeugt, das lösbar ist.
    */
    private Box[] createMaze(int columnNum, int rowNum) {
        Box[] graph = new Box[columnNum * rowNum];
        for (int i = 0; i < columnNum * rowNum; i++) {
            graph[i] = new Box(i % columnNum, i / rowNum);
        }
        createPath(graph, graph[0], new Box[] {graph[columnNum * rowNum - 1]});
        Box[] unreachableBoxes = computeUnreachableBoxes(graph);
        while (unreachableBoxes.length != 0) {
            Box startingBox = unreachableBoxes[(int) (unreachableBoxes.length * Math.random())];
            createPath(graph, startingBox, computeReachableBoxes(graph));
            unreachableBoxes = computeUnreachableBoxes(graph);
        }
        return graph;
    }

    /**
    * Hilfsmethode die einen zufälligen Weg von der Box in der (xpos + 1)-ten Spalten und (ypos + 1)-ten Reihe zu den in targets gespeicherten Boxen in maze erzeugt.
    * Das i-te Element aus targets speichert hier das i-te target und wird als ein Array der Länge 2 dargestellt, wobei das erste Element die Spaltennummer minus 1 der Box ist 
    * und das zweite Element die Zeilennummer minus 1 der Box ist.
    */
    private boolean createPath(Box[] graph, Box start, Box[] targets) {
        for (Box target : targets) {
            if (target == start) return true;
        }
        if (start.countNeighbors() != 0) return false;

        for (int direction : shuffle(new int[] {0, 1, 2, 3})) {
            Box next = null;

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
                    if(start.getXPos() == 0) continue;
                    if (start.getYPos() == this.rowNum - 1 || start.getYPos() == 0) continue;
                    next = getBoxAtPosition(graph, start.getXPos() - 1, start.getYPos());
                    break;

                case 3:
                    if(start.getYPos() == 0) continue;
                    if (start.getXPos() == this.columnNum - 1 || start.getXPos() == 0) continue;
                    next = getBoxAtPosition(graph, start.getXPos(), start.getYPos() - 1);
                    break;
            }

            if (next == null) continue;
            start.addConnection(next);
            boolean foundPath = createPath(graph, next, targets);
            if (foundPath) next.addConnection(start);
            if (foundPath) return true;
            else start.delConnection(next);
        }
        return false;
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
