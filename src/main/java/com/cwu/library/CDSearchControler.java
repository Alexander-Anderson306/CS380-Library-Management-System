package com.cwu.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import java.util.LinkedList;

public class CDSearchControler implements Popup {
    private Searchable parentController;

    @FXML
    private TextArea callNumberTextArea;

    @FXML
    private ListView<String> cdListView;

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
        LinkedList<String> dataToSend = new LinkedList<>();

        //get selected book
        String selectedCD = cdListView.getSelectionModel().getSelectedItem();
        if(selectedCD != null) {
            dataToSend.add(selectedCD);
        }

        if(dataToSend.isEmpty()) {
            //no book selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No DVD Selected");
            alert.setHeaderText("Selection Error");
            alert.setContentText("Please select a DVD from the list before confirming.");
            alert.showAndWait();
            return;
        }

        //send data back to parent controller
        String data = dataToSend.getFirst();
        String split[] = data.split(" ");
        dataToSend.clear();
        dataToSend.add(split[2]);
        parentController.recieveData(dataToSend);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void sendQuery(ActionEvent event) {
        //get fields first
        String title = titleTextBox.getText();
        String year = yearTextBox.getText();
        String callNumber = callNumberTextArea.getText();

        if(title.isEmpty() && year.isEmpty() && callNumber.isEmpty()) {
            //no search criteria entered
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Search Criteria");
            alert.setHeaderText("Search Error");
            alert.setContentText("Please enter at least one search criteria.");
            alert.showAndWait();
            return;
        }

        if(!title.isEmpty() && title.length() > 128){
            //invalid title
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Title");
            alert.setHeaderText("Input Error");
            alert.setContentText("Title cannot exceed 128 characters.");
            alert.showAndWait();
            return;
        }

        if(!year.isEmpty() && year.length() > 4){
            //invalid year
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Year");
            alert.setHeaderText("Input Error");
            alert.setContentText("Year cannot exceed 4 characters.");
            alert.showAndWait();
            return;
        }

        if(!callNumber.isEmpty() && callNumber.length() > 128) {
            //invalid call number
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Call Number");
            alert.setHeaderText("Input Error");
            alert.setContentText("Call Number cannot exceed 128 characters.");
            alert.showAndWait();
            return;
        }

        Item cd = new Item(-1);
        if(!title.isEmpty()) {
            cd.setAttribute("title", title);
        }

        if(!year.isEmpty()) {
            cd.setAttribute("year", year);
        }

        if(!callNumber.isEmpty()) {
            cd.setAttribute("call_number", callNumber);
        }

        //perform search
        LinkedList<Item> results = SQLHandler.queryCDs(cd);

        //display results
        cdListView.getItems().clear();
        for(Item result : results) {
            cdListView.getItems().add(result.toString());
        }
    }

    public void setParentController(Searchable parentController) {
        this.parentController = parentController;
    }
}
