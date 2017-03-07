package com.epsilon.see_gpa;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ahmed on 11/12/2016.
 */

public class MainScreenRecyclerAdapter extends RecyclerView.Adapter<MainScreenRecyclerAdapter.CustomViewHolder> {

    private MainScreen mainScreen;
    private ArrayList<Student> studentList;

    public MainScreenRecyclerAdapter(MainScreen mainScreen, ArrayList<Student> studentList) {

        this.mainScreen = mainScreen;
        this.studentList = studentList;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_card_view, parent, false);
        CustomViewHolder complexViewHolder = new CustomViewHolder(view, mainScreen,this.studentList);
        return complexViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        //get the particular case
        Student student = studentList.get(position);
        holder.studentName.setText(student.getName());
        holder.studentMatric.setText(student.getMatricNo());
        holder.studentCgpa.setText(student.getCgpa());
        holder.studentModeOfEntry.setText(student.getModeOfEntry());

    }

    @Override
    public int getItemCount() {
        return this.studentList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView studentName;
        private TextView studentMatric;
        private TextView studentModeOfEntry;
        private TextView studentCgpa;
        private CardView studentCardView;
        private MainScreen mainScreen;
        private ArrayList<Student> students;

        public CustomViewHolder(View itemView, MainScreen mainScreen,ArrayList<Student> students) {
            super(itemView);

            this.studentName = (TextView)itemView.findViewById(R.id.student_name);
            this.studentMatric= (TextView)itemView.findViewById(R.id.student_matric);
            this.studentCgpa = (TextView)itemView.findViewById(R.id.student_cgpa);
            this.studentModeOfEntry = (TextView) itemView.findViewById(R.id.student_mode_of_entry);
            this.studentCardView = (CardView) itemView.findViewById(R.id.student_card_view);
            this.mainScreen = mainScreen;
            this.studentCardView.setOnClickListener(this);
            this.students = students;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(this.mainScreen, StudentDetailsList.class);
            //Toast.makeText(mainScreen,"Matric selected is " +students.get(getAdapterPosition()).getMatricNo(),Toast.LENGTH_LONG).show();
            intent.putExtra(MainScreen.MAINSCREEN_TO_DETAIL,students.get(getAdapterPosition()));
            this.mainScreen.startActivity(intent);
        }
    }
}
