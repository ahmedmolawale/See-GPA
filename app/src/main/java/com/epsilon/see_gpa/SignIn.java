package com.epsilon.see_gpa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.database.Cursor;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

import model.DatabaseController;
import model.DbInfo;

/**
 * A login screen that offers login via email/password.
 *
 *
 *
 */

public class SignIn extends AppCompatActivity{

    private Button sign_in;
    private Button forgotPassword;
    private EditText username;
    private EditText password;
    public static final String INTENT_OF_LOGIN_WINDOW = "datafromloginwindow";
    private View focusView;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private CheckBox keepMeLoggedIn;
    private DatabaseController databaseController;



    //remove later
    Button viewDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        //load the excel file
        databaseController = new DatabaseController(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        sign_in = (Button) findViewById(R.id.sign_in_button);
        forgotPassword = (Button) findViewById(R.id.forgot_password);




        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        viewDB = (Button)findViewById(R.id.view_db);
       // viewDB.setVisibility(View.GONE);
        viewDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dbmanager = new Intent(getApplicationContext(),AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });
    }


    private void attemptLogin() {

        // Reset errors.
        username.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        String username = this.username.getText().toString().trim();
        String password = this.password.getText().toString();

        boolean cancel = false;
        focusView = null;
        if(TextUtils.isEmpty(username)){

            this.username.setError(getString(R.string.error_field_required));
            focusView = this.username;
            cancel = true;
        }

        if (TextUtils.isDigitsOnly(username) && !TextUtils.isEmpty(username)) {
            int matricNo = Integer.parseInt(username);
            if (!(matricNo > 146000 && matricNo < 200000)) {
                this.username.setError(getString(R.string.error_invalid_matric));
                focusView = this.username;
                cancel = true;
            }

        }
        if (!TextUtils.isDigitsOnly(username) && !Utility.validateEmail(username)) {
            //validate the staff mail
            this.username.setError(getString(R.string.error_invalid_email));
            focusView = this.username;
            cancel = true;
        }
        //check for empty password
        if (TextUtils.isEmpty(password)) {
            this.password.setError(getString(R.string.error_field_required));
            focusView = this.password;
            cancel = true;
        }
        //de-activating things

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            this.password.setError(getString(R.string.error_invalid_password));
            focusView = this.password;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            checkForTheUser(username, password);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6 ? true : false;
    }

//    private void takeUserMatric() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Matric No.");
//        builder.setMessage("Please provide your matric no. We shall send your password to your mail.");
//        final AlertDialog dialog = builder.create();
//        final EditText input = new EditText(this);
//
//        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                }
//            }
//        });
//        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//        input.setHint("Matric No.");
//        builder.setView(input);
//        // Set up the buttons
//        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //check whether user exit
//
//                String[] projection = {DbInfo.PASSWORD, DbInfo.EMAIL};
//                String selection = DbInfo.MATRIC_NO + " =?";
//                String[] selectionArgs = {input.getText().toString()};
//                Cursor cursor
//                        = getContentResolver().query(GpmContentProvider.CONTENT_URI_USERS, projection, selection, selectionArgs, null);
//                if (cursor != null && cursor.moveToFirst()) {
//
//                    passwordRetrievalConfirmation(input.getText().toString(),
//                            cursor.getString(cursor.getColumnIndex(DbInfo.PASSWORD)),
//                            cursor.getString(cursor.getColumnIndex(DbInfo.EMAIL)));
//                } else {
//                    displayAMessage("User Not Found", "User with Matric No.: " + input.getText().toString() + " not found.\nYou can create an account.");
//                }
//
//            }
//
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.show();
//
//    }

//    private void passwordRetrievalConfirmation(final String matricNo, final String password, final String email) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Your password would be sent to your mail\n" + email + "\nNote: An active internet connection is required.");
//        builder.setTitle("Password Retrieval");
//        builder.setCancelable(true);
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo netInfo = conn.getActiveNetworkInfo();
//                if (netInfo != null && netInfo.isConnected()) {
//
//
//                    new SendPassword().execute(new String[]{matricNo, password, email});
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Not Connected to the internet", Toast.LENGTH_LONG)
//                            .show();
//                }
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//        builder.show();
//
//    }

//    private class SendPassword extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//            Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_SHORT).show();
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            try {
//                GMailSender sender = new GMailSender("gpamanager@gmail.com", "whatismypassword");
//                String message = "Hi, trust you are good. You got this mail because you requested it from your GP Tracker account." +
//                        "\nPlease see your log-in details below:\n" +
//                        " Matric:   " + params[0] + "\nPassword:    " + params[1] + "\nThank you for using GP Manager.";
//                String subject = "GP Manager Account Password";
//                sender.sendMail(subject, message, "gpamanager@gmail.com", params[2]);
//                return "Success";
//
//            } catch (Exception e) {
//
//                return "Failure";
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            if (result.equals("Success")) {
//                Toast.makeText(getApplicationContext(), "Password sent successfully.", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getApplicationContext(), "An error occurred while sending password.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private void checkForTheUser(String username, String password1) {

        Cursor cursor = databaseController.getAUser(username, password1);
        if (cursor != null && cursor.moveToFirst()) {


             String userDetails[] = {username, cursor.getString(cursor.getColumnIndex(DbInfo.FIRST_NAME))
                    ,cursor.getString(cursor.getColumnIndex(DbInfo.LAST_NAME))};
            Intent intent = new Intent(SignIn.this, MainScreen.class);
                intent.putExtra(INTENT_OF_LOGIN_WINDOW, userDetails);
                startActivity(intent);
               Toast.makeText(getApplicationContext(), "Welcome, " + userDetails[1] +" "+ userDetails[2], Toast.LENGTH_LONG).show();


//            String currentCGPA = sharedPreferences.getString("CURRENTCGPA","7");
//               // Toast.makeText(getApplicationContext(),"Data: "+ userDetails[0],Toast.LENGTH_LONG).show();
//            ComputeStudentsCGPA computeStudentsCGPA = new ComputeStudentsCGPA(currentCGPA,userDetails);
//
//            computeStudentsCGPA.execute((Void) null);

        } else {

            displayAMessage("Error", "Username and Password does not match.");
        }
    }

//    public class ComputeStudentsCGPA extends AsyncTask<Void, Void, Boolean> {
//        private String gpSystem;
//        private String userDetails[];
//
//        public ComputeStudentsCGPA(String _gpSystem,String[] _userDetails) {
//            gpSystem = _gpSystem;
//            userDetails = _userDetails;
//        }
//
//        public boolean computeCGPA() {
//            //this helps to calculate the students cgpa
//            DatabaseController databaseController = new DatabaseController(SignIn.this);
//            Cursor cursor = databaseController.getAllStudents();
//
//            if (cursor != null && cursor.moveToFirst()) {
//                for (int i = 0; i < cursor.getCount(); i++) {
//                    Student student = new Student();
//                    cursor.moveToPosition(i);
//                    student.setMatricNo(cursor.getString(cursor.getColumnIndex(DbInfo.MATRIC_NO)));
//                    //looking through grade for courses taken by the current student
//                    ArrayList<Course> courses = new ArrayList<>();
//                    Cursor cursor2 = databaseController.getStudentGrades(student.getMatricNo());
//                    if (cursor2 != null && cursor2.moveToFirst()) {
//                        for (int k = 0; k < cursor2.getCount(); k++) {
//                            Course course = new Course();
//                            cursor2.moveToPosition(k);
//                            course.setScore(cursor2.getString(cursor2.getColumnIndex(DbInfo.TOTAL)));
//                            course.setCourseCode(cursor2.getString(cursor2.getColumnIndex(DbInfo.COURSE_CODE)));
//                            //checking the course detail from the course table
//                            Log.d("Data", "Course code is "+ course.getCourseCode());
//                            Cursor cursor3 = databaseController.getCourseDetails(course.getCourseCode());
//                            if (cursor3 != null && cursor3.moveToFirst()) {
//                                cursor3.moveToFirst();
//                                course.setUnit(cursor3.getString(cursor3.getColumnIndex(DbInfo.UNIT)));
//                                course.computeGP(gpSystem);
//                            } else {
//                                Log.d("Data", "Course not added!");
//                                Toast toast = Toast.makeText(getApplicationContext(), "Fuck, course not added", Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//                            }
//                            cursor3.close();
//                            courses.add(course); //the list of courses taken by student
//                        }
//                    }
//                    student.setCoursesTaken(courses);
//                    student.calculateCGPAAndSaveToDB(SignIn.this, gpSystem);
//                }
//            }
//            return true;
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//
//            scrollView.setVisibility(View.GONE);
//            showProgress(true);
//
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            return computeCGPA();
//        }
//
//
//        @Override
//        protected void onPostExecute(final Boolean result) {
//            if(result){
//                showProgress(false);
//                Intent intent = new Intent(SignIn.this, MainScreen.class);
//                intent.putExtra(INTENT_OF_LOGIN_WINDOW, userDetails);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "Welcome, " + userDetails[1] +" "+ userDetails[2], Toast.LENGTH_LONG).show();
//            }else{
//                Toast.makeText(getApplicationContext(),"An error occured",Toast.LENGTH_LONG).show();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            showProgress(false);
//        }
//
//    }
//
//    /**
//     * //     * Shows the progress UI and hides the login form.
//     * //
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
////            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
////            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
////                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
////                @Override
////                public void onAnimationEnd(Animator animation) {
////                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
////                }
////            });
//
//            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressBar.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
//            // mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }


    public void displayAMessage(String type, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(type);
        builder.setCancelable(true);
        builder.setPositiveButton("Try Again...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
        focusView = username;
        focusView.requestFocus();
    }
}
//public class SignIn extends AppCompatActivity implements LoaderCallbacks<Cursor> {

//    /**
//     * Id to identity READ_CONTACTS permission request.
//     */
//    private static final int REQUEST_READ_CONTACTS = 0;
//
//    /**
//     * A dummy authentication store containing known user names and passwords.
//     * TODO: remove after connecting to a real authentication system.
//     */
//    private static final String[] DUMMY_CREDENTIALS = new String[]{
//            "foo@example.com:hello", "bar@example.com:world"
//    };
//    /**
//     * Keep track of the login task to ensure we can cancel it if requested.
//     */
//    private ComputeStudentsCGPA mAuthTask = null;
//
//    // UI references.
//    private AutoCompleteTextView mEmailView;
//    private EditText mPasswordView;
//    private View mProgressView;
//    private View mLoginFormView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sign_in);
//        // Set up the login form.
//        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
//        populateAutoComplete();
//
//        mPasswordView = (EditText) findViewById(R.id.password);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
//        mEmailSignInButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                attemptLogin();
//            }
//        });
//
//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
//    }
//
//    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//
//        getLoaderManager().initLoader(0, null, this);
//    }
//
//    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }
//
//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                populateAutoComplete();
//            }
//        }
//    }
//
//
//    /**
//     * Attempts to sign in or register the account specified by the login form.
//     * If there are form errors (invalid email, missing fields, etc.), the
//     * errors are presented and no actual login attempt is made.
//     */
//    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }
//
//        // Reset errors.
//        mEmailView.setError(null);
//        mPasswordView.setError(null);
//
//        // Store values at the time of the login attempt.
//        String email = mEmailView.getText().toString();
//        String password = mPasswordView.getText().toString();
//
//        boolean cancel = false;
//        View focusView = null;
//
//        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }
//
//        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
//        } else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new ComputeStudentsCGPA(email, password);
//            mAuthTask.execute((Void) null);
//        }
//    }
//    private boolean isEmailValid(String email) {
//        //TODO: Replace this with your own logic
//        return email.contains("@");
//    }
//
//    private boolean isPasswordValid(String password) {
//        //TODO: Replace this with your own logic
//        return password.length() > 4;
//    }
//
//    /**
//     * Shows the progress UI and hides the login form.
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return new CursorLoader(this,
//                // Retrieve data rows for the device user's 'profile' contact.
//                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
//                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
//
//                // Select only email addresses.
//                ContactsContract.Contacts.Data.MIMETYPE +
//                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
//                                                                     .CONTENT_ITEM_TYPE},
//
//                // Show primary email addresses first. Note that there won't be
//                // a primary email address if the user hasn't specified one.
//                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
//        List<String> emails = new ArrayList<>();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            emails.add(cursor.getString(ProfileQuery.ADDRESS));
//            cursor.moveToNext();
//        }
//
//        addEmailsToAutoComplete(emails);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//
//    }
//
//    private interface ProfileQuery {
//        String[] PROJECTION = {
//                ContactsContract.CommonDataKinds.Email.ADDRESS,
//                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
//        };
//
//        int ADDRESS = 0;
//        int IS_PRIMARY = 1;
//    }
//
//
//    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(SignIn.this,
//                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
//
//        mEmailView.setAdapter(adapter);
//    }
//
//    /**
//     * Represents an asynchronous login/registration task used to authenticate
//     * the user.
//     */
//    public class ComputeStudentsCGPA extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mEmail;
//        private final String mPassword;
//
//        ComputeStudentsCGPA(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
//}

