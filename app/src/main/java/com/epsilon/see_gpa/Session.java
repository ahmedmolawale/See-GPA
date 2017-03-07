package com.epsilon.see_gpa;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import model.DatabaseController;
import model.DbInfo;

/**
 * Created by ahmed on 16/12/2016.
 */

public class Session {


    private int totalUnitPassed;
    private int totalUnitReg;
    private double gpA;
    private ArrayList<Course> coursesTaken;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    private String session;
    private String level;




    public ArrayList<Course> getCoursesTaken() {
        return coursesTaken;
    }

    public void setCoursesTaken(ArrayList<Course> coursesTaken) {
        this.coursesTaken = coursesTaken;
    }

    public int getTotalUnitPassed() {
        return totalUnitPassed;
    }

    public void setTotalUnitPassed(int totalUnitPassed) {
        this.totalUnitPassed = totalUnitPassed;
    }

    public int getTotalUnitReg() {
        return totalUnitReg;
    }

    public void setTotalUnitReg(int totalUnitReg) {
        this.totalUnitReg = totalUnitReg;
    }

    public double getGpA() {
        return gpA;
    }

    public void setGpA(double gpA) {
        this.gpA = gpA;
    }

    public void computeUnits() {
        int unitsPassed = 0;
        int unitsRegistered = 0;
        for (int i = 0; i < getCoursesTaken().size(); i++) {
            Course course = getCoursesTaken().get(i);
            if (Double.parseDouble(course.getScore()) >= 40) {
                unitsPassed += Integer.parseInt(course.getUnit());
            }
            unitsRegistered += Integer.parseInt(course.getUnit());
        }
        setTotalUnitPassed(unitsPassed);
        setTotalUnitReg(unitsRegistered);

    }

    public void computeSessionGP(String gpSystem) {
        switch (gpSystem) {
            case "7":
                calculateForSeven();
                break;
            case "5":
                calculateForFive();
                break;
            case "4":
                calculateForFour();
                break;
        }
    }

    private void calculateForFour() {
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
        double gpa = totalPointAccumulated / totalUnitsTaken;
        setGpA(gpa);
    }

    private void calculateForFive() {

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
        double gpa = totalPointAccumulated / totalUnitsTaken;
        setGpA(gpa);
    }
    private void calculateForSeven() {

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
        double gpa = totalPointAccumulated / totalUnitsTaken;
       setGpA(gpa);
    }
}
