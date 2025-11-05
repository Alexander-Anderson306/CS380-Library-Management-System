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

    @FXML
    void switchToBook(ActionEvent event) {

    }

    @FXML
    void switchToCD(ActionEvent event) {

    }

    @FXML
    void switchToCheckout(ActionEvent event) {

    }

}
