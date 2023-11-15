package com.example.dssmv_projectdroid;

import adapter.ListViewAdapterBooks;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import helper.Utils;
import model.Book;
import model.Library;
import service.RequestService;
import java.util.ArrayList;
import java.util.List;

public class ListOfBooksOfOneLibrary extends AppCompatActivity {
    Library librarybook=null;
    EditText barforbooks;
    ListView book_list;
    ListViewAdapterBooks adapter;
    List<Book> books = new ArrayList<>();

    TextView textView_name;
    TextView textView_address;
    TextView textView_openTime;
    TextView textView_closeTime;
    TextView textView_openDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_books_library);
        String ID_OF_LIB = getIntent().getStringExtra("SELECTED_LIBRARY");

        barforbooks = findViewById(R.id.LIB_BARRA_A);

        book_list = findViewById(R.id.list_lib);

        textView_name = findViewById(R.id.NOMBRE_LIVRO);

        textView_address = findViewById(R.id.address_LIVRO);

        textView_openTime = findViewById(R.id.openTime_LIVRO);

        textView_closeTime = findViewById(R.id.closeTime_LIVRO);

        textView_openDays = findViewById(R.id.openDays_LIVRO);

        librarybook=new Library();
        // criar um adaptador para a List View, adapter é um intermediario entre os dados e a List View
        adapter=new ListViewAdapterBooks(getApplicationContext(),R.layout.list_item,librarybook.getBooks());
        book_list.setAdapter(adapter);

        // Carregar os livros da biblioteca selecionada
        loadBooksFromOneLibrary(ID_OF_LIB);

        MoreInfoOboutThisLibrary(ID_OF_LIB);
        // Listener para a barra de busca de livros
        barforbooks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().trim();
                if (searchText.isEmpty()) {
                    // Caso o texto de busca esteja vazio, recarrega todos os livros da biblioteca selecionada
                    loadBooksFromOneLibrary(ID_OF_LIB);
                } else {
                    // Realiza a busca por nome de livro
                    searchBookByName(searchText);
                }
            }

        });

        book_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =  new Intent(ListOfBooksOfOneLibrary.this, InfBookActivity.class);
                Book b = librarybook.getBooks().get(i);
                intent.putExtra("ISBN", b.getIsbn());
                startActivity(intent);
            }
        });


    }

    private void loadBooksFromOneLibrary(final String ID_OF_LIB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    librarybook = RequestService.getBooksFromONELibrary(Utils.FIND_BOOKS_FROM_ONE_LIBRARY_0 + ID_OF_LIB + Utils.FIND_BOOKS_FROM_ONE_LIBRARY_1 + 70);
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
        }).start();
    }
    private void searchBookByName(final String searchText) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Book> searchResults = new ArrayList<>();
                    // Iterar sobre a lista original de livros
                    for (Book book : librarybook.getBooks()) {
                        if (book.getName().toLowerCase().contains(searchText.toLowerCase())) {
                            searchResults.add(book);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            books.clear();
                            adapter.setItems(searchResults);
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void MoreInfoOboutThisLibrary(final String libraryID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Library libraryInfo = RequestService.getLibraryInfo("http://193.136.62.24/v1/library/" + libraryID);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Exibir as informações da biblioteca nos TextViews
                            textView_name.setText(libraryInfo.getName());
                            textView_address.setText("Address: " + libraryInfo.getAddress());
                            textView_openTime.setText("Open Time: " + libraryInfo.getOpenTime());
                            textView_closeTime.setText("Close Time: " + libraryInfo.getCloseTime());
                            textView_openDays.setText("Open Days: " + libraryInfo.getOpenDays());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
