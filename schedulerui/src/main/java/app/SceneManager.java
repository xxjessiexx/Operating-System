package app;

import controller.ProcessSetupController;
import controller.SchedulerSetupController;
import controller.SimulationController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage stage;
    private static final SimulationConfig config = new SimulationConfig();
    

    public static Stage getStage() {
        return stage;
    }

    public static void init(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("OS Simulation");
    }

    public static SimulationConfig getConfig() {
        return config;
    }

    public static void showProcessSetup() throws Exception {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/view/process_setup.fxml"));
        Parent root = loader.load();

        ProcessSetupController controller = loader.getController();
        controller.setConfig(config);

        Scene scene = new Scene(root, 700, 450);
        scene.getStylesheets().add(SceneManager.class.getResource("/view/styles.css").toExternalForm());
        stage.setResizable(true);
        stage.centerOnScreen();

        stage.setScene(scene);
        stage.show();
    }

    public static void showSchedulerSetup() throws Exception {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/view/scheduler_setup.fxml"));
        Parent root = loader.load();

        SchedulerSetupController controller = loader.getController();
        controller.setConfig(config);

        Scene scene = new Scene(root, 700, 450);
        scene.getStylesheets().add(SceneManager.class.getResource("/view/styles.css").toExternalForm());
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public static void showSimulation() throws Exception {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/view/simulation.fxml"));
        Parent root = loader.load();

        SimulationController controller = loader.getController();
        controller.setConfig(config);
        controller.initializeSimulation();

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(
                root,
                screen.getWidth() * 0.97,
                screen.getHeight() * 0.92
        );
        scene.getStylesheets().add(SceneManager.class.getResource("/view/styles.css").toExternalForm());
        stage.setResizable(true);
        stage.centerOnScreen();

        stage.setScene(scene);
        stage.show();
    }
}