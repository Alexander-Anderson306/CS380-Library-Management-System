package com.cwu.library;

import java.util.List;
import javafx.event.ActionEvent;

public interface Searchable {
    /**
     * Searches for items based on user input.
     * @param event
     */
    void search(ActionEvent event);

    /**
     * Receives data from the search popup.
     * @param data
     */
    void recieveData(List<String> data);
}