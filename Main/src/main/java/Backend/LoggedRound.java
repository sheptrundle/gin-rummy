package Backend;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoggedRound {
    private final Player winner;
    private final int pointsWon;

    public LoggedRound(Player winner, int pointsWon) {
        this.winner = winner;
        this.pointsWon = pointsWon;
    }

    // Return text in correct color
    public Text getText() {
        Text text = new Text(winner.getName() + " +" + pointsWon);
        text.setFill(winner.getColor());
        return text;
    }
}
