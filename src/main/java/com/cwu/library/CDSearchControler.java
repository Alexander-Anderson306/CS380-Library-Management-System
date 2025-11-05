package com.cwu.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CDSearchControler implements Popup {
    private Searchable parentController;

    @FXML
    private TextArea callNumberTextArea;

    @FXML
    private ListView<?> cdListView;

    @FXML
    private Button confirmButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField titleTextBox;

    @FXML
    private TextField yearTextBox;

    @FXML
    public void confirm(ActionEvent event) {

    }

    @FXML
    public void sendQuery(ActionEvent event) {

    }

    public void setParentController(Searchable parentController) {
        this.parentController = parentController;
    }
}
