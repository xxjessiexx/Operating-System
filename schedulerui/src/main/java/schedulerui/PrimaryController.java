package schedulerui;

import java.io.IOException;

import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void handleBack() {
        System.out.println("Back clicked");
    }

    @FXML
    private void handleHome() {
        System.out.println("Home clicked");
    }

    @FXML
    private void selectRoundRobin() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void selectHRRN() {
        System.out.println("HRRN selected");
    }

    @FXML
    private void selectMLFQ() {
        System.out.println("MLFQ selected");
    }
}