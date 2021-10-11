package cz.najemi.minesweeper;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static boolean[][][] get_surrounding_cells(boolean[][][] grid, int x, int y) {
        boolean[][][] output = new boolean[3][3][3];
        for (int i = -1; i <= 1; i++) for (int j = -1; j <= 1; j++) output[i + 1][j + 1] = grid[x + i][y + j];
        return output;
    }
    public static int tryParse(String text) {
        try { return Integer.parseInt(text); } catch (NumberFormatException e) { return 0; }
    }
    public static void show_grid(boolean[][][] grid) {

    }
    public static int[] uncover(boolean[][][] grid) {
        return new int[]{0, 1, 2};
    }
    public static void main(String[] args) throws IOException {
        // get grid_size from either user input or from main
        while (!(Main.grid_size > 0)) {
            System.out.println("How big would you like the grid size to be?");
            Scanner in = new Scanner(System.in);
            Main.grid_size = tryParse(in.nextLine());
        }

        Main.grid = new boolean[Main.grid_size][Main.grid_size][3];

        for (int x = 0; x < Main.grid_size; x++) for (int y = 0; y < Main.grid_size; y++) for (int i = 0; i < 3; i++) Main.grid[x][y][i] = FALSE;


        int amount_of_bombs = (int) Math.round(grid_size * grid_size * 0.6);

        Pattern pattern = Pattern.compile("^f|o [0-9]+, *[0-9]+$");

        boolean match_found = FALSE;
        while (match_found == FALSE) {
            Scanner in = new Scanner(System.in);
            Matcher matcher = pattern.matcher(in.nextLine());
            match_found = matcher.find();
        }

        // bomb generation here

//        Runtime runtime = Runtime.getRuntime();
//        Process proc = runtime.exec("shutdown /s -t 0");
//        System.exit(0);










    }
}
