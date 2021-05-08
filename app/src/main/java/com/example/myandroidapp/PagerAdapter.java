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
    Bundle bundle1,bundle2,bundle3;
    public PagerAdapter(FragmentActivity fa, Bundle a,Bundle b,Bundle c) {
        super(fa);
        bundle1 = a;
        bundle2 = b;
        bundle3 = c;
    }

    // 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public Fragment createFragment(int position) {
        MonthFragment month = new MonthFragment();
        switch (position) {
            case 0:
                month.setArguments(bundle1);
                return month;
            case 1:
                month.setArguments(bundle2);
                return month;
            case 2:
                month.setArguments(bundle3);
                return month;
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
