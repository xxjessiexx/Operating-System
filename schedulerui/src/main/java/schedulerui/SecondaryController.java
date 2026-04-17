package schedulerui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SecondaryController {

    @FXML private Label readyQueueLabel;
    @FXML private Label blockedQueueLabel;
    @FXML private Label memoryContentLabel;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void handleHome() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void handlePlay() {
        readyQueueLabel.setText("P1  →  P2  →  P3");
        blockedQueueLabel.setText("Semaphore wait on file");
        memoryContentLabel.setText("P1: PC=3\nP2: READY\nP3: BLOCKED\nMutex: file=0");
    }

    @FXML
    private void handlePause() {
        readyQueueLabel.setText("Execution paused");
    }

    @FXML
    private void handleNext() {
        readyQueueLabel.setText("P2  →  P3");
        blockedQueueLabel.setText("P1 finished quantum");
        memoryContentLabel.setText("P1: READY\nP2: RUNNING\nP3: BLOCKED\nQuantum consumed");
    }
}