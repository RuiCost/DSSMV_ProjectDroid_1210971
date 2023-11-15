package com.example.dssmv_projectdroid;


import adapter.ListViewAdapterInfoBook;
import adapter.MyDAtabaseHelper;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import model.Book;
import model.Library;
import model.Reviewer;
import network.HttpOperation;
import service.RequestService;

import java.util.ArrayList;
import java.util.List;

public class InfBookActivity extends AppCompatActivity  {
    MyDAtabaseHelper myDB;
    Spinner spinner;
    TextView textView;
    TextView textView1;

    TextView textView2;
    ImageView imageView;
    ListView list;
    ListViewAdapterInfoBook adapter;

    List<String> listss = new ArrayList<>();
    Book book;

    RatingBar ratingBar;

    Book book1;
    Button bt;
    EditText ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_book);

        imageView=findViewById(R.id.thumbImage);

        textView=findViewById(R.id.name);

        textView1 = findViewById(R.id.author_b);
        textView2 =findViewById(R.id.tags);

        bt = findViewById(R.id.reviewButton);
        list = findViewById(R.id.infoListView);

        adapter = new ListViewAdapterInfoBook(this, R.layout.list_of_inf_book, listss);
        list.setAdapter(adapter);

        myDB = new MyDAtabaseHelper(this);

        ratingBar = findViewById(R.id.ratingBar);

        book=new Book();

        String ISBN = getIntent().getStringExtra("ISBN");

        // buscar informacao para a lista
        getAllBookInfo("http://193.136.62.24/v1/book/"+ISBN+"?persist=true","http://193.136.62.24/v1/book/"+ISBN+"/review?limit=10");


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(InfBookActivity.this, ReviewsOfBookActivity.class);
                intent.putExtra("ISBN_OK",ISBN);
                startActivity(intent);
            }
        });


        /// PARTE DO CORACAO

        final boolean[] isLiked = new boolean[1];
        isLiked[0] = myDB.checkISBNExists(ISBN); // Se tiver na base de dados fica vermelho (true) se nao fica preto (false)
        ImageButton heartButton = findViewById(R.id.heartButton);
        heartButton.setSelected(isLiked[0]); // Define a cor inicial do coração conforme o valor de isLiked
        // Nao sei bem porque mas tem que ser boolean do tipo array
        heartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    isLiked[0] =!isLiked[0];
                    if(isLiked[0]){
                        putINmyDB("http://193.136.62.24/v1/book/" + ISBN + "?persist=true");
                    }else  {
                        deleteBookFromDB(ISBN);
                        adapter.notifyDataSetChanged();
                    }
                    heartButton.setSelected(isLiked[0]); // Define o coração inicialmente conforme o valor de isLiked

            }
        });

        // CENA DAS ESTRELAS
        new Thread(new Runnable() {
            @Override
            public void run() {
                int num_of_reviews = RequestService.getNumOfReviews("http://193.136.62.24/v1/book/" + ISBN + "/review?limit=10");
                int num_of_good_rev= Integer.parseInt(HttpOperation.get("http://193.136.62.24/v1/book/"+ISBN+"/review/recommended-count"));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int yellowColor = ContextCompat.getColor(InfBookActivity.this, R.color.yellow_acastanhado);
                        ColorStateList colorStateList = ColorStateList.valueOf(yellowColor);
                        ratingBar.setProgressTintList(colorStateList);
                        ratingBar.setRating( ((float) num_of_good_rev /(num_of_reviews))*5);
                    }
                });
            }
        }).start();

    }


    private void deleteBookFromDB(String bookISBN) {
        myDB.deleteBookByISBN(bookISBN);
        adapter.notifyDataSetChanged();
    }


    private void putINmyDB(String urlStr) {
        new Thread() {
            public void run() {
                try {
                    book = RequestService.getBookL(urlStr);
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {

                            myDB.addBook(book.getName(), book.getIsbn());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void getAllBookInfo(String urlStr, String urlStr1) {
        new Thread() {
            public void run() {
                try {
                    final String tags_order;
                    book = RequestService.getBookL(urlStr);
                    StringBuilder sb = new StringBuilder();

                    int i;
                    // METER UMA VIRGULA ENTRE AS TAGS
                    for ( i = 0; i < book.getTags().size()-1; i++) {
                        sb.append(book.getTags().get(i)).append(", ");
                    }
                    sb.append(book.getTags().get(i));
                    String tagsWithComma = sb.toString();


                    final List<String> teste = new ArrayList<>();


                    teste.add("TITLE");
                    teste.add(book.getName());
                    teste.add("AUTHOR");
                    teste.add(book.getAuthor());
                    teste.add("DESCRIPTION");
                    teste.add(book.getDescription());
                    teste.add("CATEGORIES");
                    teste.add(tagsWithComma);
                    teste.add("PUBLISH DATE");
                    teste.add(book.getPublishDate());
                    teste.add("PAGES");
                    teste.add(book.getNumberOfPages());
                    teste.add("ISBN");
                    teste.add(book.getIsbn());


                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            imageView.setImageBitmap(book.getIcon());
                            textView.setText(book.getName());
                            textView1.setText(book.getAuthor());
                            if (tagsWithComma.length()!=0) {
                                textView2.setText(tagsWithComma);
                            }
                            adapter.setItems(teste);
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


