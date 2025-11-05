package com.cwu.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        switchScene("Checkout.fxml");
        primaryStage.setTitle("Library Management System");
        primaryStage.show();

    }

    public static void switchScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(App.class.getResource(fxmlFile));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}