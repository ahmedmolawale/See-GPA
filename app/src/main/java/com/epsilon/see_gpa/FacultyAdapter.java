package com.epsilon.see_gpa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by ahmed on 19/12/2016.
 */

public class FacultyAdapter extends ExpandableRecyclerAdapter<FacultyViewHolder,DepartmentViewHolder> {

    private LayoutInflater mInflator;
    public FacultyAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList){
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
    }
    @Override
    public FacultyViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View facultyView = mInflator.inflate(R.layout.faculty_item_layout,parentViewGroup,false);
        return new FacultyViewHolder(facultyView);

    }

    @Override
    public DepartmentViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View departmentView = mInflator.inflate(R.layout.department_item_layout,childViewGroup,false);
        return new DepartmentViewHolder(departmentView);
    }

    @Override
    public void onBindParentViewHolder(FacultyViewHolder facultyViewHolder, int i, ParentListItem parentListItem) {

        Faculty faculty = (Faculty) parentListItem;
        facultyViewHolder.onBind(faculty);
    }

    @Override
    public void onBindChildViewHolder(DepartmentViewHolder departmentViewHolder, int i, Object o) {

    }
}
