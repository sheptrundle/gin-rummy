package Controllers;

import Database.DatabaseDriver;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class StartScreenController {
    @FXML private ComboBox<String> player1;
    @FXML private ComboBox<String> player2;
    @FXML private Label errorLabel;
    @FXML private TextField scoreToWin;

    private DatabaseDriver db;

    @FXML
    private void initialize() {
        player1.getItems().addAll("Shep", "Atticus", "Trask");
        player2.getItems().addAll("Shep", "Atticus", "Trask");

        // Allow only numbers in score to win
        scoreToWin.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                scoreToWin.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void setDB(DatabaseDriver db) {
        this.db = db;
    }

    @FXML
    public void handleStartGame(ActionEvent event) {
        try {
            // Set scene
            FXMLLoader loader = new FXMLLoader(StartScreenController.class.getResource("/fx/live-game.fxml"));
            Parent root = loader.load();
            LiveGameController controller = loader.getController();

            // Check if player names are filled
            String p1 = player1.getValue();
            String p2 = player2.getValue();
            if (p1 == null || p2 == null) {
                showError("Must select both player names");
            }

            // Check if player names are the same
            else if (p1.equals(p2)) {
                showError("Players cannot have the same name");
            }

            // Check if score to win was given
            else if (scoreToWin.getText().isEmpty()) {
                showError("Score cannot be empty");
            }

            // Switch scenes
            else {
                controller.setDB(db);
                controller.setUp(p1, p2, Integer.parseInt(scoreToWin.getText()));
                System.out.println("setting scoreToWin at " + Integer.parseInt(scoreToWin.getText()));

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
            }
        } catch (IOException e) {
            showError("ERROR: " + e.getMessage());
        }
    }

    public void handleMatchupHistory(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/matchup-history.fxml"));
        Parent root = loader.load();

        HistoryController controller = loader.getController();
        controller.setUp(db);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
