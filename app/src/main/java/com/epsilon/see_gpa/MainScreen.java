package com.epsilon.see_gpa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import model.DatabaseController;
import model.DbInfo;

public class MainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MAINSCREEN_TO_DETAIL = "mainscreentodetail";
    public static final String MAINSCREEN_TO_CHART = "mainscreentochart";
    public static final String CURRENT_CGPA = "currentcgpa";

    public static final String LEVEL_IN_VIEW = "level_in_view";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar mProgressView;
    SharedPreferences sharedPreferences;
    private String levelInView;
    private NavigationView navigationView;

    private ArrayList<Student> studentOnList = new ArrayList<>();


    //important constant
    public static final String CURRENT_SESSION = "2015/16";
    public static final String MODE_OF_ENTRY_UTME = "UTME";
    public static final String MODE_OF_ENTRY_DE = "DE";
    public static final String MODE_OF_ENTRY_TRA = "TRA";


    private final String[] LEVELS = {"CSC - 100L", "CSC - 200L", "CSC - 300L", "CSC - 400L"};
    private String currentCGPA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chart_analysis);
        setSupportActionBar(toolbar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        getSupportActionBar().setTitle(LEVELS[0]);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mProgressView = (ProgressBar) findViewById(R.id.loading);

        sharedPreferences.edit().putString("level", "100").apply();//student whose year of entry is the current session

        //calculate the student cgpa with an async task

        String level = sharedPreferences.getString("level", "100");
        currentCGPA = sharedPreferences.getString(CURRENT_CGPA, "7");
        if (!sharedPreferences.getString(level + currentCGPA, "No").equals("Yes")) {
            ComputeStudentsCGPA computeStudentsCGPA = new ComputeStudentsCGPA(currentCGPA, level);
            computeStudentsCGPA.execute((Void) null);
        } else {
            displayStudents(level, "name");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start the analysis view chart analysis activity
                Intent intent = new Intent(MainScreen.this, ChartAnalysis.class);
                intent.putExtra(MainScreen.MAINSCREEN_TO_CHART, studentOnList);
                intent.putExtra(MainScreen.LEVEL_IN_VIEW, levelInView);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        MenuItem menuItem;
        if(currentCGPA.equals("7")) {
            menuItem = navigationView.getMenu().findItem(R.id.nav_gp_7);
            menuItem.setEnabled(false);
        }else if(currentCGPA.equals("5")) {
            menuItem = navigationView.getMenu().findItem(R.id.nav_gp_5);
            menuItem.setEnabled(false);
        }else if(currentCGPA.equals("4")) {
            menuItem = navigationView.getMenu().findItem(R.id.nav_gp_4);
            menuItem.setEnabled(false);
        }
        //menuItem.setEnabled(false);
        //view.setBackgroundColor(this.getResources().getColor(R.color.blue));
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_ascending) {

            //adapter.notifyDataSetChanged();
            displayStudents(sharedPreferences.getString("level", "100"), "ASC");

        } else if (id == R.id.action_descending) {

            displayStudents(sharedPreferences.getString("level", "100"), "DESC");
        } else if (id == R.id.action_100_level) {

            getSupportActionBar().setTitle(LEVELS[0]);
            sharedPreferences.edit().putString("level", "100").apply();
            String level = "100";
            currentCGPA = sharedPreferences.getString(CURRENT_CGPA, "7");
            if (!sharedPreferences.getString(level + currentCGPA, "No").equals("Yes")) {
                ComputeStudentsCGPA computeStudentsCGPA = new ComputeStudentsCGPA(currentCGPA, level);
                computeStudentsCGPA.execute((Void) null);
            } else {
                displayStudents(level, "name");
            }

        } else if (id == R.id.action_200_level) {
            getSupportActionBar().setTitle(LEVELS[1]);
            sharedPreferences.edit().putString("level", "200").apply();
            String level = "200";
            currentCGPA = sharedPreferences.getString(CURRENT_CGPA, "7");
            if (!sharedPreferences.getString(level + currentCGPA, "No").equals("Yes")) {
                ComputeStudentsCGPA computeStudentsCGPA = new ComputeStudentsCGPA(currentCGPA, level);
                computeStudentsCGPA.execute((Void) null);
            } else {
                displayStudents(level, "name");
            }
        } else if (id == R.id.action_300_level) {
            getSupportActionBar().setTitle(LEVELS[2]);
            sharedPreferences.edit().putString("level", "300").apply();
            String level = "300";
            currentCGPA = sharedPreferences.getString(CURRENT_CGPA, "7");
            if (!sharedPreferences.getString(level + currentCGPA, "No").equals("Yes")) {
                ComputeStudentsCGPA computeStudentsCGPA = new ComputeStudentsCGPA(currentCGPA, level);
                computeStudentsCGPA.execute((Void) null);
            } else {
                displayStudents(level, "name");
            }

        } else if (id == R.id.action_400_level) {
            getSupportActionBar().setTitle(LEVELS[3]);
            sharedPreferences.edit().putString("level", "400").apply();
            String level = "400";
            currentCGPA = sharedPreferences.getString(CURRENT_CGPA, "7");
            if (!sharedPreferences.getString(level + currentCGPA, "No").equals("Yes")) {
                ComputeStudentsCGPA computeStudentsCGPA = new ComputeStudentsCGPA(currentCGPA, level);
                computeStudentsCGPA.execute((Void) null);
            } else {
                displayStudents(level, "name");
            }
            //displayStudents(sharedPreferences.getString("level","100"),"name");
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_gp_7) {
            //erase all level calculated cgpa from the sharedpreference
            eraseSavedCgpaPreference();
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_gp_7);
            menuItem.setEnabled(false);
            MenuItem menuItem1 = navigationView.getMenu().findItem(R.id.nav_gp_4);
            menuItem1.setEnabled(true);
            MenuItem menuItem2 = navigationView.getMenu().findItem(R.id.nav_gp_5);
            menuItem2.setEnabled(true);
            sharedPreferences.edit().putString(CURRENT_CGPA, "7").apply();
            String level = sharedPreferences.getString("level", "100");
            currentCGPA = sharedPreferences.getString(CURRENT_CGPA, "7");
            ComputeStudentsCGPA computeStudentsCGPA = new ComputeStudentsCGPA(currentCGPA, level);
            computeStudentsCGPA.execute((Void) null);

        } else if (id == R.id.nav_gp_5) {
            eraseSavedCgpaPreference();
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_gp_7);
            menuItem.setEnabled(true);
            MenuItem menuItem1 = navigationView.getMenu().findItem(R.id.nav_gp_4);
            menuItem1.setEnabled(true);
            MenuItem menuItem2 = navigationView.getMenu().findItem(R.id.nav_gp_5);
            menuItem2.setEnabled(false);
            sharedPreferences.edit().putString(CURRENT_CGPA, "5").apply();
            String level = sharedPreferences.getString("level", "100");
            currentCGPA = sharedPreferences.getString(CURRENT_CGPA, "7");
            ComputeStudentsCGPA computeStudentsCGPA = new ComputeStudentsCGPA(currentCGPA, level);
            computeStudentsCGPA.execute((Void) null);

        } else if (id == R.id.nav_gp_4) {
            eraseSavedCgpaPreference();
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_gp_7);
            menuItem.setEnabled(true);
            MenuItem menuItem1 = navigationView.getMenu().findItem(R.id.nav_gp_4);
            menuItem1.setEnabled(false);
            MenuItem menuItem2 = navigationView.getMenu().findItem(R.id.nav_gp_5);
            menuItem2.setEnabled(true);
            sharedPreferences.edit().putString(CURRENT_CGPA, "4").apply();
            String level = sharedPreferences.getString("level", "100");
            currentCGPA = sharedPreferences.getString(CURRENT_CGPA, "7");
            ComputeStudentsCGPA computeStudentsCGPA = new ComputeStudentsCGPA(currentCGPA, level);
            computeStudentsCGPA.execute((Void) null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void eraseSavedCgpaPreference() {

        for(int l = 100;l<=400;l+=100){

            sharedPreferences.edit().putString(l+"4","No").apply();
            sharedPreferences.edit().putString(l+"5","No").apply();
            sharedPreferences.edit().putString(l+"7","No").apply();

        }
    }

    private void displayStudents(String level, String order) {
        DatabaseController databaseController = new DatabaseController(MainScreen.this);
        Cursor cursor = databaseController.getStudentsOfLevel(level, order);

        if (cursor != null && cursor.moveToFirst()) {
            studentOnList.clear();
            for (int j = 0; j < cursor.getCount(); j++) {
                cursor.moveToPosition(j);
                Student student = new Student();
                student.setName(cursor.getString(cursor.getColumnIndex(DbInfo.NAME)));
                student.setMatricNo(cursor.getString(cursor.getColumnIndex(DbInfo.MATRIC_NO)));
                student.setCgpa(cursor.getString(cursor.getColumnIndex(DbInfo.CGPA)));
                student.setYearOfEntry(cursor.getString(cursor.getColumnIndex(DbInfo.YEAR_OF_ENTRY)));
                student.setModeOfEntry(cursor.getString(cursor.getColumnIndex(DbInfo.MODE_OF_ENTRY)));

                studentOnList.add(student);
            }
        }
        if (studentOnList.size() > 0) {
            adapter = new MainScreenRecyclerAdapter(MainScreen.this, studentOnList);
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter);
            //levelInView = level + " Level";

        } else {

            Toast.makeText(getApplicationContext(), "No student returned mehn", Toast.LENGTH_LONG).show();
        }

    }

    public class ComputeStudentsCGPA extends AsyncTask<Void, Void, Boolean> {
        private String gpSystem;
        private String level;

        public ComputeStudentsCGPA(String _gpSystem, String _level) {
            gpSystem = _gpSystem;
            level = _level;
        }

        public boolean computeCGPA() {
            //this helps to calculate the students cgpa for a particular level
            DatabaseController databaseController = new DatabaseController(MainScreen.this);
            Cursor cursor = databaseController.getStudentsOfLevel(level, "name");

            if (cursor != null && cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    Student student = new Student();
                    cursor.moveToPosition(i);
                    student.setMatricNo(cursor.getString(cursor.getColumnIndex(DbInfo.MATRIC_NO)));
                    //looking through grade for courses taken by the current student
                    ArrayList<Course> courses = new ArrayList<>();
                    Cursor cursor2 = databaseController.getStudentGrades(student.getMatricNo());
                    if (cursor2 != null && cursor2.moveToFirst()) {
                        for (int k = 0; k < cursor2.getCount(); k++) {
                            Course course = new Course();
                            cursor2.moveToPosition(k);
                            course.setScore(cursor2.getString(cursor2.getColumnIndex(DbInfo.TOTAL)));
                            course.setCourseCode(cursor2.getString(cursor2.getColumnIndex(DbInfo.COURSE_CODE)));
                            //checking the course detail from the course table
                            Log.d("Data", "Course code is " + course.getCourseCode());
                            Cursor cursor3 = databaseController.getCourseDetails(course.getCourseCode());
                            if (cursor3 != null && cursor3.moveToFirst()) {
                                cursor3.moveToFirst();
                                course.setUnit(cursor3.getString(cursor3.getColumnIndex(DbInfo.UNIT)));
                                course.computeGP(gpSystem);
                            } else {
                                Log.d("Data", "Course not added!");
                                Toast toast = Toast.makeText(getApplicationContext(), "Fuck, course not added", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            cursor3.close();
                            courses.add(course); //the list of courses taken by student
                        }
                    }
                    student.setCoursesTaken(courses);
                    student.calculateCGPAAndSaveToDB(MainScreen.this, gpSystem);
                }
            }
            return true;
        }


        @Override
        protected void onPreExecute() {

            showProgress(true);
            recyclerView.setAdapter(null);
            recyclerView.removeAllViews();
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            return computeCGPA();
        }


        @Override
        protected void onPostExecute(final Boolean result) {
            if (result) {
                showProgress(false);
                displayStudents(level, "name");

            } else {
                Toast.makeText(getApplicationContext(), "An error occured", Toast.LENGTH_LONG).show();
            }
            sharedPreferences.edit().putString(level + gpSystem, "Yes").apply();  //makes lot of sense...saving calculated level and gpsystem
//            if(level.equals("100") && gpSystem.equals("4")){
//            sharedPreferences.edit().putString("Calculated_100_4","Yes").apply();
//            }else if(level.equals("200") && gpSystem.equals("4")){
//                sharedPreferences.edit().putString("Calculated_200_4","Yes").apply();
//            }else if(level.equals("300") && gpSystem.equals("4")){
//                sharedPreferences.edit().putString("Calculated300","Yes").apply();
//            }else if(level.equals("400")){
//                sharedPreferences.edit().putString("Calculated400","Yes").apply();
//            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
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

//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

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

    private void displayMessage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log Out");
        builder.setMessage("Do you really want to log out?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                close();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


    }

    private void close() {

        super.onBackPressed();
    }


}
