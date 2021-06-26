package com.tutionapp;


import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SlideViews extends FragmentStatePagerAdapter {

    List<Fragment> listOfFragments;

    public SlideViews(@NonNull @NotNull FragmentManager fm, List<Fragment> listOfFragments) {
        super(fm);
        this.listOfFragments = listOfFragments;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        return listOfFragments.get(position);
    }

    @Override
    public int getCount() {
        return listOfFragments.size();
    }
}
