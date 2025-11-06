package com.cwu.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.util.LinkedList;
import javafx.scene.control.Alert;

public class BookSearchControler implements Popup {
    private Searchable parentController;

    @FXML
    private TextField authorTextBox;

    @FXML
    private ListView<String> bookListView;

    @FXML
    private TextArea callNumberTextArea;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField isbnTextBox;

    @FXML
    private Button searchButton;

    @FXML
    private TextField titleTextBox;

    @FXML
    private TextField yearTextBox;

    /**
     * Confirms the selected book and sends it back to the parent controller.
     * @param event
     */
    @FXML
    public void confirm(ActionEvent event) {
        LinkedList<String> dataToSend = new LinkedList<>();

        //get selected book
        String selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if(selectedBook != null) {
            dataToSend.add(selectedBook);
        }

        if(dataToSend.isEmpty()) {
            //no book selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Book Selected");
            alert.setHeaderText("Selection Error");
            alert.setContentText("Please select a book from the list before confirming.");
            alert.showAndWait();
            return;
        }

        //send data back to parent controller
        parentController.recieveData(dataToSend);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Sends a query to search for books based on the input fields.
     * @param event
     */
    @FXML
    public void sendQuery(ActionEvent event) {
        //gather search parameters
        String title = titleTextBox.getText();
        String author = authorTextBox.getText();
        String isbn = isbnTextBox.getText();
        String year = yearTextBox.getText();
        String callNumber = callNumberTextArea.getText();


        if(title.isEmpty() && author.isEmpty() && isbn.isEmpty() && year.isEmpty()) {
            //no search parameters provided
            return;
        } 

        String parts[] = author.split(" ");
        if(parts.length != 2 || parts.length == 0 || parts[0].isEmpty() || parts[1].isEmpty() || parts[0].length() > 32 || parts[1].length() > 32){
            //invalid author name
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Author Name");
            alert.setHeaderText("Author Name Format Error");
            alert.setContentText("Author name must be in the format: FirstName LastName (max 32 characters each).");
            alert.showAndWait();
            return;
        }

        if(!title.isEmpty() && title.length() > 128){
            //invalid title
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Title");
            alert.setHeaderText("Title Length Exceeded");
            alert.setContentText("Title cannot exceed 128 characters.");
            alert.showAndWait();
            return;
        }

        if(!isbn.isEmpty() && isbn.length() > 13){
            //invalid isbn
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid ISBN");
            alert.setHeaderText("ISBN Length Exceeded");
            alert.setContentText("ISBN cannot exceed 13 characters.");
            alert.showAndWait();
            return;
        }

        if(!year.isEmpty() && year.length() > 4){
            //invalid year
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Year");
            alert.setHeaderText("Year Length Exceeded");
            alert.setContentText("Year cannot exceed 4 characters.");
            alert.showAndWait();
            return;
        }

        if(!callNumber.isEmpty() && callNumber.length() > 128) {
            //invalid call number
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Call Number");
            alert.setHeaderText("Call Number Length Exceeded");
            alert.setContentText("Call Number cannot exceed 128 characters.");
            alert.showAndWait();
            return;
        }

        //Create book query item
        Item bookQuery = new Item(-1);
        if(!title.isEmpty()) {
            bookQuery.setAttribute("title", title);
        }

        if(!isbn.isEmpty()) {
            bookQuery.setAttribute("isbn", isbn);
        }

        if(!year.isEmpty()) {
            bookQuery.setAttribute("year", year);
        }

        if(!callNumber.isEmpty()) {
            bookQuery.setAttribute("call_number", callNumber);
        }

        //create author person
        Person authorPerson = new Person(-1, parts[0], parts[1]);

        LinkedList<Item> results;
        if(title.isEmpty() && isbn.isEmpty() && year.isEmpty()) {
            //search by author only
            results = SQLHandler.getBooksByAuthor(authorPerson);
        } else if(author.isEmpty()) {
            //search by title, isbn, year call number
            results = SQLHandler.queryBooks(bookQuery);
        } else {
            //search by both author and other parameters
            results = SQLHandler.queryBooks(bookQuery);
            results = SQLHandler.Intersect.intersect(results, SQLHandler.getBooksByAuthor(authorPerson));
        }

        //display results in list view
        bookListView.getItems().clear();
        for(Item book : results) {
            bookListView.getItems().add(book.toString());
        }
    }

    /**
     * Sets the parent controller of this popup.
     * @param parent The parent controller.
     */
    public void setParentController(Searchable parent) {
        this.parentController = parent;
    }

}
