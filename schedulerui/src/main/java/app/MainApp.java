package app;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.init(primaryStage);
        SceneManager.showProcessSetup();
    }

    public static void main(String[] args) {
        launch(args);
    }
}