package com.example.myandroidapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MonthViewActivity extends AppCompatActivity {

    //Adapter 변수
    static MyAdapter adapter;

    //요일 저장 리스트
    private ArrayList<String> dateList;


    //캘린더 변수
    private Calendar mCal;

    private DBHelper mDbHelper;

    public void date(int a,int b, int c){
        mCal.set(a, b, c);
    }





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
                return true;
            case R.id.action_week:
                Intent weekintent = new Intent(getApplicationContext(), WeekViewActivity.class);
                weekintent.putExtra("year",mCal.get(Calendar.YEAR));
                weekintent.putExtra("month",mCal.get(Calendar.MONTH));
                weekintent.putExtra("date",mCal.get(Calendar.DATE));
                startActivity(weekintent);//새롭게 Activity 시작
                overridePendingTransition(0, 0);//애니메이션 제거
                finish();//새 Activity 시작과 동시에 종료
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthview);

        mDbHelper = new DBHelper(this);


        // Data 객체를 받을 Intent 생성------------------------------------------------------------------------------------
        Intent intent = getIntent();
        int tag_month = intent.getIntExtra("month", 13);//버튼을 눌러서 다음달로 이동 했을 경우 값을 전달 받는다.
        int tag_year = intent.getIntExtra("year", 0);//버튼을 눌러서 다음달로 이동 햇을 경우 값을 전달 받는다.
        //-----------------------------------------------------------------------------------------------------------------




        // 오늘 날짜 세팅------------------------------------------------------------------------------
        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        //---------------------------------------------------------------------------------------------





        //전달 받은 인자에 따라 출력 할 내용 변경----------------------------------------------------------
        mCal = Calendar.getInstance();
        if (tag_month != 13 && tag_year != 0)//전달 받은 값이 있을 경우
        {

            //전달 받은 달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
            mCal.set(tag_year, tag_month, 1);


            // 앱바 이름 현재날짜 표시
            getSupportActionBar().setTitle(tag_year + "년 " + String.format("%02d",tag_month+1) + "월");
        } else {//없을 경우

            //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
            mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
            // 앱바 이름 현재날짜 표시
            getSupportActionBar().setTitle(curYearFormat.format(date) + "년 " + curMonthFormat.format(date) + "월");
        }
        //------------------------------------------------------------------------------------------------


        //gridview 요일 표시-------------------------------------------------------------
        ArrayList<MyItem> data = new ArrayList<MyItem>();
        data.add(new MyItem("일"));
        data.add(new MyItem("월"));
        data.add(new MyItem("화"));
        data.add(new MyItem("수"));
        data.add(new MyItem("목"));
        data.add(new MyItem("금"));
        data.add(new MyItem("토"));

        //어댑터 생성
        adapter = new MyAdapter(this, R.layout.item_weekname, data);

        //어댑터 연결
        GridView weekView = (GridView) findViewById(R.id.weekday);
        weekView.setAdapter(adapter);
        //---------------------------------------------------------------------------------


        //--Fragment로 데이터 전송하기위해서 번들안에 넣는다-----------------------------------
        //1page

        Bundle bundle1 = new Bundle();
        int month1 = (mCal.get(Calendar.MONTH)+ 11) % 12;
        int year1;
        bundle1.putInt("Month",month1);//이전달 반환
        if (month1 == 11)//바뀐 MONTH가 11일경우(1월에서 12월로 넘어간경우)
            year1 = mCal.get(Calendar.YEAR)-1;
        else
            year1 = mCal.get(Calendar.YEAR);
        bundle1.putInt("Year",year1);
        mCal.set(year1,month1,1);
        bundle1.putInt("Maximum",mCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        //지난달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)-----------------------------
        ArrayList<String> dayList1 = new ArrayList<String>();
        int dayNum1 = mCal.get(Calendar.DAY_OF_WEEK);

        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum1; i++) {
            dayList1.add("");
        }

        setCalendarDate(mCal.get(Calendar.MONTH) + 1, dayList1);// 아래에 정의

        bundle1.putStringArrayList("Arr",dayList1);
        bundle1.putInt("Daynum",dayNum1);

        //--------------------------------------------------------------------------------
        //2page
        Bundle bundle2 = new Bundle();
        int month2 = (mCal.get(Calendar.MONTH)+1)%12;
        int year2;
        bundle2.putInt("Month",(mCal.get(Calendar.MONTH) + 1) % 12);//다음달 반환
        if (month2 == 0)//바뀐 MONTH가 11일경우(1월에서 12월로 넘어간경우)
            year2 = mCal.get(Calendar.YEAR)+1;
        else
            year2 = mCal.get(Calendar.YEAR);
        bundle2.putInt("Year",year2);
        mCal.set(year2,month2,1);
        bundle2.putInt("Maximum",mCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        //다음달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)-----------------------------
        ArrayList<String> dayList2 = new ArrayList<String>();
        int dayNum2 = mCal.get(Calendar.DAY_OF_WEEK);

        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum2; i++) {
            dayList2.add("");
        }

        setCalendarDate(mCal.get(Calendar.MONTH) + 1, dayList2);// 아래에 정의
        bundle2.putStringArrayList("Arr",dayList2);
        bundle2.putInt("Daynum",dayNum2);
        //--------------------------------------------------------------------------------

        //3page
        Bundle bundle3 = new Bundle();
        int month3 = (mCal.get(Calendar.MONTH)+1)%12;
        int year3;
        bundle3.putInt("Month",(mCal.get(Calendar.MONTH) + 1) % 12);//다음달 반환
        if (month3 == 0)//바뀐 MONTH가 11일경우(1월에서 12월로 넘어간경우)
            year3 = mCal.get(Calendar.YEAR)+1;
        else
            year3 = mCal.get(Calendar.YEAR);
        bundle3.putInt("Year",year3);
        mCal.set(year3,month3,1);
        bundle3.putInt("Maximum",mCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        //다음달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)-----------------------------
        ArrayList<String> dayList3 = new ArrayList<String>();
        int dayNum3 = mCal.get(Calendar.DAY_OF_WEEK);

        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum3; i++) {
            dayList3.add("");
        }

        setCalendarDate(mCal.get(Calendar.MONTH) + 1, dayList3);// 아래에 정의
        bundle3.putStringArrayList("Arr",dayList3);
        bundle3.putInt("Daynum",dayNum3);

        //------------------------------------------------------------------------------------------------------------------

        //다시 초기화------------------------------------------------------
        int month = (mCal.get(Calendar.MONTH)+ 11) % 12;
        int year;
        bundle1.putInt("Month",month);//이전달 반환
        if (month == 11)//바뀐 MONTH가 11일경우(1월에서 12월로 넘어간경우)
            year = mCal.get(Calendar.YEAR)-1;
        else
            year = mCal.get(Calendar.YEAR);
        bundle1.putInt("Year",year);
        mCal.set(year,month,1);


        //--------------------------------------------------------------




        // ViewPager 등록------------------------------------------------------------------
        ViewPager2 vpPager = findViewById(R.id.vpPager);
        FragmentActivity that = this;
        FragmentStateAdapter adapter = new PagerAdapter(this, bundle1, bundle2, bundle3);
        vpPager.setAdapter(adapter);

        vpPager.setCurrentItem(1,false);
        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Intent implicit_intent = null;
                if(position == 0)
                {
                    Intent intent = new Intent(getApplicationContext(), MonthViewActivity.class);//intent객체 생성

                    //MONTH 인자 전달
                    int MONTH = mCal.get(Calendar.MONTH);//현재 월 반환
                    MONTH = (MONTH + 11) % 12;//이전 달 반환(MONTH는 0 ~ 11사이의 값)
                    intent.putExtra("month", MONTH);//다음 Activity로 전달

                    //YEAR 인자 전달
                    int YEAR = mCal.get(Calendar.YEAR);//현재 년도 반환
                    if (MONTH == 11)//바뀐 MONTH가 11일경우(1월에서 12월로 넘어간경우)
                        YEAR--;//년도수를 1줄인다.
                    intent.putExtra("year", YEAR);//다음 Activity로 전달

                    implicit_intent = intent;//implicit_intent에 intent객채를 전달
                }
                else if (position == 2)
                {
                    Intent intent = new Intent(getApplicationContext(), MonthViewActivity.class);//intent객체 생성

                    //MONTH 인자 전달
                    int MONTH = mCal.get(Calendar.MONTH);//현재 월 반환
                    MONTH = (MONTH + 1) % 12;//다음 달 반환(MONTH는 0 ~ 11사이의 값)
                    intent.putExtra("month", MONTH);//다음 Activity로 전달

                    //YEAR 인자 전달
                    int YEAR = mCal.get(Calendar.YEAR);//현재 년도 반환
                    if (MONTH == 0)//바뀐 MONTH가 0일경우(12월에서 1월로 넘어간경우)
                        YEAR++;//년도수를 1늘린다.
                    intent.putExtra("year", YEAR);//다음 Activity로 전달

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



        //








    }



    // ----캘린더 날짜 계산 함수-------------------------------------------------------------------
    private void setCalendarDate(int month,ArrayList<String> dayList) {
        mCal.set(Calendar.MONTH, month - 1);
        int k=0;
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        for (int i = dayNum; i < 43; i++) {
            k += 1;
            if(k <= mCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                dayList.add("" + (k));
            }
            else{
                dayList.add("");
            }
        }

    }
    //--------------------------------------------------------------------------------------





}



