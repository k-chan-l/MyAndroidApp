package com.example.myandroidapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment {
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> dayList = new ArrayList<String>();
    private static View temp = null;
    private static View temp2 = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static View preview;

    public WeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekFragment newInstance(String param1, String param2) {
        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //번들 객체를 통해 ArrayList 전달
        int year = getArguments().getInt("Year");
        int month = getArguments().getInt("Month");//+1
        int day = getArguments().getInt("Date");
        Calendar mcal = Calendar.getInstance();
        mcal.set(year, month, day);

        dayList = getArguments().getStringArrayList("Arr");
        View weekView = inflater.inflate(R.layout.fragment_week, container, false);
        //weekView 객체를 얻어옴
        GridView weeklyView = weekView.findViewById(R.id.gridviewday);
        GridView weekdayView = weekView.findViewById(R.id.gridviewdate);

        Cursor cursor;
        DBHelper mDbHelper = new DBHelper(getContext());

        for(int j = 0; j < 24; j ++) {
            list.add(String.format("%d",j));
            for (int i = 0; i < 7; i++)
            {
                int y = mcal.get(Calendar.YEAR);
                int m = mcal.get(Calendar.MONTH)+1;
                int d = mcal.get(Calendar.DATE);
                cursor = mDbHelper.getTimeBySQL(y,m,d,j);
                if(cursor.moveToNext())
                {
                    list.add(cursor.getString(1));
                }
                else list.add("");
                mcal.add(Calendar.DATE,1);
            }
            mcal.add(Calendar.DATE,-7);
        }
        weekdayView.setAdapter(new GridAdapter(getContext(),dayList));
        weeklyView.setAdapter(new GridAdapter(getContext(),list));
        //번들 객체를 이용하여 전달한 인자를 통해 OnClickListner 구현

        weekdayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    if (temp != null)
                        temp.setBackgroundColor(getResources().getColor(R.color.white));
                    if (preview != null)
                        preview.setBackgroundColor(getResources().getColor(R.color.white));
                    v.setBackgroundColor(getResources().getColor(R.color.cyan));
                    temp = v;
            }
        });

        weeklyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position%8!=0){
                        if (temp != null)
                            temp.setBackgroundColor(getResources().getColor(R.color.white));
                        if (temp2 != null)
                            temp2.setBackgroundColor(getResources().getColor(R.color.white));
                        if (preview != null)
                            preview.setBackgroundColor(getResources().getColor(R.color.white));
                        View views = weekdayView.getChildAt(position%8);
                        views.setBackgroundColor(getResources().getColor(R.color.cyan));
                        preview = views;
                        v.setBackgroundColor(getResources().getColor(R.color.cyan));
                        if (temp2 == v)
                        {
                            Cursor cursor = mDbHelper.getTimeBySQL(year, month+1, position % 8+ day -1, position/8); // sql검색
                            final List<String> ListItems = new ArrayList<>();
                            final ArrayList<Integer> ca_id = new ArrayList<>();

                            while (cursor.moveToNext()) {
                                ListItems.add(cursor.getString(1)+"\n");
                                ca_id.add(cursor.getInt(0));

                            }
                            if(!(ListItems.isEmpty() | ca_id.isEmpty()))
                                show(ListItems, ca_id, year, month+1, position % 8+ day, position/8 -1 );
                        }
                        temp2 = v;


                    //position:전체 gridview중 현재 번째, dayNum:위치, 2:첫번째 줄+오차범위
                }
                //버튼 함수 PPPP
                FloatingActionButton fab = weekView.findViewById(R.id.fab);

                fab.setOnClickListener((view) ->{
                    Intent intent = new Intent(getActivity(), add_activity.class);
                    intent.putExtra("year", year);
                    intent.putExtra("month", month+1);
                    intent.putExtra("date", position % 8+ day -1);//다음 Activity로 전달
                    intent.putExtra("time", position/8);
                    startActivityForResult(intent,2);

                });
            }


        });


        // Inflate the layout for this fragment
        return weekView;
    }

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
                convertView = inflater.inflate(R.layout.item_calendar_week, parent, false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridviewweek);

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

    void show(List<String> List, ArrayList<Integer> ca_id, int year, int month, int date, int time)
    {
        final List<String> ListItems = List;
        final ArrayList<Integer> id = ca_id;

        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(String.format("%d.%d.%d일 %d시", year, month, date-1, time+1));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                Intent intent = new Intent(getContext(), add_activity.class);
                intent.putExtra("id",ca_id.get(pos));

                startActivityForResult(intent,2);

            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 결과를 반환하는 액티비티가 FIRST_ACTIVITY_REQUEST_CODE 요청코드로 시작된 경우가 아니거나
        // 결과 데이터가 빈 경우라면, 메소드 수행을 바로 반환함.
        if (requestCode != 2 || data == null)
            return;
        int result = data.getIntExtra("Result", 0);

        if (result == 1) {
            Intent thisact = new Intent(getActivity(), WeekViewActivity.class);
            int month = getArguments().getInt("Month");
            int year = getArguments().getInt("Year");
            int day = getArguments().getInt("Date");
            thisact.putExtra("year", year);
            thisact.putExtra("month", month);
            thisact.putExtra("date",day);
            startActivity(thisact);//현재 액티비티 다시시작
            getActivity().finish();//현재 액티비티 종료
        }
    }
}