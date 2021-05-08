package com.example.myandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Calendar;

public class WeekViewActivity extends AppCompatActivity {

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
                monthintent.putExtra("year",mCal.get(Calendar.YEAR));
                monthintent.putExtra("month",mCal.get(Calendar.MONTH));
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
        int tag_date = intent.getIntExtra("date",0);//버튼을 눌러서 다음주로 이동 했을 경우 값을 전달 받는다.
        //-----------------------------------------------------------------------------------------------------------------

        //전달 받은 인자에 따라 출력 할 내용 변경----------------------------------------------------------
        mCal = Calendar.getInstance();

            //전달 받은 달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
            mCal.set(tag_year, tag_month, tag_date);

            // 앱바 이름 현재날짜 표시
            getSupportActionBar().setTitle(tag_year + "년 " + String.format("%02d",tag_month+1) + "월");
        //------------------------------------------------------------------------------------------------


    }


}