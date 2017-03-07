package com.epsilon.see_gpa;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 19/12/2016.
 */

public class Faculty implements ParentListItem {

    //a faculty contains several departments
    private List mDepartment;

    public Faculty(List departments){
        mDepartment = departments;
    }
    @Override
    public List<?> getChildItemList() {
        return mDepartment;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
