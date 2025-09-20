import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

public class GamePanel extends JPanel {

    private static final int CELL_SIZE = 20; // Size of each cell in pixels
    private char[][] grid;
    private List<String> scoreboard = new ArrayList<>();
    private String levelUpInfo = "";

    public void updateGameState(String gameState) {
        // The game state string includes ANSI escape codes and multiple sections.
        // We need to parse it carefully.
        String[] lines = gameState.split("\n");
        List<String> boardLines = new ArrayList<>();

        boolean boardSection = false;
        boolean scoreSection = false;

        // Clear previous data
        scoreboard.clear();

        for (String line : lines) {
            if (line.contains("--- Snake vs Snakes ---")) {
                boardSection = true;
                continue;
            }
            if (line.contains("--- All-Time High Scores ---") || line.contains("--- Current Players ---")) {
                boardSection = false;
                scoreSection = true;
                scoreboard.add(line); // Add the header
                continue;
            }
             if (line.contains("--------------------")) {
                scoreSection = false;
                continue;
            }
            if(line.contains("Level Up In:")) {
                levelUpInfo = line;
                continue;
            }

            if (boardSection && line.startsWith("#")) {
                boardLines.add(line);
            }
            if (scoreSection) {
                scoreboard.add(line);
            }
        }

        if (!boardLines.isEmpty()) {
            int height = boardLines.size();
            int width = boardLines.get(0).length();
            grid = new char[height][width];
            for (int i = 0; i < height; i++) {
                grid[i] = boardLines.get(i).toCharArray();
            }
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        if (grid == null) {
            return new Dimension(800, 600); // Default size
        }
        // Calculate size based on grid and scoreboard
        int width = grid[0].length * CELL_SIZE;
        int height = (grid.length * CELL_SIZE) + (scoreboard.size() * 15) + 50;
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (grid == null) {
            return;
        }

        // Draw the game grid
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                char cell = grid[y][x];
                switch (cell) {
                    case '#':
                        g.setColor(Color.DARK_GRAY);
                        break;
                    case 'O': // Snake Head
                        g.setColor(Color.RED);
                        break;
                    case ' ':
                        g.setColor(Color.BLACK);
                        break;
                    default:
                        if (Character.isDigit(cell)) { // Fruit
                            g.setColor(Color.YELLOW);
                        } else { // Snake Body
                            g.setColor(Color.GREEN);
                        }
                        break;
                }
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // Draw the scoreboard and other info below the grid
        g.setColor(Color.WHITE);
        int currentY = grid.length * CELL_SIZE + 20;
        for (String scoreLine : scoreboard) {
            g.drawString(scoreLine, 10, currentY);
            currentY += 15;
        }
        g.drawString(levelUpInfo, 10, currentY);
    }
}
