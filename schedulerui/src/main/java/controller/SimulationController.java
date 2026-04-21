package controller;

import app.SceneManager;
import app.SimulationConfig;
import backend.OS;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

public class SimulationController {

    private OS os;
    private boolean completionShown = false;
    private boolean stepScheduled = false;

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

        refreshUI();
    }

    @FXML
    private void handleBack() throws Exception {
        if (timeline != null) {
            timeline.stop();
        }
        SceneManager.showSchedulerSetup();
    }

    @FXML
    private void handleHome() throws Exception {
        if (timeline != null) {
            timeline.stop();
        }
        SceneManager.showHome();
    }

    private void advanceOneStep() {
        if (stepScheduled) {
            return;
        }

        if (!os.isFinished()) {
            stepScheduled = true;

            if (timeline != null) {
                timeline.pause();
            }

            Platform.runLater(() -> {
                try {
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
                    } else {
                        if (timeline != null) {
                            timeline.play();
                        }
                    }
                } finally {
                    stepScheduled = false;
                }
            });
        } else {
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

        advanceOneStep();

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
        if (globalTimeLabel != null) {
            globalTimeLabel.setText(String.valueOf(os.getGlobalTime()));
        }

        if (memoryArea != null) {
            memoryArea.setText(os.getMemorySnapshot());
        }

        if (diskArea != null) {
            diskArea.setText(os.getDiskSnapshot());
        }

        if (blockedQueueArea != null) {
            blockedQueueArea.setText(os.getBlockedQueueSnapshot());
        }

        if (runningProcessLabel != null) {
            runningProcessLabel.setText(os.getRunningProcessSnapshot());
        }

        if (currentInstructionLabel != null) {
            currentInstructionLabel.setText(os.getCurrentInstructionSnapshot());
        }

        if (algorithmLabel != null) {
            algorithmLabel.setText("Algorithm: " + os.getSchedulerAlgorithm());
        }

        if (quantumLabel != null) {
            if ("RoundRobin".equals(os.getSchedulerAlgorithm())) {
                quantumLabel.setText("Quantum: " + os.getQuantum());
            } else {
                quantumLabel.setText("Quantum: N/A");
            }
        }

        if (usedTimeLabel != null) {
            if ("RoundRobin".equals(os.getSchedulerAlgorithm())) {
                usedTimeLabel.setText("Used Time: " + os.getUsedTime());
            } else {
                usedTimeLabel.setText("Used Time: N/A");
            }
        }

        if (p1StateLabel != null) {
            p1StateLabel.setText("P1: " + os.getProcessStateSnapshot(1));
        }

        if (p2StateLabel != null) {
            p2StateLabel.setText("P2: " + os.getProcessStateSnapshot(2));
        }

        if (p3StateLabel != null) {
            p3StateLabel.setText("P3: " + os.getProcessStateSnapshot(3));
        }

        if ("MultilevelFeedbackQueue".equals(os.getAlgorithm())) {
            if (pq0Area != null) {
                pq0Area.setText(os.getPQ0Snapshot());
            }
            if (pq1Area != null) {
                pq1Area.setText(os.getPQ1Snapshot());
            }
            if (pq2Area != null) {
                pq2Area.setText(os.getPQ2Snapshot());
            }
            if (pq3Area != null) {
                pq3Area.setText(os.getPQ3Snapshot());
            }

            if (readyQueueArea != null) {
                readyQueueArea.clear();
            }
        } else {
            if (readyQueueArea != null) {
                readyQueueArea.setText(os.getReadyQueueSnapshot());
            }

            if (pq0Area != null) {
                pq0Area.clear();
            }
            if (pq1Area != null) {
                pq1Area.clear();
            }
            if (pq2Area != null) {
                pq2Area.clear();
            }
            if (pq3Area != null) {
                pq3Area.clear();
            }
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
