package com.example.wealth_creation_system;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;

public class Deadlines {

    @FXML
    private TableView<Deadline> Table_Deadline;

    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_done;

    @FXML
    private Button btn_home;

    @FXML
    private Button btn_search;

    @FXML
    private Button btn_set;

    @FXML
    private TableColumn<Deadline, String> clm_Date;

    @FXML
    private TableColumn<Deadline, String> clm_Title;

    @FXML
    private TableColumn<Deadline, String> clm_Type;

    @FXML
    private TextField tb_Search;

    @FXML
    private TextField tb_Title;

    @FXML
    private TextField tb_Type;

    @FXML
    private DatePicker tb_date;

    @FXML
    private ComboBox<String> cmb_type;

    @FXML
    void Search_data(ActionEvent event) {
        // Get the search keyword from the tb_Search TextField
        String keyword = tb_Search.getText().trim();

        // Create a FilteredList to filter the data in the TableView
        FilteredList<Deadline> filteredData = new FilteredList<>(Table_Deadline.getItems());

        if (keyword == null || keyword.isEmpty()) {
            // If the search keyword is empty, load all deadlines from the file
            loadDeadlinesFromFile();
            return;
        }

        // Set a predicate to filter the data based on the keyword
        filteredData.setPredicate(deadline -> {
            // Check if the deadline's date, type, or title contains the keyword (case-insensitive)
            String lowerCaseKeyword = keyword.toLowerCase();
            return deadline.getDate().toLowerCase().contains(lowerCaseKeyword)
                    || deadline.getType().toLowerCase().contains(lowerCaseKeyword)
                    || deadline.getTitle().toLowerCase().contains(lowerCaseKeyword);
        });

        // Wrap the filtered data in a SortedList to enable sorting
        SortedList<Deadline> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList to the TableView
        Table_Deadline.setItems(sortedData);
    }

    @FXML
    void clear_textfield(ActionEvent event) {
        tb_Title.clear();
        tb_date.setValue(null);
        tb_Type.clear();
    }

    @FXML
    void finished_data(ActionEvent event) {
        // Get the selected item from the TableView
        Deadline selectedDeadline = Table_Deadline.getSelectionModel().getSelectedItem();
        if (selectedDeadline != null) {
            // Remove the selected item from the TableView
            Table_Deadline.getItems().remove(selectedDeadline);
            // You may also want to remove the data from the text file
            // Update the text file with the current data in the TableView
            saveDeadlinesToFile();
        } else {
            // Show an alert if no row is selected
            showAlert("Please select a deadline to mark as finished.");
        }
    }

    private void saveDeadlinesToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Deadlines.txt"))) {
            for (Deadline deadline : Table_Deadline.getItems()) {
                // Write each deadline's data to the text file
                writer.println(deadline.getTitle() + "," + deadline.getType() + "," + deadline.getDate());
            }
        } catch (IOException e) {
            // Handle file writing errors
            showAlert("Error saving deadlines to file: " + e.getMessage());
        }
    }

    @FXML
    void moveToHomePage(ActionEvent event) {
        try {
            // Load the FXML file for Scene 5 (Profit and Loss)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent EventRegistration = loader.load();

            // Get the current stage
            Stage stage = (Stage) btn_home.getScene().getWindow();

            // Create a new scene with Scene 5 content
            Scene Event = new Scene(EventRegistration);

            // Set the new scene on the stage
            stage.setScene(Event);
        } catch (IOException e) {
            System.err.println("Error loading Event Registration: " + e.getMessage());
        }
    }

    @FXML
    void set_deadline(ActionEvent event) {
        // Retrieve data from the input fields
        String date = String.valueOf(tb_date.getValue());
        String type = cmb_type.getValue().toString(); // Convert type to lowercase
        String title = tb_Title.getText(); // Convert title to lowercase

        // Validate the input data
        if (date.isEmpty() || type.isEmpty() || title.isEmpty()) {
            // Show an alert if any of the fields are empty
            showAlert("Please fill in all fields.");
            return;
        }

        // Check for duplicate titles
        if (isDuplicateTitle(title)) {
            // Show an alert if the title already exists
            showAlert("Title already exists. Please enter a unique title.");
            tb_Title.clear();
            return;
        }

        // Create a new instance of Deadline object
        Deadline deadline = new Deadline(date, type, title);

        // Add the deadline to the TableView
        Table_Deadline.getItems().add(deadline);

        // Save the deadline to the text file
        saveDeadlineToFile(deadline);

        // Clear input fields after adding the deadline
        tb_date.setValue(null);
        cmb_type.setValue(null);
        tb_Title.clear();
    }
    private void saveDeadlineToFile(Deadline deadline) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Deadlines.txt", true))) {
            // Append the deadline data to the text file
            writer.write(deadline.getTitle() + "," + deadline.getType() + "," + deadline.getDate() + "\n");
        } catch (IOException e) {
            System.err.println("Error saving deadline to file: " + e.getMessage());
        }
    }

    private boolean isDuplicateTitle(String newTitle) {
        // Check if the new title already exists in the TableView
        for (Deadline item : Table_Deadline.getItems()) {
            if (item.getTitle().equalsIgnoreCase(newTitle)) {
                return true;
            }
        }
        return false;
    }

    private void showAlert(String message) {
        // Display an alert with the specified message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        // Create a list of items to add to the ComboBox
        ObservableList<String> items = FXCollections.observableArrayList("Assignment", "Quiz", "Exam");


        //columns
        clm_Date.setCellValueFactory(new PropertyValueFactory<>("date"));
        clm_Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        clm_Type.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Set the items to the ComboBox
        cmb_type.setItems(items);


        loadDeadlinesFromFile();

    }

    private void loadDeadlinesFromFile() {
        ObservableList<Deadline> deadlineList = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader("Deadlines.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by comma to extract date, type, and title
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String title = parts[0];
                    String type = parts[1];
                    String date = parts[2];
                    // Create a new Deadline object and add it to the list
                    deadlineList.add(new Deadline(date, type, title));
                }
            }
        } catch (IOException e) {
            // Handle file reading errors
            showAlert("Error loading deadlines from file: " + e.getMessage());
        }

        // Set the loaded data to the TableView
        Table_Deadline.setItems(deadlineList);
    }
}
