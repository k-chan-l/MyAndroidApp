package com.example.myandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class add_activity extends AppCompatActivity{
    final static String TAG2="SQLITEDBTEST";

    private DBHelper mDbHelper;
    private static final String TAG = "add_activity";

    String title;
    String mem;
    int edMinute;
    int edHour;
    int stMinute;
    int stHour;

    SupportMapFragment mapFragment;
    GoogleMap map;
    private Button btnKor2Loc;
    private EditText editText;
    private EditText memo;
    private TimePicker start;
    private TimePicker end;
    private int tag_year;
    private int tag_month;
    private int tag_date;
    private int tag_id;


    MarkerOptions myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mDbHelper = new DBHelper(this);

        Intent intent = getIntent();

        checkDangerousPermissions();
        getSupportActionBar().setTitle("CalendarApp");
        EditText ed = (EditText) findViewById(R.id.sel);
        Cursor cursor;

        tag_id = intent.getIntExtra("id",0);
        editText = findViewById(R.id.Ed);
        btnKor2Loc = findViewById(R.id.button2);
        memo = findViewById(R.id.memo);
        start = findViewById(R.id.TPKst);
        end = findViewById(R.id.TPKend);

        if(tag_id == 0) {
            tag_month = intent.getIntExtra("month", 13);//버튼을 눌러서 다음달로 이동 했을 경우 값을 전달 받는다.
            tag_year = intent.getIntExtra("year", 0);//버튼을 눌러서 다음달로 이동 햇을 경우 값을 전달 받는다.
            tag_date = intent.getIntExtra("date", 0);
            if(intent.getIntExtra("time", 24) != 24) {
                start.setHour(intent.getIntExtra("time", 24));
                start.setMinute(0);
            }

            ed.setText(tag_year + "년 " + tag_month + "월" + tag_date + "일", TextView.BufferType.EDITABLE);
        }
        else {
            cursor = mDbHelper.getIDBySQL(tag_id);
            if (cursor.moveToNext()) {
                title = cursor.getString(1);
                mem = cursor.getString(2);
                tag_year = cursor.getInt(3);
                tag_month = cursor.getInt(4);
                tag_date = cursor.getInt(5);
                edMinute = cursor.getInt(6);
                edHour = cursor.getInt(7);
                stMinute = cursor.getInt(8);
                stHour = cursor.getInt(9);
            }
            ed.setText(title);
            memo.setText(mem);
            start.setHour(stHour);
            start.setMinute(stMinute);
            end.setHour(edHour);
            end.setMinute(edMinute);
        }


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: ");
                map = googleMap;
                if (ActivityCompat.checkSelfPermission(add_activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(add_activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                map.setMyLocationEnabled(true);
            }
        });
        MapsInitializer.initialize(this);

        btnKor2Loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() > 0) {


                    Location location = getLocationFromAddress(getApplicationContext(), editText.getText().toString());

                    showCurrentLocation(location);



                }

            }

        });

        Button save = findViewById(R.id.SVbt);
        Button cancel = findViewById(R.id.CAbt);
        Button delete = findViewById(R.id.DEbt);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = ed.getText().toString();
                mem = memo.getText().toString();
                edMinute = end.getMinute();
                edHour = end.getHour();
                stMinute = start.getMinute();
                stHour = start.getHour();

                if(title == null) {
                    Log.e(TAG, "error tilte is null");
                    return;
                }

                if(tag_id == 0)
                    insertRecord(title, mem, tag_year, tag_month, tag_date, edMinute, edHour, stMinute, stHour);
                else
                    updateRecord(tag_id, title, mem, tag_year, tag_month, tag_date, edMinute, edHour, stMinute, stHour);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("Result", 1);
                setResult(RESULT_OK, resultIntent);
                finish();


            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tag_id == 0)
                    finish();
                deleteRecord(tag_id);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Result", 1);
                setResult(RESULT_OK, resultIntent);

                finish();
            }
        });


    }




    private Location getLocationFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        Location resLocation = new Location("");
        try {
            addresses = geocoder.getFromLocationName(address, 5);
            if ((addresses == null) || (addresses.size() == 0)) {
                return null;
            }
            Address addressLoc = addresses.get(0);

            resLocation.setLatitude(addressLoc.getLatitude());
            resLocation.setLongitude(addressLoc.getLongitude());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resLocation;

    }

    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        String msg = "Latitutde : " + curPoint.latitude
                + "\n : " + curPoint.longitude;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        //화면 확대, 숫자가 클수록 확대
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        //마커 찍기
        Location targetLocation = new Location("");
        targetLocation.setLatitude(37.4937);
        targetLocation.setLongitude(127.0643);
        showMyMarker(targetLocation);

    }

    //------------------권한 설정 시작------------------------
    private void checkDangerousPermissions() {
        String[] permissions = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
//                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    //------------------권한 설정 끝------------------------

    private void showMyMarker(Location location) {
        if (myMarker == null) {
            myMarker = new MarkerOptions();
            myMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
            myMarker.title("◎ 내위치\n");
            myMarker.snippet("여기가 어디지?");

        }
    }

    private void insertRecord(String title, String mem, int year, int month, int date, int edMinute, int edHour, int stMinute, int stHour) {
            mDbHelper.insertUserBySQL(title, mem, year, month, date, edMinute, edHour, stMinute, stHour);
    }

    private void updateRecord(int id, String title, String memo, int year, int month, int date, int endminute, int endhour, int startminute, int starthour) {
        mDbHelper.updateUserBySQL(id, title, memo, year, month, date, endminute, endhour, startminute, starthour);
    }

    private void deleteRecord(int id){
        mDbHelper.deleteUserBySQL(id);
    }

}



