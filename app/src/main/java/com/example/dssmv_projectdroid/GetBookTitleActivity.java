package com.example.dssmv_projectdroid;

import adapter.ListViewAdapterBooks;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import model.Book;
import service.RequestService;

import java.util.ArrayList;
import java.util.List;

public class GetBookTitleActivity extends AppCompatActivity {

    EditText isbn;
    Button getbooktitle;
    ListView listbooktitle;
    ArrayAdapter<String> arrayAdapter;
    Book bookTitle= new Book();



    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gettitle);
        isbn = findViewById(R.id.bookisbn);
        listbooktitle = findViewById(R.id.listbooktitle);
        getbooktitle = findViewById(R.id.getbooktitle);

        getbooktitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isbn1 = isbn.getText().toString();
                if (isbn1.length() > 12) {
                    getBookTitle("http://193.136.62.24/v1/book/"+isbn1);
                }
            }
        });


    }

    private void getBookTitle(String urlStr) {

        new Thread() {
            @Override
            public void run() {
                try {

                    /*bookTitle = RequestService.getBookTitle(urlStr);*/
                    String book = bookTitle.getName();
                    List<String> bookList =new ArrayList<>();
                    bookList.add(book);
                    arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, bookList);
                    listbooktitle.setAdapter(arrayAdapter);


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        };

    }
}

