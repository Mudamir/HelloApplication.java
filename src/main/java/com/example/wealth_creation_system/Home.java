package com.example.wealth_creation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Home {

    @FXML
    private Button btn_Grades;

    @FXML
    private Button btn_deadlines;

    @FXML
    void moveToDeadlinePage(ActionEvent event) {
        try {
            // Load the FXML file for Scene 5 (Profit and Loss)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Deadlines.fxml"));
            Parent EventRegistration = loader.load();

            // Get the current stage
            Stage stage = (Stage) btn_deadlines.getScene().getWindow();

            // Create a new scene with Scene 5 content
            Scene Event = new Scene(EventRegistration);

            // Set the new scene on the stage
            stage.setScene(Event);
        } catch (IOException e) {
            System.err.println("Error loading Event Registration: " + e.getMessage());
        }

    }

    @FXML
    void moveToGradesPage(ActionEvent event) {
        try {
            // Load the FXML file for Scene 5 (Profit and Loss)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Grades-Stats.fxml"));
            Parent EventRegistration = loader.load();

            // Get the current stage
            Stage stage = (Stage) btn_Grades.getScene().getWindow();

            // Create a new scene with Scene 5 content
            Scene Event = new Scene(EventRegistration);

            // Set the new scene on the stage
            stage.setScene(Event);
        } catch (IOException e) {
            System.err.println("Error loading Event Registration: " + e.getMessage());
        }

    }

}
