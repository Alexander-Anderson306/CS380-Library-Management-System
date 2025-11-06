package com.cwu.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import java.util.LinkedList;
import java.util.List;

public class CheckoutControler implements Searchable{
    private boolean isBookMode = true;

    @FXML
    private TextField idTextBox;

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
    private Text idText;

    /**
     * Checks out an item to a student.
     * @param event
     */
    @FXML
    void checkout(ActionEvent event) {
        //generate person
        Person student = makeStudent();
        if(student == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Student");
            alert.setHeaderText("Invalid Student Information");
            alert.setContentText("Name must be in the format: Firstname Lastname (max 32 characters each)\nStudent ID must be a positive integer.");

            // Show the popup and wait for user to close
            alert.showAndWait();
        }

        //generate item
        Item item = makeItem();
        if(item == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Item");
            alert.setHeaderText("Invalid Item Information");
            alert.setContentText("Item ID must be a positive integer.");
            alert.showAndWait();
            return;
        }

        //search for the item
        LinkedList<Item> items = SQLHandler.queryBooks(item);
        if(items.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Item Not Found");
            alert.setHeaderText("Item Not Found in Database");
            alert.setContentText("The item with the given ID was not found in the database.");

            // Show the popup and wait for user to close
            alert.showAndWait();
            return;
        }

        //make sure the item was not already checked out
        Item dbItem = items.getFirst();
        if(SQLHandler.itemInInventory(dbItem.getId())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Item Unavailable");
            alert.setHeaderText("Item Already Checked Out");
            alert.setContentText("The item is already checked out and unavailable.");

            // Show the popup and wait for user to close
            alert.showAndWait();
            return;
        } else {
            //checkout the item
            SQLHandler.addStudentInventory(student.getId(), dbItem.getId());
        }


    }

    /**
     * Searches for an item to checkout/return.
     * @param event
     */
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
        //generate person
        Person student = makeStudent();
        if(student == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Student");
            alert.setHeaderText("Invalid Student Information");
            alert.setContentText("Name must be in the format: Firstname Lastname (max 32 characters each)\nStudent ID must be a positive integer.");

            // Show the popup and wait for user to close
            alert.showAndWait();
        }

        //generate item
        Item item = makeItem();
        if(item == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Item");
            alert.setHeaderText("Invalid Item Information");
            alert.setContentText("Item ID must be a positive integer.");
            alert.showAndWait();
        }

        //search for the item
        LinkedList<Item> items = SQLHandler.queryBooks(item);
        if(items.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Item Not Found");
            alert.setHeaderText("Item Not Found in Database");
            alert.setContentText("The item with the given ID was not found in the database.");

            // Show the popup and wait for user to close
            alert.showAndWait();
            return;
        }

        //make sure the item was checked out
        Item dbItem = items.getFirst();


        if(!SQLHandler.itemInInventory(dbItem.getId())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Item Not Checked Out");
            alert.setHeaderText("Item Not Checked Out");
            alert.setContentText("The item is not currently checked out.");

            // Show the popup and wait for user to close
            alert.showAndWait();
            return;
        //handle if item is checked out to a different student
        } else if(SQLHandler.itemCheckedOutTo(dbItem.getId()) != student.getId()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Item Checked Out to Different Student");
            alert.setHeaderText("Item Checked Out to Different Student");
            alert.setContentText("The item is checked out to a different student and cannot be returned by this student.");

            // Show the popup and wait for user to close
            alert.showAndWait();
            return;
        } else {
            //return the item
            SQLHandler.removeStudentInventory(student.getId(), dbItem.getId());
        }
    }

    @FXML
    void switchItem(ActionEvent event) {
        if(isBookMode) {
            isBookMode = false;
            switchButton.setText("CD");
            idText.setText("CD ID");
            idTextBox.setPromptText("enter cd id or search for cd");
        } else {
            isBookMode = true;
            switchButton.setText("Book");
            idText.setText("Book ID");
            idTextBox.setPromptText("enter book id or search for book");
        }
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

    /**
     * Receives data from the search popup.
     * @param data A list of strings containing the data.
     */
    public void recieveData(List<String> data) {
        if(data.size() >=1) {
            idTextBox.setText(data.get(0));
        }
    }

    /**
     * Creates an Item object based on the data in the text boxes.
     * @return
     */
    public Item makeItem() {
        //make sure box is not empty
        if(idTextBox.getText().isEmpty()) {
            return null;
        }
        int id = Integer.parseInt(idTextBox.getText());

        //invalid id
        if(id <=0) {
            return null;
        }

        return new Item(id);
    }

    /**
     * Creates a Person object based on the data in the text boxes.
     * @return
     */
    public Person makeStudent() {
        //make sure boxes are not empty
        if(studentIdTextBox.getText().isEmpty() || nameTextBox.getText().isEmpty()) {
            return null;
        }

        int id = Integer.parseInt(studentIdTextBox.getText());

        //invalid id
        if(id <=0) {
            return null;
        }
        String fullName = nameTextBox.getText();
        String [] nameParts = fullName.split(" ");
        //invalid name
        if(nameParts.length != 2 || nameParts[0].isEmpty() || nameParts[1].isEmpty() || nameParts[0].length() > 32 || nameParts[1].length() > 32) {
            return null;
        }

        return new Person(id, nameParts[0], nameParts[1]);
    }
}
