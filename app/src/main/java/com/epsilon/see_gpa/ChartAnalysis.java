package com.epsilon.see_gpa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class ChartAnalysis extends AppCompatActivity {

    int firstClass = 0;
    int secondClassUpper = 0;
    int secondClassLower = 0;
    int thirdClass = 0;
    int pass = 0;
    int fail = 0;
    private TableLayout tableLayout;
    private BarChart barChart;

    private final String ID = "ID";
    private final String CATEGORY = "Category";
    private final String NO = "Number";
    SharedPreferences sharedPreferences;
    String gpSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chart_analysis);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        gpSystem = sharedPreferences.getString(MainScreen.CURRENT_CGPA, "7");
        tableLayout = (TableLayout) findViewById(R.id.class_of_grade_table);
        barChart = (BarChart) findViewById(R.id.class_of_grade_bar_chart);
        ArrayList<Student> studentList = new ArrayList<>();
        //get the list of student from the intent passed
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MainScreen.MAINSCREEN_TO_CHART)) {
            studentList = (ArrayList<Student>) intent.getSerializableExtra(MainScreen.MAINSCREEN_TO_CHART);
        }
        if (intent != null && intent.hasExtra(MainScreen.LEVEL_IN_VIEW)) {
            String level = intent.getStringExtra(MainScreen.LEVEL_IN_VIEW);
            //getSupportActionBar().setTitle("Chart Analysis - "+ level);
        }
        if (gpSystem.equals("7")) {
            for (Student student : studentList) {
                double cgpa = Double.parseDouble(student.getCgpa());
                if (cgpa >= 6.0) {
                    firstClass += 1;
                } else if (cgpa >= 4.6) {
                    secondClassUpper += 1;
                } else if (cgpa >= 2.6) {
                    secondClassLower += 1;
                } else if (cgpa >= 1.6) {
                    thirdClass += 1;
                } else if (cgpa >= 1.0) {
                    pass += 1;
                } else
                    fail += 1;
            }
        } else if (gpSystem.equals("5")) {
            for (Student student : studentList) {
                double cgpa = Double.parseDouble(student.getCgpa());
                if (cgpa >= 4.50) {
                    firstClass += 1;
                } else if (cgpa >= 3.50) {
                    secondClassUpper += 1;
                } else if (cgpa >= 2.40) {
                    secondClassLower += 1;
                } else if (cgpa >= 1.50) {
                    thirdClass += 1;
                } else if (cgpa >= 1.0)
                    pass += 1;
                else
                    fail +=1;
            }

        } else if (gpSystem.equals("4")) {

            for (Student student : studentList) {
                double cgpa = Double.parseDouble(student.getCgpa());
                if (cgpa >= 3.50) {
                    firstClass += 1;
                } else if (cgpa >= 3.00) {
                    secondClassUpper += 1;
                } else if (cgpa >= 2.00) {
                    secondClassLower += 1;
                } else if (cgpa >= 1.00) {
                    thirdClass += 1;
                } else
                    fail += 1;
            }
        }
        //build the table layout now
        constructTableLayout();
        constructBarChart(barChart);
    }

    private void constructBarChart(BarChart barChart) {

        //create the dataset first
        ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
        barEntryArrayList.add(new BarEntry(firstClass, 0));
        barEntryArrayList.add(new BarEntry(secondClassUpper, 1));
        barEntryArrayList.add(new BarEntry(secondClassLower, 2));
        barEntryArrayList.add(new BarEntry(thirdClass, 3));

        //defining the x-label
        ArrayList<String> xLabels = new ArrayList<>();
        xLabels.add("FC");
        xLabels.add("SC-U");
        xLabels.add("SC-L");
        xLabels.add("TC");



        if(gpSystem.equals("4")){
            barEntryArrayList.add(new BarEntry(fail, 4));
            xLabels.add("F");
        }else {

            barEntryArrayList.add(new BarEntry(pass, 4));
            barEntryArrayList.add(new BarEntry(fail, 5));
            xLabels.add("P");
            xLabels.add("F");
        }
        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Class Of Grade");


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        BarData barData = new BarData(xLabels, barDataSet);
        barChart.setData(barData);
        barChart.animateY(2000);
        barChart.animateX(2000);

        barChart.setDoubleTapToZoomEnabled(true);
    }

    private void constructTableLayout() {

        //for the header of the table; note that its just a row
        TableRow header = new TableRow(this);
        header.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        //adding the headers
        TextView column = new TextView(this);
        column.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column.setBackgroundResource(R.drawable.cell_shape_for_header);
        column.setPadding(3, 3, 3, 3);
        column.setText(ID);
        column.setTextColor(this.getResources().getColor(R.color.black));
        header.addView(column);


        TextView column0 = new TextView(this);
        column0.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column0.setBackgroundResource(R.drawable.cell_shape_for_header);
        column0.setPadding(3, 3, 3, 3);
        column0.setText(CATEGORY);
        column0.setTextColor(this.getResources().getColor(R.color.black));
        //column1.setTextColor(context.getResources().getColor(R.color.black));
        header.addView(column0);


        TextView column1 = new TextView(this);
        column1.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column1.setBackgroundResource(R.drawable.cell_shape_for_header);
        column1.setPadding(3, 3, 3, 3);
        column1.setText(NO);
        column1.setTextColor(this.getResources().getColor(R.color.black));
        //column1.setTextColor(context.getResources().getColor(R.color.black));
        header.addView(column1);
        tableLayout.addView(header);

        String[] ID = {"FC", "SC-U", "SC-L", "TC", "P","F"};
        String[] CLASS_OF_GRADE = {"First Class", "Second Class(Upper)", "Second Class(Lower)", "Third Class", "Pass","Fail"};
        int[] result = {firstClass, secondClassUpper, secondClassLower, thirdClass, pass,fail};
        //adding the other rows
        for (int i = 0; i < CLASS_OF_GRADE.length; i++) {
            if(gpSystem.equals("4") && i == 4){
                continue;
            }else {
                tableLayout.addView(addRowData(ID[i], CLASS_OF_GRADE[i], Integer.toString(result[i])));
            }
        }

    }

    private TableRow addRowData(String sn, String category, String result) {

        TableRow row = new TableRow(this);
        row.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


        int backgroundResource = R.drawable.cell_shape_pass;

        //adding the data
        TextView column = new TextView(this);
        column.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column.setBackgroundResource(backgroundResource);
        column.setPadding(3, 3, 3, 3);
        column.setText(sn);
        //column1.setTextColor(context.getResources().getColor(R.color.black));

        row.addView(column);

        TextView column0 = new TextView(this);
        column0.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column0.setBackgroundResource(backgroundResource);
        column0.setPadding(3, 3, 3, 3);
        column0.setText(category);
        //column1.setTextColor(context.getResources().getColor(R.color.black));
        row.addView(column0);

        TextView column1 = new TextView(this);
        column1.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        column1.setBackgroundResource(backgroundResource);
        column1.setPadding(3, 3, 3, 3);
        column1.setText(result);
        //column1.setTextColor(context.getResources().getColor(R.color.black));
        row.addView(column1);
        return row;
    }
}
