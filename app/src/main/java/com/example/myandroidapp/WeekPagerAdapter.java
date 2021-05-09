package com.example.myandroidapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class WeekPagerAdapter extends FragmentStateAdapter{
    private static int NUM_ITEMS = 3;
    private String tabTitles[] = new String[]{"First", "Second", "Third"};
    Bundle bundle1, bundle2, bundle3;

    public WeekPagerAdapter(FragmentActivity fa, Bundle a,Bundle b,Bundle c) {
        super(fa);
        bundle1 = a;
        bundle2 = b;
        bundle3 = c;
    }

    // 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public Fragment createFragment(int position) {
        WeekFragment week = new WeekFragment();
        switch (position){
            case 0:
                week.setArguments(bundle1);
                return week;
            case 1:
                week.setArguments(bundle2);
                return week;
            case 2:
                week.setArguments(bundle3);
                return week;
            default:
                return null;
        }
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