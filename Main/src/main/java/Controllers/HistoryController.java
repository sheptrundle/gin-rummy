package Controllers;

import Database.DatabaseDriver;
import Database.HistoryRow;
import Database.TotalsRow;
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

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HistoryController {
    // History
    @FXML private TableView<HistoryRow> historyTable;
    @FXML private TableColumn<HistoryRow, String> name1Column;
    @FXML private TableColumn<HistoryRow, Integer> wins1Column;
    @FXML private TableColumn<HistoryRow, String> name2Column;
    @FXML private TableColumn<HistoryRow, Integer> wins2Column;

    // Totals
    @FXML private TableView<TotalsRow> totalsTable;
    @FXML private TableColumn<TotalsRow, String> totalsNameColumn;
    @FXML private TableColumn<TotalsRow, Integer> totalsWinsColumn;
    @FXML private TableColumn<TotalsRow, Integer> totalsLossesColumn;
    @FXML private TableColumn<TotalsRow, Double> totalsPctColumn;

    // Database connection
    private DatabaseDriver db;

    @FXML
    public void setUp(DatabaseDriver db) {
        this.db = db;

        // History table columns
        name1Column.setCellValueFactory(new PropertyValueFactory<>("name1"));
        wins1Column.setCellValueFactory(new PropertyValueFactory<>("wins1"));
        name2Column.setCellValueFactory(new PropertyValueFactory<>("name2"));
        wins2Column.setCellValueFactory(new PropertyValueFactory<>("wins2"));

        // Totals table columns
        totalsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        totalsWinsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));
        totalsLossesColumn.setCellValueFactory(new PropertyValueFactory<>("losses"));
        totalsPctColumn.setCellValueFactory(new PropertyValueFactory<>("pct"));

        // Load table data
        loadHistory();
        loadTotals();
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

    public void loadTotals() {
        if (db == null) throw new NullPointerException("Database is null");

        ObservableList<TotalsRow> rows = FXCollections.observableArrayList();

        String query = "SELECT Name, TotalWins, TotalLosses FROM Totals";

        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("Name");
                int wins = rs.getInt("TotalWins");
                int losses = rs.getInt("TotalLosses");
                double pct = (double) wins / (wins + losses);
                pct = Math.round(pct * 1000) / 1000.0;

                rows.add(new TotalsRow(name, wins, losses, pct));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        totalsTable.setItems(rows);

        // Sort totalsTable by pct descending
        totalsPctColumn.setSortType(TableColumn.SortType.DESCENDING);
        totalsTable.getSortOrder().clear();
        totalsTable.getSortOrder().add(totalsPctColumn);
        totalsTable.sort();
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
