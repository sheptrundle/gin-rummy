import java.io.IOException;

import Controllers.StartScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RunGinRummy extends Application {

    public static void main(String[] args) throws IOException {
        launch(args);
    }


    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/start-screen.fxml"));
        Parent root = loader.load();
        StartScreenController controller = loader.getController();
        DatabaseDriver db = new DatabaseDriver();
        db.connect();
        db.createTables();
        db.commit();

        // Set scene
        Scene scene = new Scene(root);
        stage.setTitle("Gin Rummy");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();


    }
}
