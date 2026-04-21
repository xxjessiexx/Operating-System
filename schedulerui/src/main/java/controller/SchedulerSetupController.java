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

            if (!isRR) {
                quantumField.clear();
            }
        });
    }

    @FXML
    private void handleStart() {
        String algorithm = algorithmComboBox.getValue();

        if (algorithm == null) {
            showError("Please choose a scheduling algorithm.");
            return;
        }

        config.setAlgorithm(algorithm);

        if ("RoundRobin".equals(algorithm)) {
            String qText = quantumField.getText();

            if (qText == null || qText.trim().isEmpty()) {
                showError("Please enter a quantum value.");
                return;
            }

            int quantum;
            try {
                quantum = Integer.parseInt(qText.trim());
            } catch (NumberFormatException e) {
                showError("Please enter a valid quantum value.");
                return;
            }

            if (quantum <= 0) {
                showError("Quantum must be greater than zero.");
                return;
            }

            config.setQuantum(quantum);
        } else {
            config.setQuantum(0);
        }

        try {
            SceneManager.showSimulation();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to open the simulation screen.");
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