package com.example.myandroidapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MonthViewActivity extends AppCompatActivity {

    static MyAdapter adapter;

    //년도 월을 표시할 텍스트 뷰
//    private TextView tvDate;


    //요일 저장 리스트
    private ArrayList<String> dateList;


    //일 저장 리스트
    ArrayList<String> dayList;


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

    //
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.item_calendar_gridview);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Month:
                Toast.makeText(getApplicationContext(), "Monthview", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_week:
                Toast.makeText(getApplicationContext(), "Weekview", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthview);


//        // Data 객체를 받을 Intent 생성------------------------------------------------------------------------------------
//        Intent intent = getIntent();
//        int tag_month = intent.getIntExtra("month", 13);//버튼을 눌러서 다음달로 이동 했을 경우 값을 전달 받는다.
//        int tag_year = intent.getIntExtra("year", 0);//버튼을 눌러서 다음달로 이동 햇을 경우 값을 전달 받는다.
//        //-----------------------------------------------------------------------------------------------------------------


        //화면에 표시할 객체 생성----------------------------------------------------------------------
//        tvDate = (TextView) findViewById(R.id.tv_date);//현재 날짜를 표시할 TextView
        //gridView = (GridView) findViewById(R.id.gridview);//현재 날짜를 표시할 Gridview
        //---------------------------------------------------------------------------------------------


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
//        if (tag_month != 13 && tag_year != 0)//전달 받은 값이 있을 경우
//        {
//            //전달 받은 날짜 텍스트뷰에 출력
//            tvDate.setText(tag_year + "년 " + String.format("%02d", tag_month + 1) + "월");//String format을 맞추어서 입력한다.
//
//            //전달 받은 달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
//            mCal.set(tag_year, tag_month, 1);
//        } else {//없을 경우
//            //현재 날짜 텍스트뷰에 출력
//            tvDate.setText(curYearFormat.format(date) + "년 " + curMonthFormat.format(date) + "월");
//
//            //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
//            mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
//        }
//        //------------------------------------------------------------------------------------------------

        // 앱바 이름 현재날짜 표시
      getSupportActionBar().setTitle(curYearFormat.format(date) + "년 " + curMonthFormat.format(date) + "월");
      mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);

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



        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)-----------------------------
        dayList = new ArrayList<String>();
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);

        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }

        setCalendarDate(mCal.get(Calendar.MONTH) + 1);// 아래에 정의
        //--------------------------------------------------------------------------------



        //--Fragment로 데이터 전송하기위해서 번들안에 넣는다-------------------
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("Arr",dayList);
        bundle.putInt("Year",mCal.get(Calendar.YEAR));
        bundle.putInt("Month",mCal.get(Calendar.MONTH));
        bundle.putInt("Daynum",dayNum);
        bundle.putInt("Maximum",mCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        //--------------------------------------------------------------




        // ViewPager 등록------------------------------------------------------------------
        ViewPager2 vpPager = findViewById(R.id.vpPager);
        FragmentStateAdapter adapter = new PagerAdapter(this, bundle);

        vpPager.setAdapter(adapter);

        vpPager.setCurrentItem(1);
        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MonthViewActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        // ----------------------------------------------------------------------------------

    }


    // ----캘린더 날짜 계산 함수-------------------------------------------------------------------
    private void setCalendarDate(int month) {
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


