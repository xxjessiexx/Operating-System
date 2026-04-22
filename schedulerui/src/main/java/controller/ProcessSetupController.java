package controller;

import app.SceneManager;
import app.SimulationConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ProcessSetupController {

    @FXML
    private TextField p1ArrivalField;

    @FXML
    private TextField p2ArrivalField;

    @FXML
    private TextField p3ArrivalField;

    @FXML
    private Button nextButton;

    private SimulationConfig config;

    public void setConfig(SimulationConfig config) {
        this.config = config;
    }

    @FXML
    private void initialize() {
        nextButton.setDefaultButton(true);

        p1ArrivalField.setOnAction(e -> handleNext());
        p2ArrivalField.setOnAction(e -> handleNext());
        p3ArrivalField.setOnAction(e -> handleNext());
    }

    @FXML
    private void handleBack() throws Exception {
        SceneManager.showHome();
    }

    @FXML
    private void handleHome() throws Exception {
        SceneManager.showHome();
    }

    @FXML
    private void handleNext() {
        int p1, p2, p3;

        try {
            p1 = Integer.parseInt(p1ArrivalField.getText().trim());
            p2 = Integer.parseInt(p2ArrivalField.getText().trim());
            p3 = Integer.parseInt(p3ArrivalField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Please enter valid integer arrival times.");
            return;
        }

        // only runs if parsing succeeded
        config.setP1Arrival(p1);
        config.setP2Arrival(p2);
        config.setP3Arrival(p3);

        try {
            SceneManager.showSchedulerSetup();
        } catch (Exception e) {
            e.printStackTrace();
            showError("invalid input. Please enter valid integer arrival times.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(SceneManager.getStage());
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(
                SceneManager.class.getResource("/view/styles.css").toExternalForm()
        );
        alert.getDialogPane().getStyleClass().add("dialog-pane-dark");
        alert.showAndWait();
    }
}
