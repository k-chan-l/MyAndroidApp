package com.example.myandroidapp;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;




public class MonthViewActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthviewactivity);

        final EditText tyear = (EditText)findViewById(R.id.t_year);
        final EditText tmonth = (EditText)findViewById(R.id.t_month);
        Button sendtoday = (Button) findViewById(R.id.sendtoday);
        sendtoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String year = tyear.getText().toString();
                String month = tmonth.getText().toString();

                // Data 객체 생성
                Data data = new Data(year, month);

                //Intent에 data객체 저장

                Intent intent = new Intent(MonthViewActivity.this, MainActivity.class);
                intent.putExtra("data", data);

                // MainActivity로 전환
                startActivity(intent);
            }
        });
    }

}



