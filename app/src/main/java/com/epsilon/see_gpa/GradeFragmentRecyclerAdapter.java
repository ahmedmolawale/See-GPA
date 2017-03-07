package com.epsilon.see_gpa;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ahmed on 21/12/2016.
 */

public class GradeFragmentRecyclerAdapter extends RecyclerView.Adapter<GradeFragmentRecyclerAdapter.CustomViewHolder> {

    private Session session;

    public GradeFragmentRecyclerAdapter( Session _session) {


        this.session = _session;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item_card, parent, false);
        CustomViewHolder complexViewHolder = new CustomViewHolder(view,this.session);
        return complexViewHolder;
    }

    @Override
    public void onBindViewHolder(GradeFragmentRecyclerAdapter.CustomViewHolder holder, int position) {
        //get the particular course
       Course course = session.getCoursesTaken().get(position);
        holder.courseCode.setText(course.getCourseCode());
        holder.courseUnit.setText(course.getUnit());
        holder.score.setText(course.getScore());
        holder.gradePoint.setText(course.getGradePoint());
    }

    @Override
    public int getItemCount() {
        return session.getCoursesTaken().size();
    }


    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final CardView gradeCardView;
        private TextView courseCode;
        private TextView courseUnit;
        private TextView score;
        private TextView gradePoint;

        private StudentDetailsList studentDetailsList;
        private Session session;

        public CustomViewHolder(View itemView,Session session) {
            super(itemView);

            this.courseCode = (TextView)itemView.findViewById(R.id.coursecode);
            this.courseUnit= (TextView)itemView.findViewById(R.id.unit);
            this.score = (TextView)itemView.findViewById(R.id.score);
            this.gradePoint = (TextView) itemView.findViewById(R.id.grade);
            this.gradeCardView = (CardView) itemView.findViewById(R.id.grade_item);


            this.studentDetailsList = studentDetailsList;
            this.gradeCardView.setOnClickListener(this);
            this.session = session;
        }
        @Override
        public void onClick(View v) {

        }
    }
}
