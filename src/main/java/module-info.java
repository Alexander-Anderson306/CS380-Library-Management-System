module com.cwu.library {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.cwu.library to javafx.fxml;
    exports com.cwu.library;
}
