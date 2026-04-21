package controller;

import app.SimulationConfig;
import backend.OS;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

public class SimulationController {
    private OS os;
    private boolean completionShown = false;
    @FXML
    private Label globalTimeLabel;

    @FXML
    private Label runningProcessLabel;

    @FXML
    private Label p1StateLabel;

    @FXML
    private Label p2StateLabel;

    @FXML
    private Label p3StateLabel;

    @FXML
    private TextArea memoryArea;

    @FXML
    private TextArea diskArea;

    @FXML
    private TextArea readyQueueArea;

    @FXML
    private TextArea blockedQueueArea;

    @FXML
    private TextArea pq0Area;

    @FXML
    private TextArea pq1Area;

    @FXML
    private TextArea pq2Area;

    @FXML
    private TextArea pq3Area;
    @FXML
    private Label algorithmLabel;

    @FXML
    private Label quantumLabel;

    @FXML
    private Label usedTimeLabel;

    @FXML
    private Label currentInstructionLabel;

    private SimulationConfig config;
    private Timeline timeline;
    private int currentTime = 0;

    public void setConfig(SimulationConfig config) {
        this.config = config;
    }

    public void initializeSimulation() {
        os = new OS(config.getAlgorithm(), config.getQuantum());
        completionShown = false;
        os.setupSimulation(
                config.getP1Arrival(),
                config.getP2Arrival(),
                config.getP3Arrival(),
                config.getAlgorithm(),
                config.getQuantum()
        );
        System.out.println("P1 arrival = " + config.getP1Arrival());
        System.out.println("P2 arrival = " + config.getP2Arrival());
        System.out.println("P3 arrival = " + config.getP3Arrival());
        System.out.println("Algorithm = " + config.getAlgorithm());
        System.out.println("Quantum = " + config.getQuantum());
        refreshUI();
    }
    private void advanceOneStep() {
    if (!os.isFinished()) {
        os.runOneStep();
        refreshUI();
    }

    if (os.isFinished()) {
        if (timeline != null) {
            timeline.stop();
        }
        if (!completionShown) {
            completionShown = true;
            showCompletionPopup();
        }
    }
    }

    @FXML
    private void handleNextStep() {
        advanceOneStep();
    }

   @FXML
private void handleRunAuto() {
    if (timeline != null) {
        timeline.stop();
    }

    // Execute one step immediately
    advanceOneStep();

    // If simulation already finished after that step, stop here
    if (os.isFinished()) {
        return;
    }

    timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        advanceOneStep();

        if (os.isFinished()) {
            timeline.stop();
        }
    }));

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
}

    @FXML
    private void handlePause() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    @FXML
    private void handleReset() {
        if (timeline != null) {
            timeline.stop();
        }
        initializeSimulation();
    }

    private void refreshUI() {
    globalTimeLabel.setText(String.valueOf(os.getGlobalTime()));
    memoryArea.setText(os.getMemorySnapshot());
    diskArea.setText(os.getDiskSnapshot());
    readyQueueArea.setText(os.getReadyQueueSnapshot());
    blockedQueueArea.setText(os.getBlockedQueueSnapshot());
    runningProcessLabel.setText(os.getRunningProcessSnapshot());
    currentInstructionLabel.setText(os.getCurrentInstructionSnapshot());

    algorithmLabel.setText("Algorithm: " + os.getSchedulerAlgorithm());

    if ("RoundRobin".equals(os.getSchedulerAlgorithm())) {
        quantumLabel.setText("Quantum: " + os.getQuantum());
        usedTimeLabel.setText("Used Time: " + os.getUsedTime());
    } else {
        quantumLabel.setText("Quantum: N/A");
        usedTimeLabel.setText("Used Time: N/A");
    }

    p1StateLabel.setText("P1: " + os.getProcessStateSnapshot(1));
    p2StateLabel.setText("P2: " + os.getProcessStateSnapshot(2));
    p3StateLabel.setText("P3: " + os.getProcessStateSnapshot(3));

    if ("MultilevelFeedbackQueue".equals(os.getAlgorithm())) {
        pq0Area.setText(os.getPQ0Snapshot());
        pq1Area.setText(os.getPQ1Snapshot());
        pq2Area.setText(os.getPQ2Snapshot());
        pq3Area.setText(os.getPQ3Snapshot());

        pq0Area.setVisible(true);
        pq1Area.setVisible(true);
        pq2Area.setVisible(true);
        pq3Area.setVisible(true);
    } else {
        pq0Area.setText("");
        pq1Area.setText("");
        pq2Area.setText("");
        pq3Area.setText("");

        pq0Area.setVisible(false);
        pq1Area.setVisible(false);
        pq2Area.setVisible(false);
        pq3Area.setVisible(false);
    }
}
    private void showCompletionPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulation Complete");
        alert.setHeaderText("Execution Finished");
        alert.setContentText("All 3 processes have completed execution.\nThe simulation is now finished.");
        alert.showAndWait();
    }
}