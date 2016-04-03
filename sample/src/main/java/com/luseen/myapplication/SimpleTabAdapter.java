package com.luseen.myapplication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by Chatikyan on 19.01.2016.
 */
public class SimpleTabAdapter extends FragmentPagerAdapter {

    private int page = 4;
    private String[] tabtitle = new String[]{"Record", "Like", "Books", "Github"};

    public SimpleTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new BlankFragment();
    }

    @Override
    public int getCount() {
        return page;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitle[position];
    }
}

