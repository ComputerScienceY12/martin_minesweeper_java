package cz.najemi.minesweeper;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;

public class Main {
    public static int grid_size = 20;
    public static boolean[][][] grid = null;
    public static String[] get_valid_input() {
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

        return new String[]{Integer.toString(x), Integer.toString(y), Character.toString(choice)};
    }
//    public static void print_grid_debug(boolean[][][] grid) {
//        for (int i = 0; i < Main.grid_size; i++) {
//            StringBuilder string = new StringBuilder();
//            for (int j = 0; j < Main.grid_size; j++) {
//                StringBuilder str = new StringBuilder("[");
//                for (int k = 0; k < 3; k++) str.append((grid[i][j][k]) ? "1" : "0");
//                string.append(str).append("]");
//            }
//            System.out.println(string);
//        }
//    }
    public static boolean[][][] get_surrounding_cells(boolean[][][] grid, int x, int y) {
        boolean[][][] output = new boolean[3][3][3];
        for (int i = x - 1; i <= x + 1; i++) if ((i >= 0)&&(i < Main.grid_size)) for (int j = y - 1; j <= y + 1; j++) if ((j >= 0)&&(j < Main.grid_size)&&(((Object) grid[j][i]).getClass().getSimpleName().equals("Boolean[]"))) output[j - y][i - x] = grid[j][i];
        return output;
    }

    public static String[][][] convert_grid_to_string(boolean[][][] grid) {
        String[][][] new_grid = new String[Main.grid_size][Main.grid_size][4];
        for (int i = 0; i < Main.grid_size; i++) for (int j = 0; j < Main.grid_size; j++) {
            for (int k = 0; k < 3; k++) if (grid[i][j][k] == TRUE) new_grid[i][j][k] = "1"; else new_grid[i][j][k] = "0";
            new_grid[i][j][3] = "";
        }
        return new_grid;
    }
    public static boolean show_grid(String[][][] grid) {
        boolean all_bombs_flagged = TRUE;
        for (int i = 0; i < Main.grid_size; i++) for (int j = 0; j < Main.grid_size; j++) if ((Objects.equals(grid[j][i][0], "1"))&&(Objects.equals(grid[j][i][1], "0"))) all_bombs_flagged = FALSE;

        boolean bomb_exploded = FALSE;
        for (int i = 0; i < Main.grid_size; i++) for (int j = 0; j < Main.grid_size; j++) if ((Objects.equals(grid[i][j][0], "1"))&& Objects.equals(grid[i][j][2], "1")) bomb_exploded = TRUE;

        for (int i = 0; i < Main.grid_size; i++) for (int j = 0; j < Main.grid_size; j++) if ((bomb_exploded == FALSE)&&(Objects.equals(grid[j][i][0], "0"))&&(Objects.equals(grid[j][i][1], "0"))&&(Objects.equals(grid[j][i][2], "1"))&&(number_of_surrounding_bombs(grid, i, j) > 0)) grid[j][i][3] = Integer.toString(number_of_surrounding_bombs(grid, i, j));

        for (int i = 0; i < Main.grid_size; i++) for (int j = 0; j < Main.grid_size; j++) if (Objects.equals(grid[j][i][3], "")) if ((Objects.equals(grid[j][i][0], "1"))&&(bomb_exploded)) grid[j][i][3] = "????"; else if ((Objects.equals(grid[j][i][1], "1"))&&(bomb_exploded == FALSE)) grid[j][i][3] = "????"; else if ((Objects.equals(grid[j][i][2], "1"))||(bomb_exploded == TRUE)) grid[j][i][3] = "???"; else grid[j][i][3] = "????";

//        Runtime runtime = Runtime.getRuntime();
//        Process proc = runtime.exec("shutdown /s -t 0");
//        System.exit(0);

        if (all_bombs_flagged) {
            System.out.println("well done. You win!");
            return FALSE;
        }
        String[] numbers_1 = new String[Main.grid_size];
        for (int i = 0; i < Main.grid_size; i++) numbers_1[i] = Integer.toString(i);
        System.out.println("   " + String.join("  ", numbers_1));
        String[][] values = new String[Main.grid_size][Main.grid_size];
        for (int i = 0; i < Main.grid_size; i++) for (int j = 0; j < Main.grid_size; j++) values[i][j] = grid[i][j][3];
        for (int i = 0; i < Main.grid_size; i++) System.out.println(i + " - " + String.join(" ", values[i]));

        if (bomb_exploded == TRUE) {
            System.out.println("Game Over!");
            return FALSE;
        }
        return TRUE;
    }
    public static int number_of_surrounding_bombs(String[][][] grid, int x, int y) {
        int bombs = 0;
        for (int i = x - 1; i <= x + 1; i++) if ((i >= 0)&&(i < Main.grid_size)) for (int j = y - 1; j <= y + 1; j++) if ((j >= 0)&&(j < Main.grid_size)&&(Objects.equals(((Object) grid[j][i]).getClass().getSimpleName(), "String[]"))&&(Objects.equals(grid[j][i][0], "1"))) bombs++;
        return bombs;
    }
    public static boolean[][][] open_cell(boolean[][][] grid, int x, int y, boolean suppress_output) {
        if (grid[y][x][0] == TRUE) grid[y][x][2] = TRUE; else if (grid[y][x][1] == TRUE) if (suppress_output == FALSE) System.out.println("This is a flag, you cannot open this cell. Please remove the flag and then repeat this action."); else ; else if (grid[y][x][2] == TRUE) if (suppress_output == FALSE) System.out.println("This cell is already opened."); else ; else {
            grid[y][x][2] = TRUE;
            if (number_of_surrounding_bombs(convert_grid_to_string(grid), x, y) == 0) {
                int _x = -2;
                boolean[][][] surrounding_cells = get_surrounding_cells(grid, x, y);
                for (boolean[][] surrounding_cell : surrounding_cells) {
                    _x++;
                    int _y = -2;
                    for (int c_2 = 0; c_2 < surrounding_cell.length; c_2++) {
                        _y += 1;
                        if ((x + _x > -1) && (y + _y > -1) && (x + _x < Main.grid_size) && (y + _y < Main.grid_size))
                            grid = open_cell(grid, x + _x, y + _y, TRUE);
                    }
                }
            }
        }
        return grid;
    }
    public static void main(String[] args) {
        // get grid_size from either user input or from main
        Pattern pattern = Pattern.compile("^[0-9]+$");

        while (!(Main.grid_size > 4)) {
            System.out.println("How big would you like the grid to be? (min: 5)");
            Scanner in = new Scanner(System.in);
            String input = in.nextLine();
            Matcher matcher = pattern.matcher(input);
            if (!(matcher.find())) continue;
            Main.grid_size = Integer.parseInt(input);
        }

        Main.grid = new boolean[Main.grid_size][Main.grid_size][3];

        for (int x = 0; x < Main.grid_size; x++) for (int y = 0; y < Main.grid_size; y++) for (int i = 0; i < 3; i++) Main.grid[x][y][i] = FALSE;

        int amount_of_bombs = (int) Math.round(grid_size * grid_size * 0.3);

        String[] valid_input = get_valid_input();
        int x = Integer.parseInt(valid_input[0]);
        int y = Integer.parseInt(valid_input[0]);
        char choice = valid_input[2].charAt(0);

        // bomb generation here
        for (int i = 0; i < amount_of_bombs; i++) {
            int random_x = 0;
            int random_y = 0;
            boolean appropriate_location = FALSE;
            while (appropriate_location == FALSE) {
                random_x = ThreadLocalRandom.current().nextInt(0, Main.grid_size);
                random_y = ThreadLocalRandom.current().nextInt(0, Main.grid_size);

                appropriate_location = TRUE;

                for (int j = 0; j < 3; j++) for (int k = 0; k < 3; k++) if ((random_x == x + j - 1)&&(random_y == x + k - 1)&&(Main.grid[random_y][random_x][0] == FALSE)) appropriate_location = FALSE;
            }

            Main.grid[random_y][random_x][0] = TRUE;
        }

        show_grid(convert_grid_to_string(Main.grid));

        if (choice == 'f') if (Main.grid[y][x][1] == TRUE) Main.grid[y][x][1] = FALSE; else Main.grid[y][x][1] = TRUE; else Main.grid = open_cell(Main.grid, x, y, FALSE); // open cell

        boolean still_playing = show_grid(convert_grid_to_string(Main.grid));

        while (still_playing) {
            valid_input = get_valid_input();
            x = Integer.parseInt(valid_input[0]);
            y = Integer.parseInt(valid_input[0]);
            choice = valid_input[2].charAt(0);

            if (choice == 'f') if (Main.grid[y][x][1] == TRUE) Main.grid[y][x][1] = FALSE; else Main.grid[y][x][1] = TRUE; else Main.grid = open_cell(Main.grid, x, y, FALSE); // open cell

            still_playing = show_grid(convert_grid_to_string(Main.grid));
        }
    }
}
