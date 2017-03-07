package model;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.widget.Toast;


import com.epsilon.see_gpa.MainScreen;
import com.epsilon.see_gpa.R;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ahmed on 11/12/2016.
 */

public class DatabaseController {

    private final DbHelper dbHelper;
    private static SQLiteDatabase database;
    public DatabaseController(Context context) {
        dbHelper = new DbHelper(context);
    }

    private class DbHelper extends SQLiteOpenHelper {
        Context mHelperContext;
        private final String LOGCAT = null;

        public DbHelper(Context context) {
            super(context, DbInfo.DATABASE_NAME, null, DbInfo.DATABASE_VERSION);
            mHelperContext = context;
            Log.d(LOGCAT, "Created");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            database = db;

            database.execSQL(DbInfo.CREATE_COURSE);
            database.execSQL(DbInfo.CREATE_USER);
            database.execSQL(DbInfo.CREATE_STUDENT);
            database.execSQL(DbInfo.CREATE_GRADE);
            loadExcel();

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(LOGCAT, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DbInfo.COURSE);
            db.execSQL("DROP TABLE IF EXISTS " + DbInfo.USER);
            db.execSQL("DROP TABLE IF EXISTS " + DbInfo.STUDENT);
            db.execSQL("DROP TABLE IF EXISTS " + DbInfo.GRADE);
            onCreate(db);
        }

        private void loadExcel() {
            try {
                final Resources resources = mHelperContext.getResources();

                //for the users
                InputStream inputStream = resources.openRawResource(R.raw.users);
                Workbook workbook = new HSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                if (sheet != null) {
                    insertExcelUserToSqlite(sheet);
                } else {
                    Toast.makeText(mHelperContext, "Empty Sheet", Toast.LENGTH_LONG).show();
                }


                //for the course
                InputStream inputStream1 = resources.openRawResource(R.raw.courses);
                Workbook workbook1 = new HSSFWorkbook(inputStream1);
                Sheet sheet1 = workbook1.getSheetAt(0);
                if (sheet1 != null) {
                    insertExcelCourseToSqlite(sheet1);
                } else {
                    Toast.makeText(mHelperContext, "Empty Sheet", Toast.LENGTH_LONG).show();
                }

                //for the student
                InputStream inputStream2 = resources.openRawResource(R.raw.student);
                Workbook workbook2 = new HSSFWorkbook(inputStream2);
                Sheet sheet2 = workbook2.getSheetAt(0);
                if (sheet2 != null) {
                    insertExcelStudentToSqlite(sheet2);
                } else {
                    Toast.makeText(mHelperContext, "Empty Sheet", Toast.LENGTH_LONG).show();
                }


                // for the grades
                InputStream inputStream3 = resources.openRawResource(R.raw.csc_2012_2013);
                Workbook workbook3 = new HSSFWorkbook(inputStream3);
                for (int i = 0; i < workbook3.getNumberOfSheets(); i++) {
                    Sheet sheet3 = workbook3.getSheetAt(i);
                    if (sheet3 != null) {
                        insertExcelGradeToSqlite(sheet3, sheet3.getSheetName(), "2012/13");
                    } else {
                        Toast.makeText(mHelperContext, "Sheet is empty", Toast.LENGTH_LONG).show();
                    }
                }

                InputStream inputStream4 = resources.openRawResource(R.raw.csc_2013_2014);
                Workbook workbook4 = new HSSFWorkbook(inputStream4);
                for (int i = 0; i < workbook4.getNumberOfSheets(); i++) {
                    Sheet sheet4 = workbook4.getSheetAt(i);
                    if (sheet4 != null) {
                        insertExcelGradeToSqlite(sheet4, sheet4.getSheetName(), "2013/14");
                    } else {
                        Toast.makeText(mHelperContext, "Sheet is empty", Toast.LENGTH_LONG).show();
                    }
                }

                InputStream inputStream5 = resources.openRawResource(R.raw.csc_2014_2015);
                Workbook workbook5 = new HSSFWorkbook(inputStream5);
                for (int i = 0; i < workbook5.getNumberOfSheets(); i++) {
                    Sheet sheet5 = workbook5.getSheetAt(i);
                    if (sheet5 != null) {
                        insertExcelGradeToSqlite(sheet5, sheet5.getSheetName(), "2014/15");
                    } else {
                        Toast.makeText(mHelperContext, "Sheet is empty", Toast.LENGTH_LONG).show();
                    }
                }

                InputStream inputStream6 = resources.openRawResource(R.raw.csc_2015_2016);
                Workbook workbook6 = new HSSFWorkbook(inputStream6);
                for (int i = 0; i < workbook6.getNumberOfSheets(); i++) {
                    Sheet sheet6 = workbook6.getSheetAt(i);
                    if (sheet6 != null) {
                        insertExcelGradeToSqlite(sheet6, sheet6.getSheetName(), "2015/16");
                    } else {
                        Toast.makeText(mHelperContext, "Sheet is empty", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                Log.d("Error", "Error loading excel file");
            }
        }

        private void insertExcelStudentToSqlite(Sheet sheet) {
            for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext(); ) {
                Row row = rowIterator.next();
                row.getCell(0, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(1, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(2, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(3, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                String matricNo = row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
                String name = row.getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
                String year_of_entry = row.getCell(2, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
                String mode_of_entry = row.getCell(3, Row.CREATE_NULL_AS_BLANK).getStringCellValue();

                ContentValues contentValues = new ContentValues();
                contentValues.put(DbInfo.MATRIC_NO, matricNo);
                contentValues.put(DbInfo.NAME, name);
                contentValues.put(DbInfo.YEAR_OF_ENTRY, year_of_entry);
                contentValues.put(DbInfo.MODE_OF_ENTRY, mode_of_entry);

                try {
                    if (database.insert(DbInfo.STUDENT, null, contentValues) < 0)
                        return;
                } catch (Exception ex) {
                    Log.d("Exception in importing", ex.getMessage().toString());
                }
            }
        }


        private void insertExcelGradeToSqlite(Sheet sheet, String courseCode, String session) {

            for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext(); ) {
                Row row = rowIterator.next();
                row.getCell(0, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(1, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(2, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(3, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                String matricNo = row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue();

                if (!matricNo.isEmpty()) {
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(DbInfo.MATRIC_NO, row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(DbInfo.COURSE_CODE, courseCode);
                    contentValues.put(DbInfo.CA, row.getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(DbInfo.EXAM, row.getCell(2, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(DbInfo.TOTAL, row.getCell(3, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(DbInfo.SESSION, session);

                    try {
                        if (database.insert(DbInfo.GRADE, null, contentValues) < 0)
                            return;
                    } catch (Exception ex) {
                        Log.d("Exception in importing", ex.getMessage().toString());
                    }
                }
            }
        }

        private void insertExcelUserToSqlite(Sheet sheet) {
            for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext(); ) {
                Row row = rowIterator.next();
                row.getCell(0, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(1, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(2, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(3, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                String username = row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
                if (username != null) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DbInfo.USERNAME, row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(DbInfo.FIRST_NAME, row.getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(DbInfo.LAST_NAME, row.getCell(2, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(DbInfo.PASSWORD, row.getCell(3, Row.CREATE_NULL_AS_BLANK).getStringCellValue());

                    try {
                        if (database.insert(DbInfo.USER, null, contentValues) < 0)
                            return;
                    } catch (Exception ex) {
                        Log.d("Exception in importing", ex.getMessage().toString());
                    }
                }


            }
        }

        private void insertExcelCourseToSqlite(Sheet sheet) {
            for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext(); ) {
                Row row = rowIterator.next();
                row.getCell(0, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(1, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(2, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(3, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
                String courseCode = row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
                if (courseCode != null) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DbInfo.COURSE_CODE, row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(DbInfo.COURSE_TITLE, row.getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(DbInfo.UNIT, row.getCell(2, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(DbInfo.STATUS, row.getCell(3, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    try {
                        if (database.insert(DbInfo.COURSE, null, contentValues) < 0)
                            return;
                    } catch (Exception ex) {
                        Log.d("Exception in importing", ex.getMessage().toString());
                    }
                }
            }
        }

    }


    public Cursor getAUser(String username, String password) {
        String[] columns = {DbInfo.USERNAME, DbInfo.FIRST_NAME, DbInfo.LAST_NAME};
        String selection = DbInfo.USERNAME + " = ? AND " + DbInfo.PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        return query(DbInfo.USER, selection, selectionArgs, columns);
    }

    public Cursor getCourseDetails(String courseCode) {
        String selection = DbInfo.COURSE_CODE + " = ? ";
        String[] selectionArgs = {courseCode};
        return query(DbInfo.COURSE, selection, selectionArgs, null);
    }

    private String[] buildYearOfEntry(String level) {
        String yearOfEntryForDeAndTra;
        String yearOfEntryForUtme;
        int baseYearForUtme;
        int endYearForUtme;
        int baseYearForDeTra;
        int endYearForDeTra;
        if (level == "200") {
            baseYearForUtme = Integer.parseInt(MainScreen.CURRENT_SESSION.substring(0, 4)) - 1;
            endYearForUtme = Integer.parseInt(MainScreen.CURRENT_SESSION.substring(2, 4));
            yearOfEntryForUtme = baseYearForUtme + "/" + endYearForUtme;
            yearOfEntryForDeAndTra = MainScreen.CURRENT_SESSION;
        } else if (level == "300") {
            baseYearForUtme = Integer.parseInt(MainScreen.CURRENT_SESSION.substring(0, 4)) - 2;
            endYearForUtme = Integer.parseInt(MainScreen.CURRENT_SESSION.substring(2, 4)) - 1;
            yearOfEntryForUtme = baseYearForUtme + "/" + endYearForUtme;
            baseYearForDeTra = Integer.parseInt(MainScreen.CURRENT_SESSION.substring(0, 4)) - 1;
            endYearForDeTra = Integer.parseInt(MainScreen.CURRENT_SESSION.substring(2, 4));
            yearOfEntryForDeAndTra = baseYearForDeTra + "/" + endYearForDeTra;
        } else if (level == "400") {
            baseYearForUtme = Integer.parseInt(MainScreen.CURRENT_SESSION.substring(0, 4)) - 3;
            endYearForUtme = Integer.parseInt(MainScreen.CURRENT_SESSION.substring(2, 4)) - 2;
            yearOfEntryForUtme = baseYearForUtme + "/" + endYearForUtme;
            baseYearForDeTra = Integer.parseInt(MainScreen.CURRENT_SESSION.substring(0, 4)) - 2;
            endYearForDeTra = Integer.parseInt(MainScreen.CURRENT_SESSION.substring(2, 4)) - 1;
            yearOfEntryForDeAndTra = baseYearForDeTra + "/" + endYearForDeTra;
        } else {
            yearOfEntryForDeAndTra = "";
            yearOfEntryForUtme = "";

        }
        String data[] = {yearOfEntryForDeAndTra, yearOfEntryForUtme};
        return data;
    }

    //getting student based on order
    public Cursor getStudentsOfLevel(String level, String order) {
        String[] columns = {DbInfo.MATRIC_NO, DbInfo.NAME, DbInfo.CGPA, DbInfo.YEAR_OF_ENTRY, DbInfo.MODE_OF_ENTRY};


        String orderBy;
        if (order.equals("name")) {
            orderBy = DbInfo.NAME + " ASC";
        } else if (order.equals("DESC")) {
            orderBy = DbInfo.CGPA + " DESC";
        } else {
            orderBy = DbInfo.CGPA + " ASC";
        }

        if (level.equals("100")) {
            String selection = DbInfo.YEAR_OF_ENTRY + "= ? AND " + DbInfo.MODE_OF_ENTRY + " = ?";
            String[] selectionArgs = {MainScreen.CURRENT_SESSION, MainScreen.MODE_OF_ENTRY_UTME};
            return dbHelper.getReadableDatabase().query(DbInfo.STUDENT, columns, selection, selectionArgs, null, null, orderBy);
        } else {
            //very dangerous but i think its correct
            String data[] = buildYearOfEntry(level);
            String selection = DbInfo.YEAR_OF_ENTRY + " = ? AND (" + DbInfo.MODE_OF_ENTRY
                    + " = ? OR " + DbInfo.MODE_OF_ENTRY + " = ?) OR (" +
                    DbInfo.YEAR_OF_ENTRY + " = ? AND " + DbInfo.MODE_OF_ENTRY + " = ?)";
            String selectionArgs[] = {data[0], MainScreen.MODE_OF_ENTRY_DE, MainScreen.MODE_OF_ENTRY_TRA
                    , data[1], MainScreen.MODE_OF_ENTRY_UTME};
            return dbHelper.getReadableDatabase().query(DbInfo.STUDENT, columns, selection, selectionArgs, null, null, orderBy);
        }
    }


    public Cursor getStudentGrades(String matricNo) {
        String selection = DbInfo.MATRIC_NO + " = ?";
        String[] selectionArgs = {matricNo};
        return query(DbInfo.GRADE, selection, selectionArgs, null);

    }

    public Cursor getStudentGradesAndCourseDetails(String matricNo) {

        String SELECTION_QUERY = "SELECT * FROM "+DbInfo.GRADE +
                " INNER JOIN "+ DbInfo.COURSE
                +" ON "+DbInfo.GRADE + "."+DbInfo.COURSE_CODE +" = "+DbInfo.COURSE+"."+DbInfo.COURSE_CODE+
                " WHERE "+DbInfo.GRADE+"."+DbInfo.MATRIC_NO + " = "+matricNo;

        return dbHelper.getReadableDatabase().rawQuery(SELECTION_QUERY,null);
    }

    public int updateStudentCGPA(ContentValues contentValues, String matricNo) {

        String selection = DbInfo.MATRIC_NO + "= ?";
        String[] selectionArgs = {matricNo};
        return dbHelper.getWritableDatabase().update(DbInfo.STUDENT, contentValues, selection, selectionArgs);
    }
    private Cursor query(String table, String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(table);

        Cursor cursor = builder.query(dbHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        return cursor;
    }

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {
            Log.d("printing exception", ex.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }
    }
}

