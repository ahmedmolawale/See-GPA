package com.epsilon.see_gpa;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

/**
 * Created by ahmed on 19/12/2016.
 */

public class FacultyViewHolder extends ParentViewHolder {

    private TextView mFacultyTextView;
    public FacultyViewHolder(View itemView) {
        super(itemView);
        mFacultyTextView = (TextView) itemView.findViewById(R.id.faculty_text);
    }
    public void onBind(Faculty faculty){
       // mFacultyTextView.setText(faculty.getName());

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
