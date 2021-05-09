package com.example.myandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Calendar;

public class WeekViewActivity extends AppCompatActivity {

    //Adapter 변수
    static weekViewadapter adapter;

    //캘린더 변수
    private Calendar mCal;

    // 옵션 생성 메뉴 --------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //----------------------------------------------------

    //옵션바 선택
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Month:
                Intent monthintent = new Intent(getApplicationContext(), MonthViewActivity.class);
                monthintent.putExtra("year", mCal.get(Calendar.YEAR));
                monthintent.putExtra("month", mCal.get(Calendar.MONTH));
                startActivity(monthintent);//새롭게 Activity 시작
                overridePendingTransition(0, 0);//애니메이션 제거
                finish();//새 Activity 시작과 동시에 종료
                return true;
            case R.id.action_week:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        // Data 객체를 받을 Intent 생성------------------------------------------------------------------------------------
        Intent intent = getIntent();
        int tag_month = intent.getIntExtra("month", 13);//버튼을 눌러서 다음주로 이동 했을 경우 값을 전달 받는다.
        int tag_year = intent.getIntExtra("year", 0);//버튼을 눌러서 다음주로 이동 했을 경우 값을 전달 받는다.
        int tag_date = intent.getIntExtra("date", 0);//버튼을 눌러서 다음주로 이동 했을 경우 값을 전달 받는다.
        //-----------------------------------------------------------------------------------------------------------------

        //전달 받은 인자에 따라 출력 할 내용 변경----------------------------------------------------------
        mCal = Calendar.getInstance();

        //전달 받은 달 지정
        mCal.set(tag_year, tag_month, tag_date);

        // 앱바 이름 현재날짜 표시
        getSupportActionBar().setTitle(tag_year + "년 " + String.format("%02d", tag_month + 1) + "월");


        //------------------------------------------------------------------------------------------------

        ArrayList<MyItem> data = new ArrayList<MyItem>();
        data.add(new MyItem(""));
        data.add(new MyItem("일"));
        data.add(new MyItem("월"));
        data.add(new MyItem("화"));
        data.add(new MyItem("수"));
        data.add(new MyItem("목"));
        data.add(new MyItem("금"));
        data.add(new MyItem("토"));

        //어댑터 생성
        adapter = new weekViewadapter(this, R.layout.item_weekname, data);

        //어댑터 연결
        GridView weekView = (GridView) findViewById(R.id.weekday2);
        weekView.setAdapter(adapter);


        //전달할 변수 번들객체 생성-------------------------------------------------------------------

        mCal.add(Calendar.DAY_OF_MONTH,-7);
        int dayofweek = (mCal.get(Calendar.DAY_OF_WEEK)+6) % 7;
        mCal.add(Calendar.DAY_OF_MONTH,-dayofweek);
        //1page
        Bundle bundle1 = new Bundle();
        bundle1.putInt("Month",mCal.get(Calendar.MONTH));
        bundle1.putInt("Year",mCal.get(Calendar.YEAR));
        int date1 = mCal.get(Calendar.DATE);
        bundle1.putInt("Date",date1);
        int dayofmonth1 = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        ArrayList<String> dayList1 = new ArrayList<String>();
        dayList1.add("");
        int j = 1;
        for (int i = 0; i < 7; i++){
            //String의 형태로 저장
            if (date1+i <= dayofmonth1)
                dayList1.add(String.format("%d",date1+i));
            else
                dayList1.add(String.format("%d",j++));

        }
        bundle1.putStringArrayList("Arr",dayList1);

        //2page
        mCal.add(Calendar.DATE,7);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("Month",mCal.get(Calendar.MONTH));
        bundle2.putInt("Year",mCal.get(Calendar.YEAR));
        int date2 = mCal.get(Calendar.DATE);
        bundle2.putInt("Date",date2);
        int dayofmonth2 = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        ArrayList<String> dayList2 = new ArrayList<String>();
        dayList2.add("");
        j = 1;
        for (int i = 0; i < 7; i++){
            //String의 형태로 저장
            if (date2+i <= dayofmonth2)
                dayList2.add(String.format("%d",date2+i));
            else
                dayList2.add(String.format("%d",j++));

        }
        bundle2.putStringArrayList("Arr",dayList2);

        //3page
        mCal.add(Calendar.DATE,7);
        Bundle bundle3 = new Bundle();
        bundle3.putInt("Month",mCal.get(Calendar.MONTH));
        bundle3.putInt("Year",mCal.get(Calendar.YEAR));
        int date3 = mCal.get(Calendar.DATE);
        bundle3.putInt("Date",date3);
        int dayofmonth3 = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        ArrayList<String> dayList3 = new ArrayList<String>();
        dayList3.add("");
        j = 1;
        for (int i = 0; i < 7; i++){
            //String의 형태로 저장
            if (date3+i <= dayofmonth3)
                dayList3.add(String.format("%d",date3+i));
            else
                dayList3.add(String.format("%d",j++));

        }
        bundle3.putStringArrayList("Arr",dayList3);

        mCal.add(Calendar.DATE,-1);


        //------------------------------------------------------------------------------------------

// ViewPager 등록------------------------------------------------------------------
        ViewPager2 vpPager = findViewById(R.id.vpPager2);
        FragmentActivity that = this;
        FragmentStateAdapter fadapter = new WeekPagerAdapter(this, bundle1, bundle2, bundle3);
        vpPager.setAdapter(fadapter);
        vpPager.setCurrentItem(1,false);
        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Intent implicit_intent = null;
                if(position == 0)
                {
                    mCal.add(Calendar.DATE,-7);
                    Intent intent = new Intent(getApplicationContext(), WeekViewActivity.class);//intent객체 생성

                    //MONTH 인자 전달
                    int MONTH = mCal.get(Calendar.MONTH);//현재 월 반환
                    intent.putExtra("month", MONTH);//다음 Activity로 전달

                    //YEAR 인자 전달
                    int YEAR = mCal.get(Calendar.YEAR);//현재 년도 반환
                    intent.putExtra("year", YEAR);//다음 Activity로 전달

                    int DATE = mCal.get(Calendar.DATE);
                    intent.putExtra("date",DATE);

                    implicit_intent = intent;//implicit_intent에 intent객채를 전달
                }
                else if (position == 2)
                {

                    mCal.add(Calendar.DATE,7);
                    Intent intent = new Intent(getApplicationContext(), WeekViewActivity.class);//intent객체 생성

                    //MONTH 인자 전달
                    int MONTH = mCal.get(Calendar.MONTH);//현재 월 반환
                    intent.putExtra("month", MONTH);//다음 Activity로 전달

                    //YEAR 인자 전달
                    int YEAR = mCal.get(Calendar.YEAR);//현재 년도 반환
                    intent.putExtra("year", YEAR);//다음 Activity로 전달

                    int DATE = mCal.get(Calendar.DATE);
                    intent.putExtra("date",DATE);

                    implicit_intent = intent;//implicit_intent에 intent객채를 전달
                }

                if (implicit_intent != null) {
                    startActivity(implicit_intent);//새롭게 Activity 시작
                    overridePendingTransition(0, 0);//애니메이션 제거
                    finish();//새 Activity 시작과 동시에 현재 월 종료
                }
            }
        });
        // ----------------------------------------------------------------------------------
    }
}