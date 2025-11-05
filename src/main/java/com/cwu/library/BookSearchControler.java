package com.cwu.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BookSearchControler {

    @FXML
    private TextField authorTextBox;

    @FXML
    private ListView<?> bookListView;

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

    @FXML
    void confirmSelection(ActionEvent event) {

    }

    @FXML
    void search(ActionEvent event) {

    }

}
