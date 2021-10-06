package cz.najemi.minesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;

public class Main {
    public static int grid_size = 0;
    public static boolean[][][] grid = null;
    public static int get_surrounding_bomb_amount(boolean[][][] grid, int x, int y) {
        int bombs = 0;
        for (int i = -1; i <= 1; i++) for (int j = -1; j <= 1; j++) if (grid[x + i][y + j][0] == TRUE) bombs++;
        return bombs;
    }
    public static int tryParse(String text) {
        try { return Integer.parseInt(text); } catch (NumberFormatException e) { return 0; }
    }
    public static void show_grid(boolean[][][] grid) {

    }
    public static int[] uncover(boolean[][][] grid) {
        return new int[]{0, 1, 2};
    }
    public static void main(String[] args) {
        // get grid_size from either user input or from main
        while (!(Main.grid_size > 0)) {
            System.out.println("How big would you like the grid size to be?");
            Scanner in = new Scanner(System.in);
            Main.grid_size = tryParse(in.nextLine());
        }

        Main.grid = new boolean[Main.grid_size][Main.grid_size][3];

        for (int x = 0; x < Main.grid_size; x++) for (int y = 0; y < Main.grid_size; y++) for (int i = 0; i < 3; i++) Main.grid[x][y][i] = FALSE;










    }
}
