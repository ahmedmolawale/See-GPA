package com.epsilon.see_gpa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import model.DatabaseController;
import model.DbInfo;

/**
 * Created by MOlawale on 8/10/2015.
 */
public class AnalysisFragment extends Fragment {


    private Student student;
    private SharedPreferences sharedPreferences;
    private String gpSystem;
    private TextView name, matric, modeOfEntry, yearOfEntry, unitPassed, unitReg, cgpa, classOfGrade, courseTaken;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        View v;
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(MainScreen.MAINSCREEN_TO_DETAIL)) {
            student = (Student) intent.getSerializableExtra(MainScreen.MAINSCREEN_TO_DETAIL);
        }
        gpSystem = sharedPreferences.getString(MainScreen.CURRENT_CGPA, "7");
        v = inflater.inflate(R.layout.analysis_fragment_layout, container, false);
        name = (TextView) v.findViewById(R.id.name_on_analysis);
        matric = (TextView) v.findViewById(R.id.matric_on_analysis);
        yearOfEntry = (TextView) v.findViewById(R.id.year_of_entry_on_analysis);
        modeOfEntry = (TextView) v.findViewById(R.id.mode_of_entry_on_analysis);
        courseTaken = (TextView) v.findViewById(R.id.courses_taken_on_analysis);
        unitReg = (TextView) v.findViewById(R.id.unit_reg_on_analysis);
        unitPassed = (TextView) v.findViewById(R.id.unit_passed_on_analysis);
        cgpa = (TextView) v.findViewById(R.id.cgpa_on_analysis);
        classOfGrade = (TextView) v.findViewById(R.id.class_of_grade_on_analysis);
        Session session = getStudentCourses();

        name.setText(student.getName());
        matric.setText(student.getMatricNo());
        yearOfEntry.setText(student.getYearOfEntry());
        modeOfEntry.setText(student.getModeOfEntry());
        courseTaken.setText(Integer.toString(session.getCoursesTaken().size()));
        unitReg.setText(Integer.toString(session.getTotalUnitReg()));
        unitPassed.setText(Integer.toString(session.getTotalUnitPassed()));

        String classOfGrade;
        if (gpSystem.equals("7")) {
            cgpa.setText(String.format("%.1f", session.getGpA()));
            double realCgpa = Double.parseDouble(cgpa.getText().toString());
            if (realCgpa >= 6.0)
                classOfGrade = "First Class. Congratulations!!!";
            else if (realCgpa >= 4.6)
                classOfGrade = "Second Class (Upper Division).";
            else if (realCgpa >= 2.6)
                classOfGrade = "Second Class (Lower Division).";
            else if (realCgpa >= 1.6)
                classOfGrade = "Third Class.";
            else if (realCgpa >= 1.0)
                classOfGrade = "Pass.";
            else
                classOfGrade = "Fail";
            this.classOfGrade.setText(classOfGrade);

        } else if(gpSystem.equals("5")){
            cgpa.setText(String.format("%.2f", session.getGpA()));
            double realCgpa = Double.parseDouble(cgpa.getText().toString());

            if (realCgpa >= 4.50)
                classOfGrade = "First Class. Congratulations!!!";
            else if (realCgpa >= 3.50)
                classOfGrade = "Second Class (Upper Division).";
            else if (realCgpa >= 2.40)
                classOfGrade = "Second Class (Lower Division).";
            else if (realCgpa >= 1.50)
                classOfGrade = "Third Class.";
            else if(realCgpa >=1.00)
                classOfGrade = "Pass.";
            else
                classOfGrade = "Fail.";
            this.classOfGrade.setText(classOfGrade);

        }else{
            cgpa.setText(String.format("%.2f", session.getGpA()));
            double realCgpa = Double.parseDouble(cgpa.getText().toString());

            if (realCgpa >= 3.50)
                classOfGrade = "First Class. Congratulations!!!";
            else if (realCgpa >= 3.00)
                classOfGrade = "Second Class (Upper Division).";
            else if (realCgpa >= 2.00)
                classOfGrade = "Second Class (Lower Division).";
            else if (realCgpa >= 1.00)
                classOfGrade = "Third Class.";
            else
                classOfGrade = "Fail.";
            this.classOfGrade.setText(classOfGrade);

        }
        return v;
    }

    private Session getStudentCourses() {
        DatabaseController databaseController = new DatabaseController(getActivity());

        Cursor cursor = databaseController.getStudentGrades(student.getMatricNo());//shpuld get the grades and course detail

        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session();
        if (cursor != null && cursor.moveToFirst()) {
            ArrayList<Course> courses = new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                Course course = new Course();
                course.setCourseCode(cursor.getString(cursor.getColumnIndex(DbInfo.COURSE_CODE)));
                course.setSession(cursor.getString(cursor.getColumnIndex(DbInfo.SESSION)));
                course.setScore(cursor.getString(cursor.getColumnIndex(DbInfo.TOTAL)));
                Cursor cursor2 = databaseController.getCourseDetails(course.getCourseCode());
                course.setStatus(cursor2.getString(cursor2.getColumnIndex(DbInfo.STATUS)));
                course.setUnit(cursor2.getString(cursor2.getColumnIndex(DbInfo.UNIT)));
                course.computeGP(gpSystem);
                courses.add(course);
            }
            session.setCoursesTaken(courses);
            session.computeUnits();
            session.computeSessionGP(gpSystem);
        } else {
            Toast.makeText(getActivity(), "Cursor is empty", Toast.LENGTH_LONG).show();
        }
        return session;
    }
}
