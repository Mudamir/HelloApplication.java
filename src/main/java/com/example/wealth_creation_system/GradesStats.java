package com.example.wealth_creation_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class GradesStats {

    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_home;

    @FXML
    private Button btn_rate;

    @FXML
    private Button btn_remove;

    @FXML
    private Button btn_search;

    @FXML
    private TableColumn<GradeEntry, String> clm_Course;

    @FXML
    private TableColumn<GradeEntry, String> clm_Finals;

    @FXML
    private TableColumn<GradeEntry, Integer> clm_Midterm;

    @FXML
    private TableColumn<GradeEntry, Integer> clm_Prelim;

    @FXML
    private TableView<GradeEntry> table_Grades;

    @FXML
    private TextField tb_Course;

    @FXML
    private TextField tb_Midterm;

    @FXML
    private TextField tb_Prelim;

    @FXML
    private TextField tb_Search;


    @FXML
    void Check_Grade(ActionEvent event) {
        // Retrieve data from text fields
        String course = tb_Course.getText();
        double midterm;
        double prelim;

        try {
            midterm = Double.parseDouble(tb_Midterm.getText());
            prelim = Double.parseDouble(tb_Prelim.getText());
        } catch (NumberFormatException e) {
            // Handle invalid input format
            displayError("Invalid input format", "Midterm and prelim scores must be numeric.");
            return;
        }

        // Check if prelim and midterm are within the valid range (0 to 100)
        if (prelim < 0 || prelim > 100 || midterm < 0 || midterm > 100) {
            // Show an error message
            displayError("Invalid score", "Prelim and midterm scores must be between 0 and 100.");
            return;
        }

        // Check if the course already exists in the file
        if (courseExists(course.toLowerCase())) {
            displayError("Duplicate course", "Course already exists in the file.");
            return;
        }

        // Calculate the final score
        double finalScore = 72 - ((prelim / 5) + (midterm / 2.5));

        // Ensure the final score is between 0 and 100
        double finalsValue = (finalScore * 2.5);

        // Format the finals value to have two decimal places
        String finals = String.format("%.2f", finalsValue);

        // If finals value exceeds 100, set it as "Failed"
        if (finalsValue > 100) {
            finals = "Failed";
        }

        // Create a new GradeEntry object
        GradeEntry entry = new GradeEntry(course, midterm, prelim, finals);

        // Save data to text file
        saveToFile(entry);

        // Add the entry to the table
        table_Grades.getItems().add(entry);

        // Clear text fields
        tb_Course.clear();
        tb_Midterm.clear();
        tb_Prelim.clear();
        tb_Search.clear();
    }

    private boolean courseExists(String course) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Grades.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase(); // Convert the line to lowercase
                String[] parts = line.split("-");
                if (parts.length > 0 && parts[0].equals(course)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void displayError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Remove_Grade(ActionEvent event) {
        // Get the selected item
        GradeEntry selectedItem = table_Grades.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            // Remove the selected item from the TableView
            table_Grades.getItems().remove(selectedItem);

            // Remove the entry from the text file
            removeEntryFromFile(selectedItem);
        } else {
            // If no item is selected, show a message or handle it accordingly
            System.out.println("No item selected.");
        }
        tb_Course.clear();
        tb_Midterm.clear();
        tb_Prelim.clear();
        tb_Search.clear();
    }

    private void removeEntryFromFile(GradeEntry entryToRemove) {
        try {
            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get("Grades.txt"));

            // Create a new list to store the updated lines
            List<String> updatedLines = new ArrayList<>();

            // Iterate over each line
            for (String line : lines) {
                // Split the line by the delimiter
                String[] parts = line.split("-");

                // Extract the course name from the line
                String courseName = parts[0];

                // Check if the current line corresponds to the entry to remove
                if (!courseName.equals(entryToRemove.getCourse())) {
                    // If it's not the entry to remove, add the line to the updated list
                    updatedLines.add(line);
                }
            }

            // Write the updated lines back to the file
            Files.write(Paths.get("Grades.txt"), updatedLines);
        } catch (IOException e) {
            System.err.println("Error removing entry from file: " + e.getMessage());
        }
    }



    @FXML
    void Search_course(ActionEvent event) {
        // Get the search keyword from the tb_Search TextField
        String keyword = tb_Search.getText().trim();

        FilteredList<GradeEntry> filteredData = new FilteredList<>(table_Grades.getItems());

        if (keyword == null || keyword.isEmpty()) {
            // If the search keyword is empty, load all grades from the file
            loadFromFile();
            return;
        }

        // Set a predicate to filter the data based on the keyword
        filteredData.setPredicate(entry -> {
            // Check if the course name contains the keyword (case-insensitive)
            return entry.getCourse().toLowerCase().contains(keyword.toLowerCase());
        });

        // Wrap the filtered data in a SortedList to enable sorting
        SortedList<GradeEntry> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList to the TableView
        table_Grades.setItems(sortedData);
    }



    @FXML
    void clear_textbox(ActionEvent event) {
        // Clear all text fields
        tb_Course.clear();
        tb_Midterm.clear();
        tb_Prelim.clear();
        tb_Search.clear(); // Assuming you also want to clear the search field
    }


    @FXML
    void moveToHomePage(ActionEvent event) {
        try {
            // Load the FXML file for Home.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent home = loader.load();

            // Get the current stage
            Stage stage = (Stage) btn_home.getScene().getWindow();

            // Create a new scene with Home.fxml content
            Scene scene = new Scene(home);

            // Set the new scene on the stage
            stage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Error loading Home: " + e.getMessage());
        }
    }

    @FXML
    void Rate(ActionEvent event) {
        // Call Check_Grade method to add data to the table and save to file
        Check_Grade(event);
    }

    @FXML
    public void initialize() {
        // Initialize column cell values factories
        clm_Course.setCellValueFactory(new PropertyValueFactory<>("course"));
        clm_Finals.setCellValueFactory(new PropertyValueFactory<>("finals"));
        clm_Midterm.setCellValueFactory(new PropertyValueFactory<>("midterm"));
        clm_Prelim.setCellValueFactory(new PropertyValueFactory<>("prelim"));
        loadFromFile();
    }

    private void loadFromFile() {
        ObservableList<GradeEntry> gradesList = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader("Grades.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-");
                if (parts.length == 4) {
                    String course = parts[0];
                    double prelim = Double.parseDouble(parts[1]);
                    double midterm = Double.parseDouble(parts[2]);
                    String finals = parts[3];

                    // Create a GradeEntry object and add it to the list
                    gradesList.add(new GradeEntry(course, midterm, prelim, finals));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading data from file: " + e.getMessage());
        }

        // Set the items of the table view to the loaded list
        table_Grades.setItems(gradesList);
    }




    private void saveToFile(GradeEntry entry) {
        // Construct data string with "-" delimiter
        String data = entry.getCourse() + "-" + entry.getPrelim() + "-" + entry.getMidterm() + "-" + entry.getFinals();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Grades.txt", true))) {
            writer.write(data);
            writer.newLine(); // Add new line for the next entry
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
