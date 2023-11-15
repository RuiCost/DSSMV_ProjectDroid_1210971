package com.example.dssmv_projectdroid;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import service.RequestService;

public class AddLibraryActivity extends AppCompatActivity {


    EditText addressET;

    EditText nameET;
    EditText openDaysTimeET;
    Button addLib;

    TimePicker closeTimePicker; // Adicione essa linha
    TimePicker openTimePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_library);

        addressET = findViewById(R.id.address_add_lib);
        closeTimePicker = findViewById(R.id.closeTime_add_lib);
        nameET = findViewById(R.id.name_add_lib);
        openDaysTimeET = findViewById(R.id.openDays_add_lib);
        openTimePicker = findViewById(R.id.openTime_add_lib);


        addLib = findViewById(R.id.ADDLIBLIB);

        addLib.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String address = addressET.getText().toString();

                String name= nameET.getText().toString();
                String openDays = openDaysTimeET.getText().toString();

                int closeHour = closeTimePicker.getHour();
                int closeMinute = closeTimePicker.getMinute();
                int openHour = openTimePicker.getHour();
                int openMinute = openTimePicker.getMinute();

                String closeTime = formatTime(closeHour, closeMinute);
                String openTime = formatTime(openHour, openMinute);

                if (address.length() > 0 && closeTime.length() > 0 && name.length() > 0 && openDays.length() > 0 && openTime.length() > 0) {
                    addA_NICE_Library("http://193.136.62.24/v1/library", address, closeTime, name, openDays, openTime);
                    Toast.makeText(AddLibraryActivity.this, "Library Added", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddLibraryActivity.this, "Missing some Info", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    private String formatTime(int hour, int minute) {
        return String.format("%02d:%02d", hour, minute);
    }
    private void addA_NICE_Library(String urlStr,String address,String closeTime,String name,String openDays,String openTime) {
        new Thread(){
            public void run(){
                try {

                    RequestService.addLIB_TO_API(urlStr, address,closeTime,name,openDays,openTime);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("MAINACTIVITY","onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MAINACTIVITY","onStop");
    }

}
