package com.cwu.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.util.LinkedList;
import java.util.List;

public class CDControler implements Searchable {

    @FXML
    private Button addOrModButton;

    @FXML
    private MenuItem bookMenu;

    @FXML
    private TextArea callNumberTextArea;

    @FXML
    private MenuItem checkoutMenu;

    @FXML
    private Button deleteButton;

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

    /**
     * Adds or modifies a CD item in the database.
     * @param event The event that triggered this method.
     */
    @FXML
    void addOrModify(ActionEvent event) {
        //get the query item
        Item queryItem = getQueryItem();

        //bad code
        //make sure all the text boxes are not empty
        if(titleTextBox.getText().isEmpty() || callNumberTextArea.getText().isEmpty() || yearTextBox.getText().isEmpty()) {
            //pop up error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Fields");
            alert.setHeaderText("Input Error");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        //first check if the item exists
        LinkedList<Item> items = SQLHandler.queryCDs(queryItem);
        if(items.isEmpty()) {
            //item doesn't exist, so add it
            SQLHandler.addCD(queryItem);
        } else {
            //item exists, so modify it
            SQLHandler.modifyCD(queryItem);
        }
    }

    @FXML
    void delete(ActionEvent event) {
        //make sure boxes are not empty
        if(titleTextBox.getText().isEmpty() || callNumberTextArea.getText().isEmpty() || yearTextBox.getText().isEmpty()) {
            //pop up error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Fields");
            alert.setHeaderText("Input Error");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        //get the cd query
        Item cdQuery = getQueryItem();

        //query the database and see if we already have that book
        LinkedList<Item> cdList = SQLHandler.queryCDs(cdQuery);

        if(cdList.size() > 0) {
            //delete the cd
            SQLHandler.removeCD(cdList.get(0).getId());
        }
    }

    @FXML
    public void search(ActionEvent event) {
        //load the cd search popup
        CDSearchControler controler = App.openPopup("CDSearch.fxml", CDSearchControler.class);
        controler.setParentController(this);
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

    /**
     * Gets an Item object representing the query criteria entered in the text boxes.
     * Does not perform any error checking on the entered data.
     * @return an Item object representing the query criteria, or null if no data was entered.
     */
    public Item getQueryItem() {
        Item item = new Item(-1);
        //should implement error checking

        if(!titleTextBox.getText().isEmpty()) {
            item.setAttribute("title", titleTextBox.getText());
        }

        if(!yearTextBox.getText().isEmpty()) {
            item.setAttribute("year", yearTextBox.getText());
        }

        if(!callNumberTextArea.getText().isEmpty()) {
            item.setAttribute("callNumber", callNumberTextArea.getText());
        }
        return item;
    }

    /**
     * Receives data from the search popup.
     * Sets the title text box to the first element of the data list.
     * @param data A list of strings containing the data.
     */
    public void recieveData(List<String> data) {
        if(data.size() >=1) {
            String firstData = data.get(0);
            //this is so scuffed but cant fix it now
            titleTextBox.setText(firstData);
        }
    }
}
