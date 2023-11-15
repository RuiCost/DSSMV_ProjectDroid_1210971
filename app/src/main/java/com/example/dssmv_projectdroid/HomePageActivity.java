package com.example.dssmv_projectdroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity {
    private static final String TAG = "HomePageActivity:";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

    }
    public void seeUser(View view) {
        Intent intent=new Intent(HomePageActivity.this,UserActivity.class);
        startActivity(intent);
    }

}
