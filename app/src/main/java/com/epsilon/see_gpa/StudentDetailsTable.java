package com.epsilon.see_gpa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import model.DatabaseController;
import model.DbInfo;

public class StudentDetailsTable extends AppCompatActivity {

    private Student student;
    SharedPreferences sharedPreferences;
    private ProgressBar mProgressView;
    private final String SN = "S/N";
    private final String COURSE_CODE = "Course Code";
    private final String STATUS = "Status";
    private final String UNIT = "Unit";
    private final String SCORE = "Score";
    private final String GRADE_POINT = "GP";
    private final String SESSION = "SESSION";
    private      String gpSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_student_details);
        setSupportActionBar(toolbar);


        mProgressView = (ProgressBar) findViewById(R.id.loading_details);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

         gpSystem = sharedPreferences.getString(MainScreen.CURRENT_CGPA, "7");
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(StudentDetailsList.FROMSTUDENTLIST)) {


            student = (Student) intent.getSerializableExtra(StudentDetailsList.FROMSTUDENTLIST);
            StudentDetail studentDetail = new StudentDetail(student, gpSystem);
            studentDetail.execute((Void) null);
        }
        getSupportActionBar().setTitle(student.getMatricNo());


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void assistOnCreate(ArrayList<Session> sessions) {
        //ScrollView scrollView = (ScrollView) findViewById(R.id.contents_student_details);

        LinearLayout layout = (LinearLayout) findViewById(R.id.student_details_layout);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sessions.size(); i++) {
            TextView header = new TextView(StudentDetailsTable.this);
            header.setText("Matric No: "+ student.getMatricNo());
            header.setTextSize(22);
            header.setPadding(5, 5, 5, 5);
            header.setTextColor(this.getResources().getColor(R.color.black));
            layout.addView(header);

            //for the table layout now
            TableLayout tableLayout = constructTable(this, sessions.get(i));
            layout.addView(tableLayout);
            TextView analysis = new TextView(StudentDetailsTable.this);
            stringBuilder.append("Units Reg. till date: "+ sessions.get(i).getTotalUnitReg()+"\n");
            stringBuilder.append("Units Passed. till date: "+ sessions.get(i).getTotalUnitPassed()+"\n");
            if(gpSystem.equals("7"))
            stringBuilder.append("Comm. Grade Point Ave.: "+ String.format("%.1f",sessions.get(i).getGpA())+"\n");
            else
                stringBuilder.append("Comm. Grade Point Ave.: "+ String.format("%.2f",sessions.get(i).getGpA())+"\n");
            analysis.setText(stringBuilder.toString());
            analysis.setTextSize(20);
            analysis.setTextColor(this.getResources().getColor(R.color.black));
            layout.addView(analysis);

//            LinearLayout linearLayout1 = new LinearLayout(StudentDetailsTable.this);
//            linearLayout1.setLayoutParams(new
//                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
//            TextView registered = new TextView(StudentDetailsTable.this);
//            registered.setTextSize(17);
//            registered.setTextColor(StudentDetailsTable.this.getResources().getColor(R.color.blue));
//            registered.setText("Units Reg. Till Date:  ");
//            TextView reg = new TextView(StudentDetailsTable.this);
//            reg.setTextSize(17);
//            reg.setTextColor(this.getResources().getColor(R.color.black));
//            reg.setText(sessions.get(i).getTotalUnitReg());
//            linearLayout1.addView(registered);
//            linearLayout1.addView(reg);
//
//            LinearLayout linearLayout2 = new LinearLayout(StudentDetailsTable.this);
//            linearLayout2.setLayoutParams(new
//                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//            linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
//            TextView passed = new TextView(StudentDetailsTable.this);
//            passed.setTextSize(17);
//            passed.setTextColor(StudentDetailsTable.this.getResources().getColor(R.color.blue));
//            passed.setText("Units Passed Till Date:  ");
//            TextView pass = new TextView(StudentDetailsTable.this);
//            pass.setTextSize(17);
//            pass.setTextColor(StudentDetailsTable.this.getResources().getColor(R.color.black));
//            pass.setText(sessions.get(i).getTotalUnitPassed());
//            linearLayout2.addView(passed);
//            linearLayout2.addView(pass);
//
//            LinearLayout linearLayout3 = new LinearLayout(StudentDetailsTable.this);
//            linearLayout3.setLayoutParams(new
//                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//            linearLayout3.setOrientation(LinearLayout.HORIZONTAL);
//            TextView cgpaText = new TextView(StudentDetailsTable.this);
//            cgpaText.setTextSize(17);
//            cgpaText.setTextColor(StudentDetailsTable.this.getResources().getColor(R.color.blue));
//            cgpaText.setText("CGPA Till Date:  ");
//            TextView cgpa = new TextView(StudentDetailsTable.this);
//            cgpa.setTextSize(17);
//            cgpa.setTextColor(StudentDetailsTable.this.getResources().getColor(R.color.black));
//            cgpa.setText(String.format("%.1f",sessions.get(i).getGpA()));
//            linearLayout3.addView(cgpaText);
//            linearLayout3.addView(cgpa);
//
//            layout.addView(linearLayout1);
//            //layout.addView(getLayoutInflater().inflate());
//            layout.addView(linearLayout2);
//            layout.addView(linearLayout3);
        }
    }

    public TableLayout constructTable(Context context, Session session) {

        //setting up the table layout
        TableLayout tableLayout = new TableLayout(context);
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT
                , TableLayout.LayoutParams.WRAP_CONTENT);
        tableLayout.setLayoutParams(tableLayoutParams);
        tableLayout.setPadding(10, 10, 10, 10);
        //tableLayout.setShrinkAllColumns(true);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setHorizontalScrollBarEnabled(true);


        //for the header of the table; note that its just a row
        TableRow header = new TableRow(context);
        header.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        //adding the headers
        TextView column = new TextView(context);
        column.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column.setBackgroundResource(R.drawable.cell_shape_for_header);
        column.setPadding(3, 3, 3, 3);
        column.setText(SN);
        column.setTextColor(context.getResources().getColor(R.color.black));
        header.addView(column);


        TextView column0 = new TextView(context);
        column0.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column0.setBackgroundResource(R.drawable.cell_shape_for_header);
        column0.setPadding(3, 3, 3, 3);
        column0.setText(COURSE_CODE);
        column0.setTextColor(context.getResources().getColor(R.color.black));
        //column1.setTextColor(context.getResources().getColor(R.color.black));
        header.addView(column0);


        TextView column1 = new TextView(context);
        column1.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column1.setBackgroundResource(R.drawable.cell_shape_for_header);
        column1.setPadding(3, 3, 3, 3);
        column1.setText(STATUS);
        column1.setTextColor(context.getResources().getColor(R.color.black));
        //column1.setTextColor(context.getResources().getColor(R.color.black));
        header.addView(column1);

        TextView column2 = new TextView(context);
        column2.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column2.setBackgroundResource(R.drawable.cell_shape_for_header);
        column2.setPadding(3, 3, 3, 3);
        column2.setText(UNIT);
        column2.setTextColor(context.getResources().getColor(R.color.black));
        //  column2.setTextColor(context.getResources().getColor(R.color.black));
        header.addView(column2);

        //third column
        TextView column3 = new TextView(context);
        column3.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column3.setBackgroundResource(R.drawable.cell_shape_for_header);
        column3.setPadding(3, 3, 3, 3);
        column3.setText(SCORE);
        column3.setTextColor(context.getResources().getColor(R.color.black));
        header.addView(column3);

        //fourth column
        TextView column4 = new TextView(context);
        column4.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column4.setBackgroundResource(R.drawable.cell_shape_for_header);
        column4.setPadding(3, 3, 3, 3);
        column4.setText(GRADE_POINT);
        column4.setTextColor(context.getResources().getColor(R.color.black));

        header.addView(column4);
        //fourth column
        TextView column5 = new TextView(context);
        column5.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column5.setBackgroundResource(R.drawable.cell_shape_for_header);
        column5.setPadding(3, 3, 3, 3);
        column5.setText(SESSION);
        column5.setTextColor(context.getResources().getColor(R.color.black));

        header.addView(column5);
        tableLayout.addView(header);


        for (int j = 0; j < session.getCoursesTaken().size(); j++) {
            Course course = session.getCoursesTaken().get(j);

            //adding the other rows
            tableLayout.addView(addRowData(context, Integer.toString(j + 1), course.getCourseCode(), course.getStatus(),
                    course.getUnit(), course.getScore(), course.getGradePoint(),course.getSession()));
        }

        return tableLayout;
    }

    private TableRow addRowData(Context context, String sn, String courseCode, String status, String unit, String score, String gp,String session) {

        TableRow row = new TableRow(context);
        row.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


        int backgroundResource = R.drawable.cell_shape_pass;

        //adding the data
        TextView column = new TextView(context);
        column.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column.setBackgroundResource(backgroundResource);
        column.setPadding(3, 3, 3, 3);
        column.setText(sn);
        //column1.setTextColor(context.getResources().getColor(R.color.black));

        row.addView(column);

        TextView column0 = new TextView(context);
        column0.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column0.setBackgroundResource(backgroundResource);
        column0.setPadding(3, 3, 3, 3);
        column0.setText(courseCode);
        //column1.setTextColor(context.getResources().getColor(R.color.black));
        row.addView(column0);


        TextView column1 = new TextView(context);
        column1.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column1.setBackgroundResource(backgroundResource);
        column1.setPadding(3, 3, 3, 3);
        column1.setText(status);
        //column1.setTextColor(context.getResources().getColor(R.color.black));

        row.addView(column1);

        TextView column2 = new TextView(context);
        column2.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column2.setBackgroundResource(backgroundResource);
        column2.setPadding(3, 3, 3, 3);
        column2.setText(unit);
        //column2.setTextColor(context.getResources().getColor(R.color.black));
        row.addView(column2);

        //third column
        TextView column3 = new TextView(context);
        column3.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column3.setBackgroundResource(backgroundResource);
        column3.setPadding(3, 3, 3, 3);
        column3.setText(score);
        //column3.setTextColor(context.getResources().getColor(R.color.black));

        row.addView(column3);

        //fourth column
        TextView column4 = new TextView(context);
        column3.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column4.setBackgroundResource(backgroundResource);
        column4.setPadding(3, 3, 3, 3);
        column4.setText(gp);
//        column4.setTextColor(context.getResources().getColor(R.color.black));
        row.addView(column4);
        //fifth column
        TextView column5 = new TextView(context);
        column3.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column5.setBackgroundResource(backgroundResource);
        column5.setPadding(3, 3, 3, 3);
        column5.setText(session);
        row.addView(column5);
        return row;
    }


    private class StudentDetail extends AsyncTask<Void, Void, ArrayList<Session>> {

        private final Student student;
        private final String gpSystem;


        public StudentDetail(Student _student, String _gpSystem) {

            student = _student;
            gpSystem = _gpSystem;
        }

        private ArrayList<Session> getStudentCourses() {
            DatabaseController databaseController = new DatabaseController(StudentDetailsTable.this);

            Cursor cursor = databaseController.getStudentGrades(student.getMatricNo());//shpuld get the grades and course detail
            //100 level student
            ArrayList<Session> sessions = new ArrayList<>();
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
                Session session = new Session();
                session.setCoursesTaken(courses);
                session.computeUnits();
                session.computeSessionGP(gpSystem);
                sessions.add(session);


            } else {
                Toast.makeText(getApplicationContext(), "Cursor is empty", Toast.LENGTH_LONG).show();
            }
            return sessions;
        }
        private void getStudentResults() {


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected ArrayList<Session> doInBackground(Void... params) {
            ArrayList<Session> sessions;
            sessions = getStudentCourses();
            return sessions;
        }

        @Override
        protected void onPostExecute(ArrayList<Session> sessions) {
            showProgress(false);
            if (sessions.size() > 0) {
                //set the table data and the necessary view
                assistOnCreate(sessions);
                //Toast.makeText(getApplicationContext(), "Session has " + sessions.size(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Session empty", Toast.LENGTH_LONG).show();

                //have a text view that says student details not available
            }
        }

    }

    /**
     * //     * Shows the progress UI and hides the login form.
     * //
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            // mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
//go through the below much later
//if (student.getYearOfEntry().equals(MainScreen.CURRENT_SESSION)
//        && student.getModeOfEntry().equals(MainScreen.MODE_OF_ENTRY_UTME)) {
//        for (int i = 0; i < cursor.getCount(); i++) {
//        cursor.moveToPosition(i);
//        Course course = new Course();
//        course.setCourseCode(cursor.getString(cursor.getColumnIndex(DbInfo.COURSE_CODE)));
//        course.setScore(cursor.getString(cursor.getColumnIndex(DbInfo.TOTAL)));
//        Cursor cursor2 = databaseController.getCourseDetails(course.getCourseCode());
//        course.setStatus(cursor2.getString(cursor2.getColumnIndex(DbInfo.STATUS)));
//        course.setUnit(cursor2.getString(cursor2.getColumnIndex(DbInfo.UNIT)));
//        course.computeGP(gpSystem);
//        courses.add(course);
//        }
//        Session session = new Session();
//        session.setCoursesTaken(courses);
//        session.computeUnits();
//        session.computeSessionGP(gpSystem);
//        session.setLevel("100L - 2015/16");
//        sessions.add(session);
//
//        } else if (student.getYearOfEntry().equals(MainScreen.CURRENT_SESSION)
//        && (student.getModeOfEntry().equals(MainScreen.MODE_OF_ENTRY_DE) || student.getModeOfEntry().equals(MainScreen.MODE_OF_ENTRY_TRA))) {
//        //no 100l for this guys
//        for (int i = 0; i < cursor.getCount(); i++) {
//        cursor.moveToPosition(i);
//        Course course = new Course();
//        course.setCourseCode(cursor.getString(cursor.getColumnIndex(DbInfo.COURSE_CODE)));
//        course.setScore(cursor.getString(cursor.getColumnIndex(DbInfo.TOTAL)));
//        Cursor cursor2 = databaseController.getCourseDetails(course.getCourseCode());
//        course.setStatus(cursor2.getString(cursor2.getColumnIndex(DbInfo.STATUS)));
//        course.setUnit(cursor2.getString(cursor2.getColumnIndex(DbInfo.UNIT)));
//        course.computeGP(gpSystem);
//        courses.add(course);
//        }
//        Session session = new Session();
//        session.setCoursesTaken(courses);
//        session.computeUnits();
//        session.computeSessionGP(gpSystem);
//        session.setLevel("200L - 2015/16");
//        sessions.add(session);
//
//        } else if (student.getYearOfEntry().equals("2014/15")
//        && (student.getModeOfEntry().equals(MainScreen.MODE_OF_ENTRY_UTME))) {
//        //100l and 200l for this guys
//        for (int i = 0; i < cursor.getCount(); i++) {
//        cursor.moveToPosition(i);
//        if(cursor.getString(cursor.getColumnIndex(DbInfo.SESSION))=="2014/15"){
//        Course course = new Course();
//        course.setCourseCode(cursor.getString(cursor.getColumnIndex(DbInfo.COURSE_CODE)));
//        course.setScore(cursor.getString(cursor.getColumnIndex(DbInfo.TOTAL)));
//        Cursor cursor2 = databaseController.getCourseDetails(course.getCourseCode());
//        course.setStatus(cursor2.getString(cursor2.getColumnIndex(DbInfo.STATUS)));
//        course.setUnit(cursor2.getString(cursor2.getColumnIndex(DbInfo.UNIT)));
//        course.computeGP(gpSystem);
//        courses.add(course);
//
//
//        }
//        Course course = new Course();
//        course.setCourseCode(cursor.getString(cursor.getColumnIndex(DbInfo.COURSE_CODE)));
//        course.setScore(cursor.getString(cursor.getColumnIndex(DbInfo.TOTAL)));
//        Cursor cursor2 = databaseController.getCourseDetails(course.getCourseCode());
//        course.setStatus(cursor2.getString(cursor2.getColumnIndex(DbInfo.STATUS)));
//        course.setUnit(cursor2.getString(cursor2.getColumnIndex(DbInfo.UNIT)));
//        course.computeGP(gpSystem);
//        courses.add(course);
//        }
//        Session session = new Session();
//        session.setCoursesTaken(courses);
//        session.computeUnits();
//        session.computeSessionGP(gpSystem);
//        session.setLevel("200L - 2015/16");
//        sessions.add(session);
//
//
//
//        }