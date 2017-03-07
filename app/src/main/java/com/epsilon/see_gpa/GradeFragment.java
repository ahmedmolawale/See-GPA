package com.epsilon.see_gpa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import model.DatabaseController;
import model.DbInfo;

;

/**
 * Created by MOlawale on 8/8/2015.
 */
public class GradeFragment extends Fragment {


    private SharedPreferences sharedPreferences;
    private Student student;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    String gpSystem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(MainScreen.MAINSCREEN_TO_DETAIL)) {

            student = (Student) intent.getSerializableExtra(MainScreen.MAINSCREEN_TO_DETAIL);
        }
        gpSystem = sharedPreferences.getString(MainScreen.CURRENT_CGPA, "7");
        v = inflater.inflate(R.layout.grades_layout, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_grade);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        GradeFragmentRecyclerAdapter gradeFragmentRecyclerAdapter  = new GradeFragmentRecyclerAdapter(getStudentCourses());
        recyclerView.setAdapter(gradeFragmentRecyclerAdapter);

        return v;

    }
    private Session getStudentCourses() {
        DatabaseController databaseController = new DatabaseController(getActivity());

        Cursor cursor = databaseController.getStudentGrades(student.getMatricNo());//shpuld get the grades and course detail

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

//    private void createTheOptions(final long id) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setCancelable(true);
//        builder.setTitle("Options");
//        CharSequence options[] = {"Edit Grade", "Delete Grade"};
//
//        builder.setSingleChoiceItems(options, 0, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case 0:
//                        itemSelected = which;
//
//                        break;
//                    case 1:
//                        itemSelected = which;
//
//
//                        break;
//
//                }
//
//            }
//        });
//        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (itemSelected) {
//
//                    case 0:
//                        //create an activity to edit the grade
//                        Intent intent = new Intent(getActivity(), EditGrade.class);
//                        String data[] = {matric, level, Long.toString(id)};
//                        intent.putExtra(Intent.EXTRA_TEXT, data);
//                        startActivity(intent);
//                        break;
//                    case 1:
//                        Uri newUri = ContentUris.withAppendedId(GpmContentProvider.CONTENT_URI_GRADES, id);
//                        contentResolver.delete(newUri, null, null);
//                        Toast.makeText(getActivity(), "Grade deleted successfully.", Toast.LENGTH_SHORT).show();
//                        //refresh the list after delete
//                        String projection[] = {DbInfo.ID, DbInfo.COURSE_CODE, DbInfo.UNIT, DbInfo.SCORE, DbInfo.GRADE};
//                        String selection = DbInfo.MATRIC_NO + "=? AND " + DbInfo.LEVEL + "=? AND " + DbInfo.SEMESTER + "=?";
//                        String[] selectionArgs = {matric, level, "First Semester"};
//                        Cursor cursor = contentResolver.query(GpmContentProvider.CONTENT_URI_GRADES, projection, selection, selectionArgs, null);
//                        simpleCursorAdapter.changeCursor(cursor);
//                        simpleCursorAdapter.notifyDataSetChanged();
//                        break;
//
//
//                }
//
//
//            }
//        });
//        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.show();
//
//    }
//
//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//
//        Toast.makeText(getActivity(), "Press and Hold to Edit/Delete Grade.", Toast.LENGTH_SHORT).show();
//    }
}
