package controller;

import app.SceneManager;
import app.SimulationConfig;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class SchedulerSetupController {

    @FXML
    private ComboBox<String> algorithmComboBox;

    @FXML
    private TextField quantumField;

    private SimulationConfig config;

    public void setConfig(SimulationConfig config) {
        this.config = config;
    }

    @FXML
    public void initialize() {
        algorithmComboBox.setItems(FXCollections.observableArrayList(
                "RoundRobin",
                "HRRN",
                "MultilevelFeedbackQueue"
        ));

        quantumField.setVisible(false);
        quantumField.setManaged(false);

        algorithmComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            boolean isRR = "RoundRobin".equals(newValue);
            quantumField.setVisible(isRR);
            quantumField.setManaged(isRR);
        });
    }

    @FXML
    private void handleStart() {
        try {
            String algorithm = algorithmComboBox.getValue();

            if (algorithm == null) {
                showError("Please choose a scheduling algorithm.");
                return;
            }

            config.setAlgorithm(algorithm);

            if ("RoundRobin".equals(algorithm)) {
                config.setQuantum(Integer.parseInt(quantumField.getText().trim()));
            } else {
                config.setQuantum(0);
            }

            SceneManager.showSimulation();

        } catch (Exception e) {
            showError("Please enter a valid quantum value.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}