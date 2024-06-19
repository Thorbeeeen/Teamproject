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
    }

    public int[][][] getGraph() {
        return this.graph;
    }

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

    private static int[] shuffle(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int j = (int) (array.length * Math.random());
            int temp = array[j];
            array[j] = array[i];
            array[i] = temp;
        }
        return array;
    }

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
