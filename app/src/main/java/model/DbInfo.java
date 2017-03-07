package model;

/**
 * Created by MOlawale on 6/11/2015.
 */
public class DbInfo {



    public static final String DATABASE_NAME = "seegpa";
    public static final int DATABASE_VERSION = 1;




    //the user table
    public static final String USERNAME ="username";
    public static final String FIRST_NAME ="firstname";
    public static final String LAST_NAME ="lastname";
    public static final String PASSWORD ="password";



    //for the course table
    public static final String COURSE_CODE = "course_code";
    public static final String COURSE_TITLE = "course_title";
    public static final String UNIT = "unit";
    public static final String STATUS = "status";




    //for a student
    public static final String MATRIC_NO = "matric_no";
    public static final String NAME= "name";
    public static final String YEAR_OF_ENTRY = "year_of_entry";
    public static final String MODE_OF_ENTRY= "mode_of_entry";
    public static final String CGPA = "cgpa";


    //for the grade table
    //of course, we have matric no
    public static final String CA = "ca";
    public static final String EXAM = "exam";
    public static final String TOTAL = "total";
    public static final String SESSION = "session";


    //tables
    public static final String USER = "user";
    public static final String COURSE = "course";
    public static final String STUDENT = "student";
    public static final String GRADE = "grade";



    //strings fro creating tables
    public static final String CREATE_USER =
            "CREATE VIRTUAL TABLE " + USER +
                    " USING fts3 (" +
                    USERNAME + " TEXT, " +
                    FIRST_NAME + " TEXT, " +
                    LAST_NAME + " TEXT, " +
                    PASSWORD + " TEXT)";

    public static final String CREATE_COURSE =
            "CREATE VIRTUAL TABLE "  +COURSE+
                    " USING fts3 (" +
                    COURSE_CODE + " TEXT, " +
                    COURSE_TITLE + " TEXT, " +
                    UNIT + " TEXT, " +
                    STATUS + " TEXT)";


    public static final String CREATE_STUDENT =
            "CREATE VIRTUAL TABLE " + STUDENT +
                    " USING fts3 (" +
                    MATRIC_NO + " TEXT, " +
                    NAME + " TEXT, " +
                    YEAR_OF_ENTRY + " TEXT, " +
                    MODE_OF_ENTRY + " TEXT, " +
                    CGPA + " TEXT)";

    public static final String CREATE_GRADE =
            "CREATE VIRTUAL TABLE "  +GRADE+
                    " USING fts3 (" +
                    MATRIC_NO + " TEXT, " +
                    COURSE_CODE + " TEXT, " +
                    SESSION + " TEXT, " +
                    CA + " TEXT, " +
                    EXAM + " TEXT, " +
                    TOTAL + " TEXT )";






    //tables....delete later
    public static final String STUDENT_ONE = "student_one";
    public static final String STUDENT_TWO = "student_two";
    public static final String STUDENT_THREE = "student_three";
    public static final String STUDENT_FOUR = "student_four";
    public static final String CSC_LEVEL_ONE = "csc_level_one";
    public static final String CSC_LEVEL_TWO = "csc_level_two";
    public static final String CSC_LEVEL_THREE = "csc_level_three";
    public static final String CSC_LEVEL_FOUR = "csc_level_four";




    public static final String CREATE_STUDENT_ONE =
            "CREATE VIRTUAL TABLE " + STUDENT_ONE +
                    " USING fts3 (" +
                    MATRIC_NO + " TEXT, " +
                    NAME + " TEXT, " +
                    CGPA + " TEXT)";

    public static final String CREATE_STUDENT_TWO =
            "CREATE VIRTUAL TABLE " + STUDENT_TWO +
                    " USING fts3 (" +
                    MATRIC_NO + " TEXT, " +
                    NAME + " TEXT, " +
                    CGPA + " TEXT)";
    public static final String CREATE_STUDENT_THREE =
            "CREATE VIRTUAL TABLE " + STUDENT_THREE +
                    " USING fts3 (" +
                    MATRIC_NO + " TEXT, " +
                    NAME + " TEXT, " +
                    CGPA + " TEXT)";
    public static final String CREATE_STUDENT_FOUR =
            "CREATE VIRTUAL TABLE " + STUDENT_FOUR +
                    " USING fts3 (" +
                    MATRIC_NO + " TEXT, " +
                    NAME + " TEXT, " +
                    CGPA + " TEXT)";




    public static final String CREATE_CSC_LEVEL_ONE =
            "CREATE VIRTUAL TABLE "  +CSC_LEVEL_ONE+
                    " USING fts3 (" +
                    MATRIC_NO + " TEXT, " +
                    COURSE_CODE + " TEXT, " +
                    CA + " TEXT, " +
                    EXAM + " TEXT, " +
                    TOTAL + " TEXT )";

    public static final String CREATE_CSC_LEVEL_TWO =
            "CREATE VIRTUAL TABLE "  +CSC_LEVEL_TWO+
                    " USING fts3 (" +
                    MATRIC_NO + " TEXT, " +
                    COURSE_CODE + " TEXT, " +
                    CA + " TEXT, " +
                    EXAM + " TEXT, " +
                    TOTAL + " TEXT )";

    public static final String CREATE_CSC_LEVEL_THREE =
            "CREATE VIRTUAL TABLE "  +CSC_LEVEL_THREE+
                    " USING fts3 (" +
                    MATRIC_NO + " TEXT, " +
                    COURSE_CODE + " TEXT, " +
                    CA + " TEXT, " +
                    EXAM + " TEXT, " +
                    TOTAL + " TEXT )";
    public static final String CREATE_CSC_LEVEL_FOUR =
            "CREATE VIRTUAL TABLE "  +CSC_LEVEL_FOUR+
                    " USING fts3 (" +
                    MATRIC_NO + " TEXT, " +
                    COURSE_CODE + " TEXT, " +
                    CA + " TEXT, " +
                    EXAM + " TEXT, " +
                    TOTAL + " TEXT )";




    //For the user's table




}