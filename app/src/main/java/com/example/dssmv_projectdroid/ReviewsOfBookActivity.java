package com.example.dssmv_projectdroid;

import adapter.ListViewAdapterBooks;
import adapter.ListViewAdapterInfoBook;
import adapter.ListViewAdapterReviewsOfBook;
import adapter.MyDAtabaseHelper;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import model.Book;
import model.Library;
import service.RequestService;

import java.util.List;

public class ReviewsOfBookActivity extends AppCompatActivity {
    Book book=null;
    ListView review_list;

    ListViewAdapterReviewsOfBook adapter;
    EditText ed;
    Button bt;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_of_book);

        review_list = (ListView) findViewById(R.id.list_commentery);

        bt=findViewById(R.id.MAKE_MY_REVIEW);

        ed=findViewById(R.id.WRITE_REVIEW);

        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);

        radioButton1.setChecked(true); // selecionado por padrão

        String iSbN = getIntent().getStringExtra("ISBN_OK");
        String ID_LIB = getIntent().getStringExtra("ID_LIBRARY");
        getReviews(iSbN);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Coloque aqui o código para recarregar os comentários
                getReviews(iSbN);

                // Finalize o "refresh" após a atualização
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String review = ed.getText().toString();
                if (!review.isEmpty()) {
                    int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    String recommend = null;
                    if (radioButton1.isChecked()) {
                        recommend = "true";
                    } else if (radioButton2.isChecked()) {
                        recommend = "false";
                    }
                    postReview("http://193.136.62.24/v1/book/" + iSbN + "/review?userId=cff2", review, ID_LIB, recommend);

                    Toast.makeText(getApplicationContext(),
                            "Review done, please refresh the page", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),
                            "No review, no post...", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void rd1(View v) {
        // RadioButton 1 is clicked
        radioButton2.setChecked(false);
    }

    public void rd2(View v) {
        // RadioButton 2 is clicked
        radioButton1.setChecked(false);
    }

    private void getReviews(String iSbN) {
        new Thread() {
            public void run() {
                try {
                    book = RequestService.getReviewsNOW("http://193.136.62.24/v1/book/" + iSbN + "/review?limit=10");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ListViewAdapterReviewsOfBook(getApplicationContext(), R.layout.list_of_revies, book.getReviewers());
                            review_list.setAdapter(adapter); // Defina o novo adaptador com as novas avaliações
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void postReview(String url, String review, String ID, String recommend) {
        new Thread() {
            public void run() {
                try {

                    RequestService.postReview(url, review, recommend);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

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
