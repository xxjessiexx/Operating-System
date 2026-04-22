package backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SystemCalls {

    private Scanner scanner;

    public SystemCalls() {
        scanner = new Scanner(System.in);
    }

    public void print(String message) {
        System.out.println(message);
    }

    public void showOutput(int processId, String instruction, String output) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(app.SceneManager.getStage());
        alert.setTitle("Process Output");
        alert.setHeaderText("Program " + processId + " / Process P" + processId);
        alert.setContentText("Instruction: " + instruction + "\n\nOutput:\n" + output);
        alert.showAndWait();
    }

    public String input(int processId, String instruction, String variableName) {
        Dialog<String> dialog = new Dialog<>();
        dialog.initOwner(app.SceneManager.getStage());
        dialog.setTitle("Process Input");
        dialog.setHeaderText(null);

        ButtonType okButtonType
                = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType
                = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        VBox content = new VBox(14);
        content.setStyle("-fx-padding: 18;");

        Label title = new Label("Process P" + processId);
        title.getStyleClass().add("section-title");

        Label info = new Label(
                "Instruction: " + instruction + "\nEnter value for " + variableName + ":"
        );
        info.setWrapText(true);

        TextField inputField = new TextField();
        inputField.setPromptText("Enter value");

        content.getChildren().addAll(title, info, inputField);
        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().getStylesheets().add(
                app.SceneManager.class.getResource("/view/styles.css").toExternalForm()
        );
        dialog.getDialogPane().getStyleClass().add("dialog-pane-dark");

        Platform.runLater(inputField::requestFocus);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButtonType) {
                return inputField.getText();
            }
            return "";
        });

        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }

    public void writeFile(String fileName, String data) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + fileName);
        }
    }

    public String readFile(String fileName) {
        StringBuilder content = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
        }

        return content.toString().trim();
    }

    public MemoryWord readFromMemory(Memory memory, int address) {
        return memory.readWord(address);
    }

    public void writeToMemory(Memory memory, int address, String name, Object value) {
        MemoryWord word = new MemoryWord(name, value);
        memory.writeWord(address, word);
    }
}
