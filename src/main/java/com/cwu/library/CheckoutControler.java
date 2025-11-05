package com.cwu.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import java.util.List;

public class CheckoutControler implements Searchable{
    private boolean isBookMode = true;

    @FXML
    private TextField bookIdTextBox;

    @FXML
    private MenuItem bookMenu;

    @FXML
    private MenuItem cdMenu;

    @FXML
    private Button checkoutButton;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TextField nameTextBox;

    @FXML
    private Button returnButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField studentIdTextBox;

    @FXML
    private MenuItem studentMenu;

    @FXML
    private Button switchButton;

    @FXML
    void checkout(ActionEvent event) {

    }

    @FXML
    public void search(ActionEvent event) {
        if(isBookMode) {
            //load the book search popup
            BookSearchControler controler = App.openPopup("BookSearch.fxml", BookSearchControler.class);
            controler.setParentController(this);
        } else {
            //load the cd search popup
            CDSearchControler controler = App.openPopup("CDSearch.fxml", CDSearchControler.class);
            controler.setParentController(this);
        }
    }

    @FXML
    void returnItem(ActionEvent event) {

    }

    @FXML
    void switchItem(ActionEvent event) {

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
     * Switches to the Student scene.
     * @param event
     */
    @FXML
    void switchToStudent(ActionEvent event) {
        App.switchScene("StudentInventory.fxml");
    }

    public void recieveData(List<String> data) {
        //handle data received from search popup
    }
}
