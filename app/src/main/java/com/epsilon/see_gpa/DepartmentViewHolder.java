package com.epsilon.see_gpa;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

/**
 * Created by ahmed on 19/12/2016.
 */

public class DepartmentViewHolder extends ChildViewHolder {

    private TextView mDepartmentTextView;
    public DepartmentViewHolder(View itemView){

        super(itemView);
        mDepartmentTextView = (TextView) itemView.findViewById(R.id.department_text);
    }

    public void onBind(Department department){
       // mDepartmentTextView.setText(department.getName());
    }
}
