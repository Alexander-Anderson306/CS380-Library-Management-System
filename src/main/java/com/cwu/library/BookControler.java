package com.cwu.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.List;

public class BookControler implements Searchable {

    @FXML
    private Button addOrModButton;

    @FXML
    private TextField authorTextBox;

    @FXML
    private TextArea callNumberTextArea;

    @FXML
    private MenuItem cdMenu;

    @FXML
    private MenuItem checkoutMenu;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField isbnTextBox;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Button searchButton;

    @FXML
    private MenuItem studentMenu;

    @FXML
    private TextField titleTextBox;

    @FXML
    private TextField yearTextBox;

    @FXML
    void addOrMod(ActionEvent event) {

    }

    @FXML
    void delete(ActionEvent event) {

    }

    @FXML
    public void search(ActionEvent event) {

    }

    /**
     * Switches to the CD scene.
     * @param event
     */
    @FXML
    void switchToCD(ActionEvent event) {
        App.switchScene("CD.fxml");
    }

    /**
     * Switches to the Checkout scene.
     * @param event
     */
    @FXML
    void switchToCheckout(ActionEvent event) {
        App.switchScene("Checkout.fxml");
    }

    /**
     * Switches to the Student scene.
     * @param event
     */
    @FXML
    void switchToStudent(ActionEvent event) {
        App.switchScene("StudentInventory.fxml");
    }

    public void recieveData(List<String> data) {

    }
}
