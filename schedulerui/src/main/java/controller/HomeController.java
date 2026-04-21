package controller;

import app.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    private void handleStart() {
        try {
            SceneManager.showProcessSetup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }
}
