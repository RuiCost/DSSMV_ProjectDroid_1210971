package com.example.dssmv_projectdroid;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class UserActivity extends AppCompatActivity {
    private static final String TAG="MainActivity:";
    private Button searchbook;

    private Button searchLib;

    private Button randomBOOK;

    private Button AddLib;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

                                //layout inflado
        searchbook=findViewById(R.id.searchbook1);

        searchLib=findViewById(R.id.searchLib);

        randomBOOK=findViewById(R.id.randomBooook);

        AddLib=findViewById(R.id.adddlibb);

    }


    public void searchBook(View view) {
        Intent intent = new Intent(UserActivity.this, SearchBookActivity.class);
        startActivity(intent);
    }
    public void searchLib(View view) {
        Intent intent = new Intent(UserActivity.this, SearchLibraryActivity.class);
        startActivity(intent);
    }

    public void lik_book(View view) {
        Intent intent = new Intent(UserActivity.this, LikedBooksActivity.class);
        startActivity(intent);
    }

    public void randomBook(View view) {
        Intent intent = new Intent(UserActivity.this, RandomBookByShakingItActivity.class);
        startActivity(intent);
    }

    public void addLIB(View view) {
        Intent intent = new Intent(UserActivity.this, AddLibraryActivity.class);
        startActivity(intent);
    }

}