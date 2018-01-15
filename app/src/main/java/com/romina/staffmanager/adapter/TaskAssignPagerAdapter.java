package com.romina.staffmanager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.romina.staffmanager.fragment.TaskAssignFirstStepFragment;
import com.romina.staffmanager.fragment.TaskAssignSecondStepFragment;
import com.romina.staffmanager.fragment.TaskAssignThirdStepFragment;

/**
 * Created by Abhilash on 30-07-2017
 */

public class TaskAssignPagerAdapter extends FragmentPagerAdapter{
    private final int NUM_ITEMS=3;

    public TaskAssignPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TaskAssignFirstStepFragment();
            case 1:
                return new TaskAssignSecondStepFragment();
            case 2:
                return new TaskAssignThirdStepFragment();
            default:
                return null;
        }
    }
}
