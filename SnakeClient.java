import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SnakeClient {

    private JFrame frame;
    private GamePanel gamePanel;
    private PrintWriter out;

    public SnakeClient() {
        frame = new JFrame("Snake vs Snakes");
        gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack(); // Pack before adding key listener
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyInput(e);
            }
        });
    }

    private void handleKeyInput(KeyEvent e) {
        if (out == null) return;
        char keyChar = Character.toLowerCase(e.getKeyChar());
        switch (keyChar) {
            case 'w':
            case 'a':
            case 's':
            case 'd':
                out.println(keyChar);
                break;
        }
    }

    public void connectToServer(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Thread to listen for server messages
            new Thread(() -> {
                try {
                    String fromServer;
                    while ((fromServer = in.readLine()) != null) {
                        final String gameState = fromServer;
                        // Update GUI on the Event Dispatch Thread
                        SwingUtilities.invokeLater(() -> {
                            // Check for special messages
                            if ("GAME OVER".equals(gameState)) {
                                // Could show a dialog box here
                                System.out.println("Game Over!");
                            } else if (gameState.startsWith("YOU WIN")) {
                                System.out.println("You Win!");
                            } else {
                                gamePanel.updateGameState(gameState);
                                frame.pack(); // Adjust frame size if content changes
                            }
                        });
                    }
                } catch (IOException e) {
                    System.out.println("Connection to server lost.");
                }
            }).start();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8189;

        if (args.length >= 1) {
            host = args[0];
        }
        if (args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[1]);
                System.exit(1);
            }
        }

        final String finalHost = host;
        final int finalPort = port;

        SwingUtilities.invokeLater(() -> {
            SnakeClient client = new SnakeClient();
            client.connectToServer(finalHost, finalPort);
        });
    }
}
