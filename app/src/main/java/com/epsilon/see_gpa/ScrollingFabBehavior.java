package com.epsilon.see_gpa;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;



/**
 * Created by ahmed on 17/12/2016.
 */

public class ScrollingFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {


    private int toolbarHeight;

    public ScrollingFabBehavior(Context context, AttributeSet attributeSet){

        super(context,attributeSet);

    }

}
