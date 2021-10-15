package cz.najemi.minesweeper;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;

public class Main {
    public static int grid_size = 5;
    public static boolean[][][] grid = null;
    public static void print_grid_debug(boolean[][][] grid) {
        for (int i = 0; i < Main.grid_size; i++) {
            String string = "";
            for (int j = 0; j < Main.grid_size; j++) {
                String str = "[";
                for (int k = 0; k < 3; k++) str += (grid[i][j][k]) ? "1" : "0";
                string += str + "]";
            }
            System.out.println(string);
        }
    }
    public static int get_surrounding_bomb_amount_old(boolean[][][] grid, int x, int y) {
        int bombs = 0;
        for (int i = -1; i <= 1; i++) for (int j = -1; j <= 1; j++) if (grid[x + i][y + j][0] == TRUE) bombs++;
        return bombs;
    }
    public static boolean[][][] get_surrounding_cells(boolean[][][] grid, int x, int y) {
        boolean[][][] output = new boolean[3][3][3];
        for (int i = x - 1; i <= x + 1; i++) if ((i >= 0)&&(i < Main.grid_size)) {
            for (int j = y - 1; j <= y + 1; j++) if ((i >= 0)&&(i < Main.grid_size)&&(j >= 0)&&(j < Main.grid_size)&&(((Object) grid[j][i]).getClass().getSimpleName() == "Boolean[]")) {
                output[j - y][i - x] = grid[j][i];
            }
        }
        return output;
    }
    public static boolean[][][] get_surrounding_cells_old(boolean[][][] grid, int x, int y) {
        boolean[][][] output = new boolean[3][3][3];
        for (int i = -1; i <= 1; i++) for (int j = -1; j <= 1; j++) output[i + 1][j + 1] = grid[x + i][y + j];
        return output;
    }
    public static int tryParse(String text) {
        try { return Integer.parseInt(text); } catch (NumberFormatException e) { return 0; }
    }
    public static void show_grid(boolean[][][] grid) {
        String[][] display_grid = new String[Main.grid_size][Main.grid_size];

        boolean all_bombs_flagged = TRUE;
        for (int i = 0; i < Main.grid_size; i++) for (int j = 0; j < Main.grid_size; j++) if ((grid[j][i][0] == TRUE)&&(grid[j][i][1] == FALSE)) all_bombs_flagged = FALSE;

        boolean bomb_exploded = FALSE;
        for (int i = 0; i < Main.grid_size; i++) for (int j = 0; j < Main.grid_size; j++) if ((grid[i][j][0] == TRUE)&&(grid[i][j][2]) == TRUE) bomb_exploded = TRUE;

        for (int i = 0; i < Main.grid_size; i++) for (int j = 0; j < Main.grid_size; j++) if ((bomb_exploded == FALSE)&&(grid[j][i][0] == FALSE)&&(grid[j][i][1] == FALSE)&&(grid[j][i][2])&&(number_of_surrounding_bombs(grid, i, j) > 0)) display_grid[j][i] = Integer.toString(number_of_surrounding_bombs(grid, i, j));

        for (int i = 0; i < Main.grid_size; i++) for (int j = 0; j < Main.grid_size; j++) if ((grid[j][i][0] == TRUE)&&(bomb_exploded)) display_grid[j][i] = "🐳"; else if ((grid[j][i][1] == TRUE)&&(bomb_exploded == FALSE)) display_grid[j][i] = "🚩"; else if ((grid[j][i][2] == TRUE)||(bomb_exploded == TRUE)) display_grid[j][i] = "❌"; else display_grid[j][i] = "🔲";


//        Runtime runtime = Runtime.getRuntime();
//        Process proc = runtime.exec("shutdown /s -t 0");
//        System.exit(0);

        if (all_bombs_flagged) System.out.println("well done. You win!"); else {
            String[] numbers = new String[Main.grid_size];
            for (int i = 0; i < Main.grid_size; i++) numbers[i] = Integer.toString(i);
            System.out.println("   " + String.join("  ", numbers));
            for (int i = 0; i < Main.grid_size; i++) System.out.println(i + " - " + String.join(" ", display_grid[i]));

            if (bomb_exploded == TRUE) System.out.println("Game Over!");
        }
    }
    public static int number_of_surrounding_bombs(boolean[][][] grid, int x, int y) {
        int bombs = 0;
        for (int i = x - 1; i <= x + 1; i++) if ((i >= 0)&&(i < Main.grid_size)) for (int j = y - 1; j <= y + 1; j++) if ((j >= 0)&&(j < Main.grid_size)&&(((Object) grid[j][i]).getClass().getSimpleName() == "Boolean[]")&&(grid[j][i][0] == TRUE)) bombs++;
        return bombs;
    }
    public static boolean[][][] open_cell(boolean[][][] grid, int x, int y) {
        if (grid[y][x][0] == TRUE) {
            grid[y][x][2] = TRUE;
        } else if (grid[y][x][1] == TRUE) {
            System.out.println("This is a flag, you cannot open this cell. Please remove the flag and then repeat this action.");
        }else if (grid[y][x][2] == TRUE) {
            System.out.println("This cell is already opened.");
        }else {
            grid[y][x][2] = TRUE;

            int surrounding_bombs = number_of_surrounding_bombs(grid, x, y);
            if (surrounding_bombs == 0) {
                int _x = -2;
                boolean[][][] surrounding_cells = get_surrounding_cells(grid, x, y);
                for (int c_1 = 0; c_1 < surrounding_cells.length; c_1++) {
                    _x ++;
                    int _y = -2;
                    for (int c_2 = 0; c_2 < surrounding_cells[c_1].length; c_2++) {
                        _y += 1;

                        if ((x + _x > -1)&&(y + _y > -1)&&(x + _x < Main.grid_size)&&(y + _y < Main.grid_size)) grid = open_cell(grid, x + _x, y + _y);

//                        if (len(c_2): # and not c_2[0])
//
                    }
                }
            }
        }
        return grid;
    }
    public static void main(String[] args) {
        // get grid_size from either user input or from main
        while (!(Main.grid_size > 4)) {
            System.out.println("How big would you like the grid to be? (min: 5)");
            Scanner in = new Scanner(System.in);
            Main.grid_size = tryParse(in.nextLine());
        }

        Main.grid = new boolean[Main.grid_size][Main.grid_size][3];

        for (int x = 0; x < Main.grid_size; x++) for (int y = 0; y < Main.grid_size; y++) for (int i = 0; i < 3; i++) Main.grid[x][y][i] = FALSE;

        int amount_of_bombs = (int) Math.round(grid_size * grid_size * 0.6);

        Pattern pattern = Pattern.compile("^f|o [0-9]+, *[0-9]+$");

        boolean valid_input = FALSE;
        char choice = 'o';
        int x = 0;
        int y = 0;
        while (valid_input == FALSE) {
            System.out.println("Use the format: o/f x, y in order to play");
            Scanner in = new Scanner(System.in);
            String input = in.nextLine();
            Matcher matcher = pattern.matcher(input);
            if (!(matcher.find())) continue;

            choice = input.charAt(0);

            String coordinates = input.substring(2);

            String[] coordinates_list = coordinates.split(",");

            if (!(coordinates_list.length == 2)) System.out.println("Something went very wrong");

            String x_string = coordinates_list[0];
            String y_string = coordinates_list[1];

            x = Integer.parseInt(x_string);
            y = Integer.parseInt(y_string);
            if ((x > -1)&&(y > -1)&&(x < Main.grid_size)&&(y < Main.grid_size)) valid_input = TRUE;
        }

        System.out.println(Main.grid);

        // x, y

        // bomb generation here
        for (int i = 0; i < amount_of_bombs; i++) {
            int random_x = x;
            while (random_x == x) random_x = ThreadLocalRandom.current().nextInt(0, Main.grid_size);
            int random_y = y;
            while (random_y == y) random_y = ThreadLocalRandom.current().nextInt(0, Main.grid_size);

            Main.grid[random_y][random_x][0] = TRUE;
        }


        print_grid_debug(Main.grid);
        show_grid(Main.grid);

        if (choice == 'f') if (Main.grid[y][x][1] == TRUE) Main.grid[y][x][1] = FALSE; else Main.grid[y][x][1] = TRUE; else Main.grid = open_cell(Main.grid, x, y); // open cell
        show_grid(Main.grid);

        print_grid_debug(Main.grid);


















    }
}
