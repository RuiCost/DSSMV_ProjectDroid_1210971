package com.example.dssmv_projectdroid;

import adapter.ListViewAdapterBooksOfLibrary;
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
        /*
        bt=findViewById(R.id.btReview);
        ed=findViewById(R.id.ReviewPost);
        */
        book=new Book();

        String valor = getIntent().getStringExtra("ISBN");

        String ID_LIB = getIntent().getStringExtra("ID_LIBRARY");

        getLibraryBookandReview("http://193.136.62.24/v1/book/"+valor+"?persist=true","http://193.136.62.24/v1/book/"+valor+"/review?limit=10");

       /////////////
        List<String> bookTitles = myDB.getAllBookTitles();

        StringBuilder message = new StringBuilder();
        for (String title : bookTitles) {
            message.append(title).append("\n");
        }


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(InfBookActivity.this, ReviewsOfBookActivity.class);
                intent.putExtra("ID_LIBRARY",ID_LIB);
                intent.putExtra("ISBN_OK",valor);
                startActivity(intent);
            }
        });
        final boolean[] isLiked;
        myDB.getLikeStatusByISBN(valor);
        if(myDB.getLikeStatusByISBN(valor).equals("0")) {
            isLiked = new boolean[]{false}; // Inicialmente, o livro está "unliked" (coração preto)
        }else {
            isLiked = new boolean[]{true};
        }
        ImageButton heartButton = findViewById(R.id.heartButton);
        heartButton.setSelected(isLiked[0]); // Define o coração inicialmente conforme o valor de isLiked
        heartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    isLiked[0]=!isLiked[0];
                    if(isLiked[0]){
                        myDB.updateLikeStatus(valor,"1");
                        putINmyDB("http://193.136.62.24/v1/book/" + valor + "?persist=true", "http://193.136.62.24/v1/book/" + valor + "/review?limit=10");
                    }else  {
                        myDB.updateLikeStatus(valor,"0");
                        deleteBookFromDB(valor);
                        adapter.notifyDataSetChanged();
                    }
                    heartButton.setSelected(isLiked[0]); // Define o coração inicialmente conforme o valor de isLiked

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                int num_of_reviews = RequestService.getNumOfReviews("http://193.136.62.24/v1/book/" + valor + "/review?limit=10");
                int num_of_good_rev= Integer.parseInt(HttpOperation.get("http://193.136.62.24/v1/book/"+valor+"/review/recommended-count"));
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


    private void putINmyDB(String urlStr, String urlStr1) {
        new Thread() {
            public void run() {
                try {
                    // Resto do seu código para obter os detalhes do livro da API...
                    book = RequestService.getBookL(urlStr, urlStr1);
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            // Atualizar a UI com os detalhes do livro...

                            // Inserir os dados do livro na base de dados
                            myDB.addBook(book.getName(), book.getIsbn(), "1");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void getLibraryBookandReview(String urlStr, String urlStr1) {
        new Thread() {
            public void run() {
                try {
                    final String tags_order;
                    book = RequestService.getBookL(urlStr, urlStr1);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < book.getTags().size(); i++) {
                        sb.append(book.getTags().get(i)).append(", ");
                    }
                    String tagsWithComma = sb.toString();



// Remover a última vírgula e espaço da string
                    if (tagsWithComma.length() > 2) { // Verificar se a string possui pelo menos 2 caracteres (vírgula e espaço)
                        tags_order = tagsWithComma.substring(0, tagsWithComma.length() - 2);
                    } else {
                        tags_order = tagsWithComma; // Se a string não tiver vírgulas e espaços extras, manter a mesma string
                    }

                    final List<String> teste = new ArrayList<>();


                    teste.add("TITLE");
                    teste.add(book.getName());
                    teste.add("AUTHOR");
                    teste.add(book.getAuthor());
                    teste.add("DESCRIPTION");
                    teste.add(book.getDescription());
                    teste.add("CATEGORIES");
                    teste.add(tags_order);
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
                            if (tags_order.length()!=0) {
                                textView2.setText(tags_order);
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


