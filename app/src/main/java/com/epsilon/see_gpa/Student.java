package com.epsilon.see_gpa;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import model.DatabaseController;
import model.DbInfo;

/**
 * Created by ahmed on 11/12/2016.
 */

public class Student implements Serializable {

    private String matricNo;
    private String name;

    private String yearOfEntry;
    private String modeOfEntry;
    private String cgpa;
    private ArrayList<Course> coursesTaken;

    public String getYearOfEntry() {
        return yearOfEntry;
    }

    public void setYearOfEntry(String yearOfEntry) {
        this.yearOfEntry = yearOfEntry;
    }

    public String getModeOfEntry() {
        return modeOfEntry;
    }

    public void setModeOfEntry(String modeOfEntry) {
        this.modeOfEntry = modeOfEntry;
    }


    public ArrayList<Course> getCoursesTaken() {
        return this.coursesTaken;
    }

    public void setCoursesTaken(ArrayList<Course> coursesTaken) {
        this.coursesTaken = coursesTaken;
    }


    public Student() {


    }

    public String getMatricNo() {
        return matricNo;
    }

    public void setMatricNo(String matricNo) {
        this.matricNo = matricNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCgpa() {
        return cgpa;
    }

    public void setCgpa(String cgpa) {
        this.cgpa = cgpa;
    }

    public void calculateCGPAAndSaveToDB(Context context, String gpSystem) {


        switch (gpSystem) {
            case "7":
                calculateForSeven(context);
                break;
            case "5":
                calculateForFive(context);
                break;
            case "4":
                calculateForFour(context);
                break;
        }


    }

    private void calculateForFour(Context context) {

        int totalUnitsTaken = 0;
        double totalPointAccumulated = 0;
        int[] pointsArray = {4, 3, 2, 1, 0};
        String gradeTypes[] = {"A", "B", "C", "D", "F"};
        for (int i = 0; i < getCoursesTaken().size(); i++) {
            Course course = getCoursesTaken().get(i);
            totalUnitsTaken += Integer.parseInt(course.getUnit());
            for (int j = 0; j < gradeTypes.length; j++) {
                if (course.getGradePoint().equals(gradeTypes[j])) {
                    totalPointAccumulated += Integer.parseInt(course.getUnit()) * pointsArray[j];
                }
            }
        }
        double cgpa = totalPointAccumulated / totalUnitsTaken;
        setCgpa(String.format("%.2f", cgpa));
        DatabaseController databaseController = new DatabaseController(context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbInfo.CGPA, getCgpa());
        databaseController.updateStudentCGPA(contentValues, getMatricNo());
    }

    private void calculateForFive(Context context) {
        int totalUnitsTaken = 0;
        double totalPointAccumulated = 0;
        int[] pointsArray = {5, 4, 3, 2, 1, 0};
        String gradeTypes[] = {"A", "B", "C", "D", "E", "F"};
        for (int i = 0; i < getCoursesTaken().size(); i++) {
            Course course = getCoursesTaken().get(i);
            totalUnitsTaken += Integer.parseInt(course.getUnit());
            for (int j = 0; j < gradeTypes.length; j++) {
                if (course.getGradePoint().equals(gradeTypes[j])) {
                    totalPointAccumulated += Integer.parseInt(course.getUnit()) * pointsArray[j];
                }
            }
        }
        double cgpa = totalPointAccumulated / totalUnitsTaken;
        setCgpa(String.format("%.2f", cgpa));
        DatabaseController databaseController = new DatabaseController(context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbInfo.CGPA, getCgpa());
        databaseController.updateStudentCGPA(contentValues, getMatricNo());
    }

    private void calculateForSeven(Context context) {
        int totalUnitsTaken = 0;
        double totalPointAccumulated = 0;
        int[] pointsArray = {7, 6, 5, 4, 3, 2, 1, 0};
        String gradeTypes[] = {"7 pts", "6 pts", "5 pts", "4 pts", "3 pts", "2 pts", "1 pt", "0 pt"};
        for (int i = 0; i < this.getCoursesTaken().size(); i++) {
            Course course = this.getCoursesTaken().get(i);
            totalUnitsTaken += Integer.parseInt(course.getUnit());
            for (int j = 0; j < gradeTypes.length; j++) {
                if (course.getGradePoint().equals(gradeTypes[j])) {
                    totalPointAccumulated += Integer.parseInt(course.getUnit()) * pointsArray[j];
                }
            }
        }
        double cgpa = totalPointAccumulated / totalUnitsTaken;
        setCgpa(String.format("%.1f", cgpa));
        DatabaseController databaseController = new DatabaseController(context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbInfo.CGPA, this.getCgpa());
        if(databaseController.updateStudentCGPA(contentValues, getMatricNo())>0){
            Log.d("Data","Update db for "+ getMatricNo());
        }else{
            Log.d("Data","Update db");
        }
    }
}