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

    //그리드뷰 어댑터
    private GridAdapter gridAdapter;


    //그리드뷰
    private GridView gridView;




    //일 저장 리스트
    private ArrayList<String> dayList;


    //캘린더 변수
    private Calendar mCal;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

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







        // Data 객체를 받을 Intent 생성------------------------------------------------------------------------------------
        Intent intent = getIntent();
        int tag_month = intent.getIntExtra("month", 13);//버튼을 눌러서 다음달로 이동 했을 경우 값을 전달 받는다.
        int tag_year = intent.getIntExtra("year", 0);//버튼을 눌러서 다음달로 이동 햇을 경우 값을 전달 받는다.
        //-----------------------------------------------------------------------------------------------------------------


        //화면에 표시할 객체 생성----------------------------------------------------------------------
//        tvDate = (TextView) findViewById(R.id.tv_date);//현재 날짜를 표시할 TextView
        gridView = (GridView) findViewById(R.id.gridview);//현재 날짜를 표시할 Gridview
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
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);
        //--------------------------------------------------------------------------------


        //그리드뷰 객체 생성---------------------------------------------------------------

        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);





//        TextView C =(TextView).findViewById(R.id.tv_item_gridview);

        // 항목 클릭 이벤트 처리
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if ((position - dayNum + 2) > 0 &&  mCal.getActualMaximum(Calendar.DAY_OF_MONTH) >= (position - dayNum + 2))//날짜가 존재 할 경우

                    Toast.makeText(MonthViewActivity.this, mCal.get(Calendar.YEAR) + "." + (mCal.get(Calendar.MONTH) + 1) + "." + (position - dayNum + 2), Toast.LENGTH_SHORT).show();//토스트 메시지 출력

//                    C.setBackgroundColor(Color.BLACK);
                //position:전체 gridview중 현재 번째, dayNum:위치, 2:첫번째 줄+오차범위
            }
        });
        //---------------------------------------------------------------------------------

//        ViewPager2 vpPager = findViewById(R.id.vpPager);
//        FragmentStateAdapter adapter = new PagerAdapter(this);
//        vpPager.setAdapter(adapter);
//
//        vpPager.setCurrentItem(1);
    }


    /*해당 월에 표시할 일 수 구하는 함수
     *
     * @param month-------------------------------------------------------------------------
     */
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


    //그리드뷰 어댑터-------------------------------------------------------------------------
    private class GridAdapter extends BaseAdapter {

        private final List<String> list;

        private final LayoutInflater inflater;

        /**
         * 생성자
         *
         * @param context
         * @param list
         */
        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));


            return convertView;
        }
    }

    //textView 설정
    private class ViewHolder {
        TextView tvItemGridView;
    }

    //------------------------------------------------------------------------------------------------------




    //버튼 클릭시 이벤트 처리 함수---------------------------------------------------------------------------

//    public void btnClick(View view){
//        Intent implicit_intent = null;
//        switch (view.getId()) {
//            case R.id.prev: {//이전 버튼 클릭
//                Intent intent = new Intent(getApplicationContext(), MonthViewActivity.class);//intent객체 생성
//
//                //MONTH 인자 전달
//                int MONTH = mCal.get(Calendar.MONTH);//현재 월 반환
//                MONTH = (MONTH + 11) % 12;//이전 달 반환(MONTH는 0 ~ 11사이의 값)
//                intent.putExtra("month", MONTH);//다음 Activity로 전달
//
//                //YEAR 인자 전달
//                int YEAR = mCal.get(Calendar.YEAR);//현재 년도 반환
//                if (MONTH == 11)//바뀐 MONTH가 11일경우(1월에서 12월로 넘어간경우)
//                    YEAR--;//년도수를 1줄인다.
//                intent.putExtra("year", YEAR);//다음 Activity로 전달
//
//                implicit_intent = intent;//implicit_intent에 intent객채를 전달
//                break;
//            }
//
//            case R.id.next: {//다음 버튼 클릭
//                Intent intent = new Intent(getApplicationContext(), MonthViewActivity.class);//intent객체 생성
//
//                //MONTH 인자 전달
//                int MONTH = mCal.get(Calendar.MONTH);//현재 월 반환
//                MONTH = (MONTH + 1) % 12;//다음 달 반환(MONTH는 0 ~ 11사이의 값)
//                intent.putExtra("month", MONTH);//다음 Activity로 전달
//
//                //YEAR 인자 전달
//                int YEAR = mCal.get(Calendar.YEAR);//현재 년도 반환
//                if (MONTH == 0)//바뀐 MONTH가 0일경우(12월에서 1월로 넘어간경우)
//                    YEAR++;//년도수를 1줄인다.
//                intent.putExtra("year", YEAR);//다음 Activity로 전달
//
//                implicit_intent = intent;//implicit_intent에 intent객채를 전달
//                break;
//            }
//        }
//        if (implicit_intent != null) {
//            startActivity(implicit_intent);//새롭게 Activity 시작
//            finish();//새 Activity 시작과 동시에 현재 월 종료
//        }
//    }
    //-----------------------------------------------------------------------------------------------------------
}


