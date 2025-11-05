package com.cwu.library;

import javafx.event.ActionEvent;

public interface Popup {
    void setParentController(Searchable parent);

    void confirm(ActionEvent event);

    void sendQuery(ActionEvent event);
}