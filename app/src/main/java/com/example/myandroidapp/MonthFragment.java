package com.example.myandroidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MonthFragment extends Fragment {
    ArrayList<String> list = new ArrayList<String>();
    private static View temp = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MonthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthFragment newInstance(String param1, String param2) {
        MonthFragment fragment = new MonthFragment();
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





    //--Fragment 생성-----------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //번들 객체를 통해 ArrayList 전달
        list = getArguments().getStringArrayList("Arr");
        int dayNum = getArguments().getInt("Daynum");
        int y = getArguments().getInt("Year");
        int m = getArguments().getInt("Month") + 1;

        View monthView = inflater.inflate(R.layout.fragment_month, container, false);
        //monthView 객체를 얻어옴
        GridView monthlyView = monthView.findViewById(R.id.gridview);

        monthlyView.setAdapter(new GridAdapter(getActivity(),list, y, m, dayNum));

        //번들 객체를 이용하여 전달한 인자를 통해 OnClickListner 구현
        monthlyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        Cursor cursor;
                        DBHelper mDbHelper = new DBHelper(getContext());

                        if ((position - dayNum + 2) > 0 &&  getArguments().getInt("Maximum") >= (position - dayNum + 2)) {//날짜가 존재 할 경우
                            if (temp != null)
                                temp.setBackgroundColor(getResources().getColor(R.color.white));
                            v.setBackgroundColor(getResources().getColor(R.color.cyan));
                            if (temp == v){
                                cursor = mDbHelper.getCurentDayBySQL(y, m, position - dayNum + 2); // sql검색
                                final List<String> ListItems = new ArrayList<>();
                                final ArrayList<Integer> ca_id = new ArrayList<>();

                                while (cursor.moveToNext()) {
                                    ListItems.add(cursor.getString(1)+"\n");
                                    ca_id.add(cursor.getInt(0));


                                }
                                if(!(ListItems.isEmpty() | ca_id.isEmpty()))
                                    show(ListItems, ca_id, y, m, position - dayNum + 2);
                            }



                            temp = v;
                            int day = position - dayNum + 2;
                            int month = getArguments().getInt("Month") + 1;
                            int year = getArguments().getInt("Year");
                            Activity act = getActivity();
                            Toast.makeText(act,  year + "." + month + "." + day +"일", Toast.LENGTH_SHORT).show();//토스트 메시지 출력

                            if (act instanceof MonthViewActivity)
                                ((MonthViewActivity) act).date(year, (month+11)%12, day);


                            //position:전체 gridview중 현재 번째, dayNum:위치, 2:첫번째 줄+오차범위

                            //버튼 함수  PPPP
                            FloatingActionButton fab = monthView.findViewById(R.id.fab);

                            fab.setOnClickListener((view) ->{
                                Intent intent = new Intent(getActivity(), add_activity.class);
                                intent.putExtra("year", year);
                                intent.putExtra("month", month);
                                intent.putExtra("date", day);//다음 Activity로 전달
                                startActivityForResult(intent,1);

                            });

                        }
                    }
                });




        // Inflate the layout for this fragment
        return monthView;
    }



    //--------------------------------------------------------------------------


    //그리드뷰 어댑터-------------------------------------------------------------------------
    private class GridAdapter extends BaseAdapter {

        private final List<String> list;

        private final LayoutInflater inflater;

        private int y;

        private int m;

        private int dn;

        /**
         * 생성자
         *
         * @param context
         * @param list
         */

        public GridAdapter(Context context, List<String> list, int year, int month, int dayNum) {
            this.y = year;
            this.m = month;
            this.dn = dayNum;
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
            Cursor cursor;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);
                TextView item1 = (TextView) convertView.findViewById(R.id.item1);
                TextView item2 = (TextView) convertView.findViewById(R.id.item2);
                DBHelper mDbHelper = new DBHelper(getContext());

                if ((position - dn + 2) > 0 &&  getArguments().getInt("Maximum") >= (position - dn + 2)) {
                    cursor = mDbHelper.getCurentDayBySQL(y, m, position - dn + 2); // sql검색
                    StringBuffer buffer = new StringBuffer();
                    StringBuffer buffer2 = new StringBuffer();
                    if (cursor.moveToNext()) {//1번
                        buffer.append(cursor.getString(1)+"\n");
                        item1.setText(buffer);
                        item1.setBackgroundColor(getResources().getColor(R.color.lime));
                    }
                    if (cursor.moveToNext()) {//2번
                        buffer2.append(cursor.getString(1)+"\n");
                        item2.setText(buffer2);
                        item2.setBackgroundColor(getResources().getColor(R.color.teal_200));
                    }

                }



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

    void show(List<String> List, ArrayList<Integer> ca_id, int year, int month, int date)
    {
        final List<String> ListItems = List;
        final ArrayList<Integer> id = ca_id;

        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(String.format("%d.%d.%d일", year, month, date));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                Intent intent = new Intent(getContext(), add_activity.class);
                intent.putExtra("id",ca_id.get(pos));

                startActivityForResult(intent,1);

            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 결과를 반환하는 액티비티가 FIRST_ACTIVITY_REQUEST_CODE 요청코드로 시작된 경우가 아니거나
        // 결과 데이터가 빈 경우라면, 메소드 수행을 바로 반환함.
        if (requestCode != 1 || data == null)
            return;
        int result = data.getIntExtra("Result",0);

        if (result == 1) {
            Intent thisact = new Intent(getActivity(), MonthViewActivity.class);
            int month = getArguments().getInt("Month");
            int year = getArguments().getInt("Year");
            thisact.putExtra("year", year);
            thisact.putExtra("month", month);
            startActivity(thisact);//현재 액티비티 다시시작
            getActivity().finish();//현재 액티비티 종료
        }



    }

}