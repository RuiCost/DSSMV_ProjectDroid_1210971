package com.example.dssmv_projectdroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {
    private static final String TAG="HomePageActivity:";
    private Button searchbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        searchbook=findViewById(R.id.searchbook);

    }


    public void searchBook(View view) {
        Intent intent = new Intent(AdminActivity.this, SearchBookActivity.class);
        startActivity(intent);
    }

    public void addBook(View view) {
        Intent intent = new Intent(AdminActivity.this, AddBookActivity.class);
        startActivity(intent);
    }

    public void getBookTitle(View view){
        Intent intent = new Intent(AdminActivity.this, GetBookTitleActivity.class);
        startActivity(intent);
    }


}