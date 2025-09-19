import java.awt.Point;
import java.util.LinkedList;

public class Snake {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private LinkedList<Point> body = new LinkedList<>();
    private Direction direction = Direction.RIGHT;
    private char bodyChar;
    private int score = 0;
    private int growthPending = 0;

    public Snake(int startX, int startY, char bodyChar) {
        this.bodyChar = bodyChar;
        // Start with a body of 3 segments
        body.add(new Point(startX, startY));
        body.add(new Point(startX - 1, startY));
        body.add(new Point(startX - 2, startY));
    }

    public void move() {
        Point head = body.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case UP:
                newHead.y--;
                break;
            case DOWN:
                newHead.y++;
                break;
            case LEFT:
                newHead.x--;
                break;
            case RIGHT:
                newHead.x++;
                break;
        }

        body.addFirst(newHead);

        if (growthPending > 0) {
            growthPending--;
        } else {
            body.removeLast();
        }
    }

    public void grow(int amount) {
        growthPending += amount;
        score += amount;
    }

    public void setDirection(Direction newDirection) {
        // Prevent the snake from reversing
        if (direction == Direction.UP && newDirection == Direction.DOWN) return;
        if (direction == Direction.DOWN && newDirection == Direction.UP) return;
        if (direction == Direction.LEFT && newDirection == Direction.RIGHT) return;
        if (direction == Direction.RIGHT && newDirection == Direction.LEFT) return;
        this.direction = newDirection;
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public Point getHead() {
        return body.getFirst();
    }

    public char getBodyChar() {
        return bodyChar;
    }

    public int getScore() {
        return score;
    }

    public boolean checkSelfCollision() {
        Point head = getHead();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void reset(int startX, int startY) {
        body.clear();
        this.direction = Direction.RIGHT;
        // Start with a body of 3 segments
        body.add(new Point(startX, startY));
        body.add(new Point(startX - 1, startY));
        body.add(new Point(startX - 2, startY));
    }
}
