package com.example.wealth_creation_system;

public class GradeEntry {
    private String course;
    private double midterm;
    private double prelim;
    private String finals; // Changed to String

    public GradeEntry(String course, double midterm, double prelim, String finals) {
        this.course = course;
        this.midterm = midterm;
        this.prelim = prelim;
        this.finals = finals; // Initialize the new field
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getMidterm() {
        return midterm;
    }

    public void setMidterm(double midterm) {
        this.midterm = midterm;
    }

    public double getPrelim() {
        return prelim;
    }

    public void setPrelim(double prelim) {
        this.prelim = prelim;
    }

    public String getFinals() {
        return finals;
    }

    public void setFinals(String finals) {
        this.finals = finals;
    }
}
