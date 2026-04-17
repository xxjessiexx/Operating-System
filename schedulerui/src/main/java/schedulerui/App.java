package schedulerui;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = loadFXML("primary");
        scene = new Scene(root, 1366, 768);

        URL cssUrl = App.class.getResource("/schedulerui/styles.css");
        System.out.println("CSS URL = " + cssUrl);
        scene.getStylesheets().add(Objects.requireNonNull(cssUrl).toExternalForm());

        stage.setTitle("Scheduler UI");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("/schedulerui/" + fxml + ".fxml")
        );
        return loader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}