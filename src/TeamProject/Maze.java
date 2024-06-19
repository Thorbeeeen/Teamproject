/* Idee:
* Klasse Zelle oder Box zu programmieren die eine Box des Labyrinths darstellt.
* Und dann this.graph durch Box[] statt int[][][] darszustellen.
* Vorteile sind wahrscheinlich Übersichtlichkeit und Erweiterbarkeit mit zum Beispiel Hinblick auf Tunnel, Schlüssel, Türen oder Items.
*/

package TeamProject;

import java.util.Arrays;

public class Maze {

    private final int columnNum;
    private final int rowNum;
    private final Player player;

    private final int[][][] graph;

    public Maze(int columnNum, int rowNum) {
        this.columnNum = columnNum;
        this.rowNum = rowNum;
        this.player = new Player();

        this.graph = createMaze(columnNum, rowNum); 
        /* 
        * this.graph speichert für jede Box die anliegenden Wände,
        * indem für 0 <= i < this.columnNum, 0 <= j < this.rowNum und 0 <= k < 4,  
        * grapgh[i][j][k] gleich 1 ist falls die Box in der (i + 1)-ten Spalten und (j + 1)-ten Zeile in k-ter Richutng eine Wand befindet.
        * Mit k-ter Richtung im Fall k = 0 ist rechts bzw, k = 1 ist unten, k = 2 ist links, k = 3 oben. 
        */
    }

    public int[][][] getGraph() {
        return this.graph;
    }

    /**
    * Hilfsmethode die ein zufälliges Labyrinth erzeugt der lösbar ist.
    */
    private static int[][][] createMaze(int columns, int rows) {
        int[][][] graph = new int[columns][rows][4];
        createPath(graph, 0, 0, new int[][] {{columns - 1, rows - 1}});
        int[][] unreachableIndexes = computeUnreachableIndexes(graph);
        while (unreachableIndexes.length != 0) {
            int[] startingPosition = unreachableIndexes[(int) (unreachableIndexes.length * Math.random())];
            createPath(graph, startingPosition[0], startingPosition[1], computeReachableIndexes(graph));
            unreachableIndexes = computeUnreachableIndexes(graph);
        }
        return graph;

    }

    /**
    * Hilsmethode die einen zufälligen Weg von der Box in der (xpos + 1)-ten Spalten und (ypos + 1)-ten Reihe zu den in targets gespeicherten Boxen in maze erzeugt.
    * Das i-te Element aus targets speichert hier das i-te target und wird als ein Array der Länge 2 dargestellt, wobei das erste Element die Spaltennummer minus 1 der Box ist 
    * und das zweite Element die Zeilennummer minus 1 der Box ist.
    */
    private static boolean createPath(int[][][] maze, int xpos, int ypos, int[][] targets) {
        if (maze.length <= xpos || maze[0].length <= ypos) return false;
        if (xpos < 0 || ypos < 0) return false;
        for (int i = 0; i < targets.length; i++) {
            if (targets[i][0] == xpos && targets[i][1] == ypos) return true;
        }
        if (Arrays.stream(maze[xpos][ypos]).sum() != 0) return false;

        for (int direction : shuffle(new int[] {0, 1, 2, 3})) {
            boolean foundPath = false;

            switch (direction) {
                case 0:
                    maze[xpos][ypos][direction] = 1;
                    foundPath = createPath(maze, xpos + 1, ypos, targets);
                    if (foundPath) maze[xpos + 1][ypos][(direction + 2) % 4] = 1;
                    break;

                case 1:
                    maze[xpos][ypos][direction] = 1;
                    foundPath = createPath(maze, xpos, ypos + 1, targets);
                    if (foundPath) maze[xpos][ypos + 1][(direction + 2) % 4] = 1;
                    break;

                case 2:
                    if (ypos == maze[0].length - 1 || ypos == 0) continue;
                    maze[xpos][ypos][direction] = 1;
                    foundPath = createPath(maze, xpos - 1, ypos, targets);
                    if (foundPath) maze[xpos - 1][ypos][(direction + 2) % 4] = 1;
                    break;

                case 3:
                    if (xpos == maze.length - 1 || xpos == 0) continue;
                    maze[xpos][ypos][direction] = 1;
                    foundPath = createPath(maze, xpos, ypos - 1, targets);
                    if (foundPath) maze[xpos][ypos - 1][(direction + 2) % 4] = 1;
                    break;
            }

            if (foundPath) return true;
            else maze[xpos][ypos][direction] = 0;
        }
        return false;
    }

    /**
    * Hilfsmethode für createMaze, die ein int-Array mischt, um im erstellten Labyrinth aus createMaze für Zufall zusorgen.
    */
    private static int[] shuffle(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int j = (int) (array.length * Math.random());
            int temp = array[j];
            array[j] = array[i];
            array[i] = temp;
        }
        return array;
    }

    /**
    * Hilfmethode für createMaze, die alle Boxen berechnet die NICHT von der oberen linken Box erreicht werden können.
    * Die Ausgabe wird wie der Parameter targets in createPath repräsentiert. 
    */
    private static int[][] computeUnreachableIndexes(int[][][] graph) {
        int length = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                if (Arrays.stream(graph[i][j]).sum() == 0) length++;
            }
        }
        int[][] result = new int[length][2];
        int pos = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                if (Arrays.stream(graph[i][j]).sum() != 0) continue;
                result[pos] = new int[] {i, j};
                pos++;
            }
        }
        return result;
    }

    /**
    * Hilfmethode für createMaze, die alle Boxen berechnet die von der oberen linken Box erreicht werden können.
    * Die Ausgabe wird wie der Parameter targets in createPath repräsentiert. 
    */
    private static int[][] computeReachableIndexes(int[][][] graph) {
        int length = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                if (Arrays.stream(graph[i][j]).sum() != 0) length++;
            }
        }
        int[][] result = new int[length][2];
        int pos = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                if (Arrays.stream(graph[i][j]).sum() == 0) continue;
                result[pos] = new int[] {i, j};
                pos++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        GUI.main(new String[0]);
    }
}
