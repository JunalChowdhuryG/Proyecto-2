import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HighScores {
    // Use CopyOnWriteArrayList for thread-safe reads, but synchronize writes
    private static final List<HighScoreEntry> topScores = new CopyOnWriteArrayList<>();
    private static final int MAX_SCORES = 3;

    public static synchronized void submitScore(char playerChar, int score) {
        if (score <= 0) {
            return; // Don't record scores of 0 or less
        }

        // Add the new score
        topScores.add(new HighScoreEntry(playerChar, score));

        // Sort the list in descending order
        Collections.sort(topScores, (s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));

        // Trim the list to the max size
        while (topScores.size() > MAX_SCORES) {
            topScores.remove(topScores.size() - 1);
        }
    }

    public static List<HighScoreEntry> getTopScores() {
        // Return a copy to prevent external modification
        return new ArrayList<>(topScores);
    }
}
