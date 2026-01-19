package Backend;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class LoggedRound {
    private final Player winner;
    private final int pointsWon;

    public LoggedRound(Player winner, int pointsWon) {
        this.winner = winner;
        this.pointsWon = pointsWon;
    }

    public LoggedRound() {
        this.winner = null;
        this.pointsWon = 0;
    }

    // Return text in correct color
    public Text getText() {
        // Tie
        if (winner == null) {
            Text text = new Text("Round tied");
            text.setFill(Paint.valueOf("gray"));
            return text;
        }

        // Player won
        Text text = new Text(winner.getName() + " +" + pointsWon);
        text.setFill(winner.getColor());
        return text;
    }
}
