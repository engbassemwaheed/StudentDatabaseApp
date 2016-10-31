package com.example.waheed.bassem.studentdatabaseapp;

/**
 * this will bring back the choices of the dialog
 */

public interface DialogCallback {
    void createItem(String name, String academicYear, String grade);
    void readItem(String name);
    void readAllItems();
    void updateItem(String oldName, String newName, String newAcademicYear, String newGrade);
    void deleteItem(String name);
    void deleteTable();
    void enableButtons();
}
