/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 first_name last_name
 */

package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InventoryTracker extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("InventoryTracker.fxml"));

            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("InventoryTracker");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
