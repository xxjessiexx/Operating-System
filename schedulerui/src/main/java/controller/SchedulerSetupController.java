package controller;

import app.SceneManager;
import app.SimulationConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class SchedulerSetupController {

    @FXML
    private TextField quantumField;

    private SimulationConfig config;
    private boolean roundRobinSelected = false;

    public void setConfig(SimulationConfig config) {
        this.config = config;
    }

    @FXML
    public void initialize() {
        quantumField.setVisible(false);
        quantumField.setManaged(false);
        quantumField.setOnAction(e -> handleRoundRobin());
    }

    @FXML
    private void handleBack() throws Exception {
        SceneManager.showProcessSetup();
    }

    @FXML
    private void handleHome() throws Exception {
        SceneManager.showHome();
    }

    @FXML
    private void handleRoundRobin() {
        if (!roundRobinSelected) {
            roundRobinSelected = true;
            quantumField.setVisible(true);
            quantumField.setManaged(true);
            quantumField.clear();
            quantumField.requestFocus();
            return;
        }

        String qText = quantumField.getText();

        if (qText == null || qText.trim().isEmpty()) {
            showError("Please enter a quantum value for Round Robin.");
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

        config.setAlgorithm("RoundRobin");
        config.setQuantum(quantum);
        openSimulation();
    }

    @FXML
    private void handleHRRN() {
        roundRobinSelected = false;
        quantumField.clear();
        quantumField.setVisible(false);
        quantumField.setManaged(false);

        config.setAlgorithm("HRRN");
        config.setQuantum(0);
        openSimulation();
    }

    @FXML
    private void handleMLFQ() {
        roundRobinSelected = false;
        quantumField.clear();
        quantumField.setVisible(false);
        quantumField.setManaged(false);

        config.setAlgorithm("MultilevelFeedbackQueue");
        config.setQuantum(0);
        openSimulation();
    }

    private void openSimulation() {
        try {
            SceneManager.showSimulation();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to open the simulation screen.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(SceneManager.getStage());
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(
                SceneManager.class.getResource("/view/styles.css").toExternalForm()
        );
        alert.getDialogPane().getStyleClass().add("dialog-pane-dark");
        alert.showAndWait();
    }
}
