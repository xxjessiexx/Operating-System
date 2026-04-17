module schedulerui {
    requires javafx.controls;
    requires javafx.fxml;

    opens schedulerui to javafx.fxml;
    exports schedulerui;
}