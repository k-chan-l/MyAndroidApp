package com.example.myandroidapp;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {

    private static int NUM_ITEMS=3;

    public PagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    // 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                MonthViewFragment1 first = new MonthViewFragment1();
                return first;
            case 1:
                MonthViewFragment second = new MonthViewFragment();
                return second;
            case 2:
                MonthViewFragment3 third = new MonthViewFragment3();
                return third;
            default:
                return null;
        }
    }

    // 전체 페이지 개수 반환
    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }
}
