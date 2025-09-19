import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class Board {
    private int width;
    private int height;
    private char[][] grid;
    private Random random = new Random();

    // Level 1: Open field
    private static final String[] LEVEL_1_MAP = {
        "########################################",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "########################################"
    };

    // Level 2: Center blocks
    private static final String[] LEVEL_2_MAP = {
        "########################################",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#        ##########    ##########      #",
        "#        #                     #       #",
        "#        #                     #       #",
        "#        ##########    ##########      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#        ##########    ##########      #",
        "#        #                     #       #",
        "#        #                     #       #",
        "#        ##########    ##########      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "#                                      #",
        "########################################"
    };

    // Level 3: Tunnels
    private static final String[] LEVEL_3_MAP = {
        "########################################",
        "#                                      #",
        "# ################### ################ #",
        "# #                   #              # #",
        "# # ################### ############ # #",
        "# # #                                # #",
        "# # # #################### ######### # #",
        "# # # #                  # #       # # #",
        "# # # # ################ # # ##### # # #",
        "# # # # #              # # # #   # # # #",
        "# # # # # ############ # # # #   # # # #",
        "# # # # # #          # # # # ##### # # #",
        "# # # # # ############ # # #       # # #",
        "# # # # #              # # ######### # #",
        "# # # ################## #           # #",
        "# # #                    ############# #",
        "# #################################### #",
        "#                                      #",
        "#                                      #",
        "########################################"
    };

    private static final List<String[]> LEVELS = new ArrayList<>();
    static {
        LEVELS.add(LEVEL_1_MAP);
        LEVELS.add(LEVEL_2_MAP);
        LEVELS.add(LEVEL_3_MAP);
    }

    public Board(int level) {
        initBoard(level);
    }

    private void initBoard(int level) {
        String[] map = LEVELS.get(level - 1);
        this.height = map.length;
        this.width = map[0].length();
        this.grid = new char[height][width];
        for (int i = 0; i < height; i++) {
            this.grid[i] = map[i].toCharArray();
        }
        placeFruit();
    }

    public List<Point> getSafeSpawnPoints() {
        List<Point> safePoints = new ArrayList<>();
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (grid[y][x] == ' ') {
                    safePoints.add(new Point(x, y));
                }
            }
        }
        return safePoints;
    }

    public void placeFruit() {
        List<Point> emptyCells = getSafeSpawnPoints();
        if (emptyCells.isEmpty()) {
            return; // No space left
        }
        Point p = emptyCells.get(random.nextInt(emptyCells.size()));
        char fruit = (char) (random.nextInt(9) + '1');
        grid[p.y][p.x] = fruit;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static int getMaxLevels() {
        return LEVELS.size();
    }

    public char getCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return '#'; // Treat out of bounds as a wall
        }
        return grid[y][x];
    }

    public void setCell(int x, int y, char value) {
        if (x > 0 && x < width - 1 && y > 0 && y < height - 1) {
            grid[y][x] = value;
        }
    }

    public char[][] getGrid() {
        return grid;
    }
}
