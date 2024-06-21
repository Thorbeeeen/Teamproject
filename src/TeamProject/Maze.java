package TeamProject;

public class Maze {

    private final int playerWidth = 8;
    private final int playerHeight = 8;

    private final int columnNum;
    private final int rowNum;
    private final Player player;

    private final Box[] graph;

    public Maze(int columnNum, int rowNum) {
        this.columnNum = columnNum;
        this.rowNum = rowNum;
        this.player = new Player(this.playerWidth, this.playerHeight);

        this.graph = createMaze(columnNum, rowNum);
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

    public Box getBoxAtPosition(int xpos, int ypos) {
        return getBoxAtPosition(this.graph, xpos, ypos);
    }

    private Box getBoxAtPosition(Box[] graph, int xpos, int ypos) {
        for (Box box : graph) {
            if (box.getXPos() == xpos && box.getYPos() == ypos) return box;
        }
        return null;
    }

    /**
    * Hilfsmethode, die ein zufälliges Labyrinth erzeugt der lösbar ist.
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

    public static void main(String[] args) {
        GUI.main(new String[0]);
    }
}
