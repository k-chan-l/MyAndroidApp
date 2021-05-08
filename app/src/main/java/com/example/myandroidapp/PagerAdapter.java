package com.example.myandroidapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class PagerAdapter extends FragmentStateAdapter {
    private static int NUM_ITEMS=3;
    private String tabTitles[] = new String[] { "First", "Second", "Third" };
    Bundle bundle;
    public PagerAdapter(FragmentActivity fa, Bundle b) {
        super(fa);
        bundle = b;
    }

    // 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public Fragment createFragment(int position) {
        MonthFragment month = new MonthFragment();
        month.setArguments(bundle);
        return month;
    }

    // 전체 페이지 개수 반환
    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }

    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
