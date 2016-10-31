package com.example.waheed.bassem.studentdatabaseapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * studnet data base
 */

@Table(name = "StudentData")
public class StudentData extends Model {
    // If name is omitted, then the field name is used.
    @Column (name = "Name")
    private String mName;
    @Column (name = "AcademicYear")
    private String mAcademicYear;
    @Column (name = "Grade")
    private String mGrade;

    public StudentData () {
        super();
    }

    public StudentData(String name, String academicYear, String grade) {
        super();
        mName = name;
        mAcademicYear = academicYear;
        mGrade = grade;
    }

    public String getName () {
        return mName;
    }

    public String getAcademicYear () {
        return mAcademicYear;
    }

    public String getGrade () {
        return mGrade;
    }

    public void setName (String name) {
        mName = name;
    }

    public void setAcademicYear (String academicYear) {
        mAcademicYear = academicYear;
    }

    public void setGrade (String grade) {
        mGrade = grade;
    }
}
