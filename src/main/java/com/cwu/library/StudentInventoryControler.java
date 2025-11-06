package com.cwu.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.util.LinkedList;

public class StudentInventoryControler {

    @FXML
    private MenuItem bookMenu;

    @FXML
    private MenuItem cdMenu;

    @FXML
    private MenuItem checkoutMenu;

    @FXML
    private ListView<String> itemsListView;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TextField nameTextBox;

    @FXML
    private Button searchButton;

    @FXML
    private TextField studentIdTextBox;

    @FXML
    private Button removeStudentButton;

    @FXML
    void search(ActionEvent event) {
        //load the student
        Person student = getStudent();
        if(student == null) {
            return;
        }

        //load the student's items
        LinkedList<Item> items = SQLHandler.getCheckedoutItems(student);

        //display the items
        itemsListView.getItems().clear();
        for(Item item : items) {
            itemsListView.getItems().add(item.toString());
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
     * Switches to the Checkout scene.
     * @param event
     */
    @FXML
    void switchToCheckout(ActionEvent event) {
        App.switchScene("Checkout.fxml");
    }

    @FXML
    void removeStudent(ActionEvent event) {
        Person student = getStudent();
        if(student == null) {
            return;
        }

        //delete inventory first
        SQLHandler.removeStudentInventory(student.getId());

        //delete student
        SQLHandler.removeStudent(student.getId());
    }

    /**
     * Returns a Person object based on the data in the student name text box and student id text box.
     * Checks the data for validity and displays an error popup if the data is invalid.
     * @return a Person object representing the student if the data is valid, null otherwise.
     */
    public Person getStudent() {
        String fullName = nameTextBox.getText();
        String [] nameParts = fullName.split(" ");
        int id = Integer.parseInt(studentIdTextBox.getText());

        if(nameParts.length != 2 || nameParts[0].isEmpty() || nameParts[1].isEmpty() || nameParts[0].length() > 32 || nameParts[1].length() > 32) {
            //error poopup
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Student Name");
            alert.setHeaderText("Student Name Format Error");
            alert.setContentText("Student name must be in the format: FirstName LastName (max 32 characters each).");
            alert.showAndWait();
            return null;
        }

        return new Person(id, nameParts[0], nameParts[1]);
    }
}
