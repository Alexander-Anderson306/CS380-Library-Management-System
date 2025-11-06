package com.cwu.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.List;
import javafx.scene.control.Alert;
import java.util.LinkedList;

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
        //make sure boxes are not empty
        if(titleTextBox.getText().isEmpty() || authorTextBox.getText().isEmpty() || callNumberTextArea.getText().isEmpty() || isbnTextBox.getText().isEmpty()) {
            //pop up error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Fields");
            alert.setHeaderText("Input Error");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        //get the book query
        Item bookQuery = getQueryItem();

        //this is dumb but i am running out of time
        //query the database and see if we already have that book
        LinkedList<Item> bookList = SQLHandler.queryBooks(bookQuery);

        //in this case we are adding a book
        if(bookList.size() == 0) {
            //generate author
            Person author = getAuthor();

            //if the author does not exist, add it
            if(!SQLHandler.authorExists(author.getId())) {
                SQLHandler.addAuthor(author);
            }

            //add the book
            SQLHandler.addBook(bookQuery);

            //get the book id (this is horribly inefficient im sorry)
            bookList = SQLHandler.queryBooks(bookQuery);
            bookQuery.setId(bookList.get(0).getId());

            //connect book and author
            SQLHandler.addBookAuthor(bookQuery.getId(), author.getId());
        } else {
            //modify the book with what information we have
            SQLHandler.modifyBook(bookQuery);
        }
    }

    /**
     * Deletes a book from the database.
     * First checks that all text boxes are filled in, and if not, shows an error popup.
     * Then gets the book query from the text boxes and queries the database to see if the book already exists.
     * If the book exists, deletes the book author connection first, then deletes the book.
     * @param event The event that triggered this method.
     */
    @FXML
    void delete(ActionEvent event) {
        //make sure boxes are not empty
        if(titleTextBox.getText().isEmpty() || authorTextBox.getText().isEmpty() || callNumberTextArea.getText().isEmpty() || isbnTextBox.getText().isEmpty()) {
            //pop up error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Fields");
            alert.setHeaderText("Input Error");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        //get the book query
        Item bookQuery = getQueryItem();

        //dumb and inefficient
        //query the database and see if we already have that book
        LinkedList<Item> bookList = SQLHandler.queryBooks(bookQuery);

        if(bookList.size() > 0) {
            //delete the book author connection first
            Person author = getAuthor();
            SQLHandler.getAuthorId(author);
            SQLHandler.removeBookAuthor(bookList.get(0).getId(), author.getId());

            //delete the book
            SQLHandler.removeBook(bookList.get(0).getId());
        }
    }

    @FXML
    public void search(ActionEvent event) {
        //load the book search popup
        BookSearchControler controler = App.openPopup("BookSearch.fxml", BookSearchControler.class);
        controler.setParentController(this);
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
        if(data.size() >=1) {
            String firstData = data.get(0);
            //this is so scuffed but cant fix it now
            titleTextBox.setText(firstData);
        }
    }

    /**
     * Creates a Person object based on the data in the author text box.
     * Checks the data for validity and displays an error popup if the data is invalid.
     * @return a Person object representing the author if the data is valid, null otherwise.
     */
    public Person getAuthor() {
        //create author from the text boxes
        String authorName = authorTextBox.getText();
        String [] nameParts = authorName.split(" ");
        //invalid name
        if(nameParts.length != 2 || nameParts[0].isEmpty() || nameParts[1].isEmpty() || nameParts[0].length() > 32 || nameParts[1].length() > 32) {
            //error popup
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Author Name");
            alert.setHeaderText("Author Name Format Error");
            alert.setContentText("Author name must be in the format: FirstName LastName (max 32 characters each).");
            alert.showAndWait();
            return null;
        }

        Person author = new Person(-1, nameParts[0], nameParts[1]);
        SQLHandler.getAuthorId(author);
        return author;
    }

    /**
     * Gets an Item object representing the query criteria entered in the text boxes
     * @return an Item object representing the query criteria
     */
    public Item getQueryItem() {
        Item bookQuery = new Item(-1);
        String title = titleTextBox.getText();
        String year = yearTextBox.getText();
        String isbn = isbnTextBox.getText();
        String callNumber = callNumberTextArea.getText();

        if(!title.isEmpty() && title.length() > 128) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid title");
            alert.setHeaderText("Invalid title Information");
            alert.setContentText("Title must be less than 128 characters");

            // Show the popup and wait for user to close
            alert.showAndWait();
        }

        if(!year.isEmpty() && year.length() > 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid year");
            alert.setHeaderText("Invalid year Information");
            alert.setContentText("Year must be an integer equal to 4 characters");
        }

        if(!isbn.isEmpty() && isbn.length() > 13) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid isbn");
            alert.setHeaderText("Invalid isbn Information");
            alert.setContentText("ISBN must be an integer equal to 13 characters");
        }

        if(!callNumber.isEmpty() && callNumber.length() > 128) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid call number");
            alert.setHeaderText("Invalid call number Information");
            alert.setContentText("Call number must be less than 128 characters");
        }

        if(!title.isEmpty()) {
            bookQuery.setAttribute("title", title);
        }

        if(!year.isEmpty()) {
            bookQuery.setAttribute("year", year);
        }

        if(!isbn.isEmpty()) {
            bookQuery.setAttribute("isbn", isbn);
        }

        if(!callNumber.isEmpty()) {
            bookQuery.setAttribute("call_number", callNumber);
        }

        return bookQuery;
    }
}
