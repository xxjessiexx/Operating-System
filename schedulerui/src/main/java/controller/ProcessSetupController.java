package controller;

import app.SceneManager;
import app.SimulationConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ProcessSetupController {

    @FXML
    private TextField p1ArrivalField;

    @FXML
    private TextField p2ArrivalField;

    @FXML
    private TextField p3ArrivalField;

    private SimulationConfig config;

    public void setConfig(SimulationConfig config) {
        this.config = config;
    }

    @FXML
    private void handleNext() {
        try {
            config.setP1Arrival(Integer.parseInt(p1ArrivalField.getText().trim()));
            config.setP2Arrival(Integer.parseInt(p2ArrivalField.getText().trim()));
            config.setP3Arrival(Integer.parseInt(p3ArrivalField.getText().trim()));

            SceneManager.showSchedulerSetup();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid integer arrival times.");
            alert.showAndWait();
        }
    }
}