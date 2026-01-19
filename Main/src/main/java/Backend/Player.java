package Backend;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;


public class Player {
    private final String name;
    private int score;
    private final Paint color;

    public Player(String name, Paint color) {
        this.name = name;
        this.color = color;
        score = 0;
    }

    public String getName() {return name;}
    public Paint getColor() {return color;}

    // Score calculation
    public int getScore() {return score;}
    public void addPoints(int points) {score += points;}

    // Get player picture
    public Image getImage() {
        return new Image("/images/" + name + ".jpeg");
    }
}
