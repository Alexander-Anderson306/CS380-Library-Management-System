module com.cwu.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.cwu.library to javafx.fxml;
    exports com.cwu.library;
}
