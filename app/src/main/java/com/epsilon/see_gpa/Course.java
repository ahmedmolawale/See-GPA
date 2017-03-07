package com.epsilon.see_gpa;

/**
 * Created by ahmed on 11/12/2016.
 */

public class Course {


    private String courseCode;
    private String courseTitle;
    private String unit;
    private String status;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    private String session;

    public String getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(String gradePoint) {
        this.gradePoint = gradePoint;
    }

    private String gradePoint;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    private String level;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    private String score;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public void computeGP(String gradePoint) {

        String gradeObtained ="";
        switch (gradePoint) {
            case "7":
                if (Double.parseDouble(getScore()) >= 70) {
                    gradeObtained = "7 pts";
                } else if (Double.parseDouble(getScore()) >= 65 && Double.parseDouble(getScore()) <= 69) {
                    gradeObtained = "6 pts";
                } else if (Double.parseDouble(getScore()) >= 60 && Double.parseDouble(getScore()) <= 64) {
                    gradeObtained = "5 pts";
                } else if (Double.parseDouble(getScore()) >= 55 && Double.parseDouble(getScore()) <= 59) {
                    gradeObtained = "4 pts";
                } else if (Double.parseDouble(getScore()) >= 50 && Double.parseDouble(getScore()) <= 54) {
                    gradeObtained = "3 pts";
                } else if (Double.parseDouble(getScore()) >= 45 && Double.parseDouble(getScore()) <= 49) {
                    gradeObtained = "2 pts";
                } else if (Double.parseDouble(getScore()) >= 40 && Double.parseDouble(getScore()) <= 44) {
                    gradeObtained = "1 pt";
                } else {
                    gradeObtained = "0 pt";
                }
                break;
            case "5":
                if (Double.parseDouble(getScore()) >= 70) {
                    gradeObtained = "A";
                } else if (Double.parseDouble(getScore()) >= 60 && Double.parseDouble(getScore()) <= 69) {
                    gradeObtained = "B";
                } else if (Double.parseDouble(getScore()) >= 50 && Double.parseDouble(getScore()) <= 59) {
                    gradeObtained = "C";
                } else if (Double.parseDouble(getScore()) >= 45 && Double.parseDouble(getScore()) <= 49) {
                    gradeObtained = "D";
                } else if (Double.parseDouble(getScore()) >= 40 && Double.parseDouble(getScore()) <= 44) {
                    gradeObtained = "E";
                } else {
                    gradeObtained = "F";
                }
                break;
            case "4":
                if (Double.parseDouble(getScore()) >= 70) {
                    gradeObtained = "A";
                } else if (Double.parseDouble(getScore()) >= 60 && Double.parseDouble(getScore())<= 69) {
                    gradeObtained = "B";
                } else if (Double.parseDouble(getScore()) >= 50 && Double.parseDouble(getScore()) <= 59) {
                    gradeObtained = "C";
                } else if (Double.parseDouble(getScore()) >= 45 && Double.parseDouble(getScore()) <= 49) {
                    gradeObtained = "D";
                } else {
                    gradeObtained = "F";
                }
                break;

        }
        setGradePoint(gradeObtained);
    }
}
