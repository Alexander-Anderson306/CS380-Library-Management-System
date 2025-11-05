package com.cwu.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
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

    /**
     * Switches the scene to the specified FXML file.
     * @param fxmlFile
     */
    public static void switchScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(App.class.getResource(fxmlFile));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a popup window with the specified FXML file and returns its controller.
     * @param fxmlFile
     * @param controllerClass The class of the controller.
     * @param <T> The type of the controller (I.E a class).
     * @return The controller of the popup window.
     */
    public static <T> T openPopup(String fxmlFile, Class<T> controllerClass) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlFile));
            Parent root = loader.load();

            //give popup reference to its parent stage
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Search");
            popupStage.initOwner(primaryStage);
            //user cannot interact with primary stage while popup is open
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.showAndWait();

            //return the controller of the popup
            return controllerClass.cast(loader.getController());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        launch();
    }

}