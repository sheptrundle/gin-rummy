package Controllers;

import Database.DatabaseDriver;
import Database.HistoryRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HistoryController {
    @FXML private TableView<HistoryRow> historyTable;

    // Table columns
    @FXML private TableColumn<HistoryRow, String> name1Column;
    @FXML private TableColumn<HistoryRow, Integer> wins1Column;
    @FXML private TableColumn<HistoryRow, String> name2Column;
    @FXML private TableColumn<HistoryRow, Integer> wins2Column;

    // Database connection
    private DatabaseDriver db;

    @FXML
    public void setUp(DatabaseDriver db) {
        this.db = db;

        // Table columns
        name1Column.setCellValueFactory(new PropertyValueFactory<>("name1"));
        wins1Column.setCellValueFactory(new PropertyValueFactory<>("wins1"));
        name2Column.setCellValueFactory(new PropertyValueFactory<>("name2"));
        wins2Column.setCellValueFactory(new PropertyValueFactory<>("wins2"));

        // Load table data
        loadHistory();
    }

    public void loadHistory() {
        if (db == null) throw new NullPointerException("Database is null");

        ObservableList<HistoryRow> rows = FXCollections.observableArrayList();

        String query = "SELECT Name1, Wins1, Name2, Wins2 FROM History";

        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name1 = rs.getString("Name1");
                int wins1 = rs.getInt("Wins1");
                String name2 = rs.getString("Name2");
                int wins2 = rs.getInt("Wins2");

                rows.add(new HistoryRow(name1, wins1, name2, wins2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        historyTable.setItems(rows);
    }

    @FXML
    private void handleBack() {
        try {
            // Load the start screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/start-screen.fxml"));
            Parent root = loader.load();

            // Set up controller
            StartScreenController controller = loader.getController();
            controller.setDB(db);

            // Switch scenes
            Stage stage = (Stage) historyTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
