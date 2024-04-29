package com.example.wealth_creation_system;

public class Deadline {
    private String date;
    private String type;
    private String title;

    public Deadline(String date, String type, String title) {
        this.date = date;
        this.type = type;
        this.title = title;
    }

    // Getter for date property
    public String getDate() {
        return date;
    }

    // Setter for date property
    public void setDate(String date) {
        this.date = date;
    }

    // Getter for type property
    public String getType() {
        return type;
    }

    // Setter for type property
    public void setType(String type) {
        this.type = type;
    }

    // Getter for title property
    public String getTitle() {
        return title;
    }

    // Setter for title property
    public void setTitle(String title) {
        this.title = title;
    }
}
