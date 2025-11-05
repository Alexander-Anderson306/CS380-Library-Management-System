package com.cwu.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class StudentInventoryControler {

    @FXML
    private MenuItem bookMenu;

    @FXML
    private MenuItem cdMenu;

    @FXML
    private MenuItem checkoutMenu;

    @FXML
    private ListView<?> itemsListView;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TextField nameTextBox;

    @FXML
    private Button searchButton;

    @FXML
    private TextField studentIdTextBox;

    @FXML
    void search(ActionEvent event) {

    }

    /**
     * Switches to the Book scene.
     * @param event
     */
    @FXML
    void switchToBook(ActionEvent event) {
        App.switchScene("Book.fxml");
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

}
