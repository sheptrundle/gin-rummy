import java.io.IOException;

import Controllers.StartScreenController;
import Database.DatabaseDriver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RunGinRummy extends Application {
    private DatabaseDriver db;

    public static void main(String[] args) throws IOException {
        launch(args);
    }


    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/start-screen.fxml"));
        Parent root = loader.load();
        StartScreenController controller = loader.getController();

        // Database setup
        db = new DatabaseDriver();
        db.connect();
        db.createTables();
        db.commit();
        controller.setDB(db);

        // Set scene
        Scene scene = new Scene(root);
        stage.setTitle("Gin Rummy");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        // Called automatically when app exits
        if (db != null) {
            db.disconnect();
        }
        super.stop();
    }
}
