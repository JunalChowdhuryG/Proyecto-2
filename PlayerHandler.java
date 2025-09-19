import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Random;

public class PlayerHandler implements Runnable {
    private Socket socket;
    private Game game;
    private Snake snake;
    private PrintWriter out;
    private List<PlayerHandler> players;
    private volatile boolean running = true;

    public PlayerHandler(Socket socket, Game game, char playerChar, List<PlayerHandler> players) {
        this.socket = socket;
        this.game = game;
        this.players = players;

        // Find a safe starting position
        Random rand = new Random();
        int startX, startY;
        // A simple way to avoid spawning on a wall. A more robust method would check for other snakes.
        startX = rand.nextInt(36) + 2; // Board width 40, so 2 to 37
        startY = rand.nextInt(16) + 2; // Board height 20, so 2 to 17

        this.snake = new Snake(startX, startY, playerChar);
        this.game.addSnake(this.snake);

        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            while (running && (inputLine = in.readLine()) != null) {
                switch (inputLine.trim().toLowerCase()) {
                    case "w":
                        snake.setDirection(Snake.Direction.UP);
                        break;
                    case "s":
                        snake.setDirection(Snake.Direction.DOWN);
                        break;
                    case "a":
                        snake.setDirection(Snake.Direction.LEFT);
                        break;
                    case "d":
                        snake.setDirection(Snake.Direction.RIGHT);
                        break;
                    case "quit":
                        running = false;
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Player " + snake.getBodyChar() + " disconnected: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public void sendMessage(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
        }
    }

    public void closeConnection() {
        this.running = false;
        if (snake != null) {
            game.removeSnake(snake);
        }
        players.remove(this);
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Player " + (snake != null ? snake.getBodyChar() : "") + " connection closed.");
    }

    public Snake getSnake() {
        return snake;
    }
}
