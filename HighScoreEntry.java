public class HighScoreEntry {
    private final char playerChar;
    private final int score;

    public HighScoreEntry(char playerChar, int score) {
        this.playerChar = playerChar;
        this.score = score;
    }

    public char getPlayerChar() {
        return playerChar;
    }

    public int getScore() {
        return score;
    }
}
