package com.example.myandroidapp;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment {
    ArrayList<String> list = new ArrayList<String>();
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
        list = getArguments().getStringArrayList("Arr");
        View weekView = inflater.inflate(R.layout.fragment_week, container, false);
        //weekView 객체를 얻어옴
        GridView weeklyView = weekView.findViewById(R.id.gridviewdate);


        for(int j = 0; j < 24; j ++) {
            list.add(String.format("%d",j));
            for (int i = 0; i < 7; i++)
                list.add("");
        }

        weeklyView.setAdapter(new GridAdapter(getContext(),list));
        //번들 객체를 이용하여 전달한 인자를 통해 OnClickListner 구현

        weeklyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position%8!=0){
                    if(position<8){
                        if (temp != null)
                            temp.setBackgroundColor(getResources().getColor(R.color.white));
                        if (preview != null)
                            preview.setBackgroundColor(getResources().getColor(R.color.white));
                        v.setBackgroundColor(getResources().getColor(R.color.cyan));
                        temp = v;
                    }
                    else{
                        if (temp != null)
                            temp.setBackgroundColor(getResources().getColor(R.color.white));
                        if (temp2 != null)
                            temp2.setBackgroundColor(getResources().getColor(R.color.white));
                        if (preview != null)
                            preview.setBackgroundColor(getResources().getColor(R.color.white));
                        View views = parent.getChildAt(position%8);
                        views.setBackgroundColor(getResources().getColor(R.color.cyan));
                        preview = views;
                        v.setBackgroundColor(getResources().getColor(R.color.cyan));
                        temp2 = v;

                        Toast.makeText(getActivity(), String.format("%d",position), Toast.LENGTH_SHORT).show();//토스트 메시지 출력


                    }



                    //position:전체 gridview중 현재 번째, dayNum:위치, 2:첫번째 줄+오차범위
                }
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
}