package com.example.dssmv_projectdroid;

import adapter.ListViewAdapterBooks;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import model.Book;
import service.RequestService;
import model.Library;
import helper.Utils;

import java.util.ArrayList;
import java.util.List;

public class SearchBookActivity extends AppCompatActivity {

    Library librarybook=null;
    EditText bookname;
    ListView book_list;

    ListViewAdapterBooks adapter;

    Button searchbook;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbook);

        searchbook=(Button) findViewById(R.id.searchbook);

        bookname =(EditText) findViewById(R.id.book);

        book_list=(ListView) findViewById(R.id.list_book);

        librarybook=new Library();
        // criar um adaptador para a List View, adapter é um intermediario entre os dados e a List View
        adapter=new ListViewAdapterBooks(getApplicationContext(),R.layout.list_item,librarybook.getBooks());
        book_list.setAdapter(adapter);

        //clicar num item da  lista
        book_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =  new Intent(SearchBookActivity.this, InfBookActivity.class);
                Book b = librarybook.getBooks().get(i);

                intent.putExtra("ISBN", b.getIsbn());
                startActivity(intent);
                Log.i("AMAINHAAPP", b.toString()+ "--" + b.getName() +"--" + b.getIsbn());
            }
        });

        //botao
        searchbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookName=bookname.getText().toString();
                if (bookName.length()>0) {
                    getLibraryBook("http://193.136.62.24/v1/search?page=1&query="+bookName);
                }
            }
        });

    }

    private void getLibraryBook(String urlStr){

        new Thread(){
            public void run(){
                try {

                    librarybook = RequestService.getLibraryBook(urlStr);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setItems(librarybook.getBooks());
                            adapter.notifyDataSetChanged();

                        }
                    });
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
