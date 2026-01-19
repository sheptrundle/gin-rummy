package Controllers;

import Backend.Player;
import Database.DatabaseDriver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class LiveGameController {
    @FXML Label p1name;
    @FXML Label p2name;
    @FXML Label p1score;
    @FXML Label p2score;
    @FXML Label turnLabel;
    @FXML Label errorLabel;
    @FXML TextField p1textField;
    @FXML TextField p2textField;

    private int score1;
    private int score2;
    private int scoreToWin;
    private Player[] players;
    private int goesFirst;
    private boolean isLive;
    private DatabaseDriver db;

    public void setUp(String p1, String p2, int scoreToWin) {
        // Set fields
        players = new Player[] {
                new Player(p1, javafx.scene.paint.Paint.valueOf("blue")),
                new Player(p2, javafx.scene.paint.Paint.valueOf("orange"))
        };

        // Set player names
        p1name.setText(p1);
        p2name.setText(p2);

        // Set score to win
        this.scoreToWin = scoreToWin;
        isLive = true;

        // Pick who goes first
        goesFirst = (int) (Math.random() * 2);

        // Update turn label
        turnLabel.setText(players[goesFirst].getName() + " goes first this round");
    }

    public void setDB(DatabaseDriver db) {
        this.db = db;
    }

    @FXML
    public void handleQuitGame(ActionEvent actionEvent)  {
        try {
            // Load the start screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/start-screen.fxml"));
            Parent root = loader.load();

            // Set up controller
            StartScreenController controller = loader.getController();
            controller.setDB(db);

            // Switch scenes
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSubmitScore(ActionEvent event) throws SQLException {
        if (!isLive) {
            showError("Game has already ended");
            return;
        }
        errorLabel.setVisible(false);

        int addP1 = textFieldToInt(p1textField);
        int addP2 = textFieldToInt(p2textField);

        // Check if they both have an input
        if (addP1 > 0 && addP2 > 0) {
            showError("Cannot submit two scores at once");
            return;
        }

        // Add points
        else {
            score1 += addP1;
            score2 += addP2;
        }

        endTurn();

    }

    public void endTurn() throws SQLException {
        p1score.setText(String.valueOf(score1));
        p2score.setText(String.valueOf(score2));

        checkGameEnd();

        swapTurns();
        turnLabel.setText(players[goesFirst].getName() + " goes first this round");

        p1textField.clear();
        p2textField.clear();
    }

    private void swapTurns() {
        if (goesFirst == 0) {
            goesFirst = 1;
        } else if (goesFirst == 1) {
            goesFirst = 0;
        }
    }

    private void checkGameEnd() throws SQLException {
        if (score1 >= scoreToWin || score2 >= scoreToWin) {
            isLive = false;
            int winner;
            // Find winner
            if (score1 >= scoreToWin) {
                winner = 0;
            } else {
                winner = 1;
            }

            showError(players[winner].getName() + " wins");
            db.increment(players[0], players[1], players[winner]);
            db.commit();
        }
    }

    private int textFieldToInt(TextField field) {
        String text = field.getText().trim();
        if (text.isEmpty()) return 0;
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            showError("Invalid number: " + text);
            return 0;
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
