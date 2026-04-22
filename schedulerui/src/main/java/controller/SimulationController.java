package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.SceneManager;
import app.SimulationConfig;
import backend.OS;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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
    private VBox memorySlotsBox;

    @FXML
    private VBox diskSlotsBox;

    @FXML
    private FlowPane readyQueuePane;

    @FXML
    private FlowPane blockedQueuePane;

    @FXML
    private FlowPane pq0Pane;

    @FXML
    private FlowPane pq1Pane;

    @FXML
    private FlowPane pq2Pane;

    @FXML
    private FlowPane pq3Pane;

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

    private final Map<String, String> previousQueueSnapshots = new HashMap<>();
    private final Map<Integer, String> previousMemoryValues = new HashMap<>();
    private String previousDiskSnapshot = "";

    public void setConfig(SimulationConfig config) {
        this.config = config;
    }

    public void initializeSimulation() {
        os = new OS(config.getAlgorithm(), config.getQuantum());
        completionShown = false;
        stepScheduled = false;

        previousQueueSnapshots.clear();
        previousMemoryValues.clear();
        previousDiskSnapshot = "";

        os.setupSimulation(
                config.getP1Arrival(),
                config.getP2Arrival(),
                config.getP3Arrival(),
                config.getAlgorithm(),
                config.getQuantum()
        );

        refreshUI();
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

    private void refreshUI() {
        if (globalTimeLabel != null) {
            globalTimeLabel.setText(String.valueOf(os.getGlobalTime()));
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

        if (memorySlotsBox != null) {
            renderMemory(os.getMemorySnapshot());
        }

        if (diskSlotsBox != null) {
            renderDisk(os.getDiskSnapshot());
        }

        if (readyQueuePane != null) {
            renderQueue(readyQueuePane, os.getReadyQueueSnapshot(), "ready");
        }

        if (blockedQueuePane != null) {
            renderQueue(blockedQueuePane, os.getBlockedQueueSnapshot(), "blocked");
        }

        if (pq0Pane != null) {
            renderQueue(pq0Pane, os.getPQ0Snapshot(), "pq0");
        }

        if (pq1Pane != null) {
            renderQueue(pq1Pane, os.getPQ1Snapshot(), "pq1");
        }

        if (pq2Pane != null) {
            renderQueue(pq2Pane, os.getPQ2Snapshot(), "pq2");
        }

        if (pq3Pane != null) {
            renderQueue(pq3Pane, os.getPQ3Snapshot(), "pq3");
        }
    }

    private void renderQueue(FlowPane pane, String snapshot, String key) {
        if (pane == null) {
            return;
        }

        List<String> processes = parseQueueSnapshot(snapshot);
        String previous = previousQueueSnapshots.get(key);
        boolean changed = !Objects.equals(previous, snapshot);

        pane.getChildren().clear();

        if (processes.isEmpty()) {
            Label empty = new Label("Empty");
            empty.getStyleClass().add("queue-placeholder");
            pane.getChildren().add(empty);
        } else {
            for (int i = 0; i < processes.size(); i++) {
                Label chip = createProcessChip(processes.get(i));
                pane.getChildren().add(chip);

                if (changed) {
                    animateChip(chip, i);
                }
            }
        }

        previousQueueSnapshots.put(key, snapshot);
    }

    private void renderMemory(String snapshot) {
        if (memorySlotsBox == null) {
            return;
        }

        List<MemoryRowData> rows = parseMemorySnapshot(snapshot);
        Map<Integer, String> oldValues = new HashMap<>(previousMemoryValues);

        memorySlotsBox.getChildren().clear();
        previousMemoryValues.clear();

        for (MemoryRowData rowData : rows) {
            HBox row = createMemoryRow(rowData);
            memorySlotsBox.getChildren().add(row);

            String oldValue = oldValues.get(rowData.address);
            if (!Objects.equals(oldValue, rowData.rawValue)) {
                animateMemoryRow(row);
            }

            previousMemoryValues.put(rowData.address, rowData.rawValue);
        }
    }

    private void renderDisk(String snapshot) {
        if (diskSlotsBox == null) {
            return;
        }

        boolean changed = !Objects.equals(previousDiskSnapshot, snapshot);
        List<DiskRowData> rows = parseDiskSnapshot(snapshot);

        diskSlotsBox.getChildren().clear();

        if (rows.isEmpty()) {
            Label empty = new Label("Disk Empty");
            empty.getStyleClass().add("queue-placeholder");
            diskSlotsBox.getChildren().add(empty);
        } else {
            for (int i = 0; i < rows.size(); i++) {
                HBox row = createDiskRow(rows.get(i));
                diskSlotsBox.getChildren().add(row);

                if (changed) {
                    animateMemoryRow(row);
                }
            }
        }

        previousDiskSnapshot = snapshot;
    }

    private Label createProcessChip(String processName) {
        Label chip = new Label(processName);
        chip.getStyleClass().add("process-chip");

        switch (processName) {
            case "P1" ->
                chip.getStyleClass().add("p1-chip");
            case "P2" ->
                chip.getStyleClass().add("p2-chip");
            case "P3" ->
                chip.getStyleClass().add("p3-chip");
            default ->
                chip.getStyleClass().add("generic-chip");
        }

        return chip;
    }

    private HBox createMemoryRow(MemoryRowData rowData) {
        Label addressLabel = new Label(String.valueOf(rowData.address));
        addressLabel.getStyleClass().add("memory-address");

        Label chip = new Label(rowData.chipText);
        chip.getStyleClass().add("memory-chip");
        chip.getStyleClass().add(rowData.chipStyleClass);

        Label detailLabel = new Label(rowData.detailText);
        detailLabel.getStyleClass().add("memory-detail");
        detailLabel.setWrapText(true);
        detailLabel.setMaxWidth(Double.MAX_VALUE);

        Region filler = new Region();
        filler.getStyleClass().add("memory-bar");
        HBox.setHgrow(filler, Priority.ALWAYS);

        HBox row = new HBox(12, addressLabel, chip, filler, detailLabel);
        row.getStyleClass().add("memory-slot");

        return row;
    }

    private HBox createDiskRow(DiskRowData rowData) {
        Label chip = new Label(rowData.processName);
        chip.getStyleClass().add("memory-chip");
        chip.getStyleClass().add(rowData.chipStyleClass);

        Label detail = new Label(rowData.detailText);
        detail.getStyleClass().add("disk-detail");
        detail.setWrapText(true);

        HBox row = new HBox(12, chip, detail);
        row.getStyleClass().add("disk-slot");
        return row;
    }

    private void animateChip(Node node, int index) {
        FadeTransition fade = new FadeTransition(Duration.millis(220), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setDelay(Duration.millis(index * 60L));

        TranslateTransition slide = new TranslateTransition(Duration.millis(220), node);
        slide.setFromX(-18);
        slide.setToX(0);
        slide.setDelay(Duration.millis(index * 60L));

        ScaleTransition scale = new ScaleTransition(Duration.millis(220), node);
        scale.setFromX(0.88);
        scale.setFromY(0.88);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setDelay(Duration.millis(index * 60L));

        new ParallelTransition(fade, slide, scale).play();
    }

    private void animateMemoryRow(Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(220), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(220), node);
        slide.setFromY(8);
        slide.setToY(0);

        new ParallelTransition(fade, slide).play();
    }

    private List<String> parseQueueSnapshot(String snapshot) {
        List<String> result = new ArrayList<>();
        if (snapshot == null || snapshot.isBlank() || snapshot.equals("[]")) {
            return result;
        }

        Matcher pidMatcher = Pattern.compile("PID\\s*=\\s*(\\d+)").matcher(snapshot);
        while (pidMatcher.find()) {
            result.add("P" + pidMatcher.group(1));
        }

        if (!result.isEmpty()) {
            return result;
        }

        Matcher pMatcher = Pattern.compile("\\bP(\\d+)\\b").matcher(snapshot);
        while (pMatcher.find()) {
            result.add("P" + pMatcher.group(1));
        }

        return result;
    }

    private List<MemoryRowData> parseMemorySnapshot(String snapshot) {
        List<MemoryRowData> rows = new ArrayList<>();
        if (snapshot == null || snapshot.isBlank()) {
            return rows;
        }

        String[] lines = snapshot.split("\\R");
        Pattern linePattern = Pattern.compile("^address\\s+(\\d+)\\s*:\\s*(.*)$", Pattern.CASE_INSENSITIVE);

        String currentOwner = null;

        for (String line : lines) {
            Matcher lineMatcher = linePattern.matcher(line.trim());
            if (!lineMatcher.matches()) {
                continue;
            }

            int address = Integer.parseInt(lineMatcher.group(1));
            String rawValue = lineMatcher.group(2).trim();

            String chipText;
            String chipStyleClass;
            String detailText = rawValue;

            if (rawValue.equalsIgnoreCase("Empty")) {
                chipText = "Free";
                chipStyleClass = "free-chip";
                currentOwner = null;
            } else {
                Matcher pidMatcher = Pattern.compile("^PID\\s*=\\s*(\\d+)$").matcher(rawValue);
                if (pidMatcher.find()) {
                    currentOwner = "P" + pidMatcher.group(1);
                    chipText = currentOwner;
                    chipStyleClass = processClass(currentOwner);
                } else if (currentOwner != null) {
                    chipText = currentOwner;
                    chipStyleClass = processClass(currentOwner);
                } else {
                    chipText = classifyMemoryWord(rawValue);
                    chipStyleClass = "generic-chip";
                }
            }

            rows.add(new MemoryRowData(address, rawValue, chipText, chipStyleClass, detailText));
        }

        return rows;
    }

    private List<DiskRowData> parseDiskSnapshot(String snapshot) {
        List<DiskRowData> rows = new ArrayList<>();
        if (snapshot == null || snapshot.isBlank()) {
            return rows;
        }

        String[] lines = snapshot.split("\\R");
        String currentOwner = null;

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.isEmpty()
                    || trimmed.startsWith("----DISK")
                    || trimmed.startsWith("-------------")) {
                continue;
            }

            Matcher pidMatcher = Pattern.compile("^PID\\s*=\\s*(\\d+)$").matcher(trimmed);
            if (pidMatcher.find()) {
                currentOwner = "P" + pidMatcher.group(1);
                rows.add(new DiskRowData(currentOwner, "PCB • PID = " + pidMatcher.group(1), processClass(currentOwner)));
                continue;
            }

            if (trimmed.startsWith("State")) {
                rows.add(new DiskRowData(
                        currentOwner != null ? currentOwner : "SYS",
                        "PCB • " + trimmed,
                        currentOwner != null ? processClass(currentOwner) : "generic-chip"
                ));
                continue;
            }

            if (trimmed.startsWith("PC") || trimmed.startsWith("MemStart") || trimmed.startsWith("MemEnd")) {
                rows.add(new DiskRowData(
                        currentOwner != null ? currentOwner : "SYS",
                        "PCB • " + trimmed,
                        currentOwner != null ? processClass(currentOwner) : "generic-chip"
                ));
                continue;
            }

            if (trimmed.startsWith("Instruction_")) {
                rows.add(new DiskRowData(
                        currentOwner != null ? currentOwner : "SYS",
                        cleanDiskDetail(trimmed),
                        currentOwner != null ? processClass(currentOwner) : "generic-chip"
                ));
                continue;
            }

            rows.add(new DiskRowData(
                    currentOwner != null ? currentOwner : "SYS",
                    cleanDiskDetail(trimmed),
                    currentOwner != null ? processClass(currentOwner) : "generic-chip"
            ));
        }

        return rows;
    }

    private String cleanDiskDetail(String text) {
        return text
                .replaceFirst("^Instruction_\\d+\\s*=\\s*", "Instr • ")
                .replace("_", " ");
    }

    private String processClass(String processName) {
        return switch (processName) {
            case "P1" ->
                "p1-chip";
            case "P2" ->
                "p2-chip";
            case "P3" ->
                "p3-chip";
            default ->
                "generic-chip";
        };
    }

    private String classifyMemoryWord(String rawValue) {
        if (rawValue.startsWith("State")) {
            return "State";
        }
        if (rawValue.startsWith("PC")) {
            return "PC";
        }
        if (rawValue.startsWith("MemStart")) {
            return "MemStart";
        }
        if (rawValue.startsWith("MemEnd")) {
            return "MemEnd";
        }
        if (rawValue.startsWith("Instruction_")) {
            return "Instr";
        }
        return "Data";
    }

    private void showCompletionPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulation Complete");
        alert.setHeaderText("Execution Finished");
        alert.setContentText("All 3 processes have completed execution.\nThe simulation is now finished.");
        alert.showAndWait();
    }

    private static class MemoryRowData {

        final int address;
        final String rawValue;
        final String chipText;
        final String chipStyleClass;
        final String detailText;

        MemoryRowData(int address, String rawValue, String chipText, String chipStyleClass, String detailText) {
            this.address = address;
            this.rawValue = rawValue;
            this.chipText = chipText;
            this.chipStyleClass = chipStyleClass;
            this.detailText = detailText;
        }
    }

    private static class DiskRowData {

        final String processName;
        final String detailText;
        final String chipStyleClass;

        DiskRowData(String processName, String detailText, String chipStyleClass) {
            this.processName = processName;
            this.detailText = detailText;
            this.chipStyleClass = chipStyleClass;
        }
    }
}
