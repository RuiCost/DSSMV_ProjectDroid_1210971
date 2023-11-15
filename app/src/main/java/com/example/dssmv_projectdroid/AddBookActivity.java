package com.example.dssmv_projectdroid;

import adapter.ListViewAdapterBooks;
import adapter.SelectedBooksAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import model.Book;
import model.Library;
import service.RequestService;

import java.util.ArrayList;
import java.util.List;

public class AddBookActivity extends AppCompatActivity {


    EditText isbn;
    EditText libraryid;
    EditText stock;
    Button addbook;

    TextView textView;

    Library librarybook=null;
    EditText bookname;
    ListView book_list;

    ListViewAdapterBooks adapter;

    Button searchbook;
    TextView textView11;
    private ArrayList<Book> selectedBooks = new ArrayList<>();
    SelectedBooksAdapter selectedBooksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);

        isbn = findViewById(R.id.book2);

        stock = findViewById(R.id.book4);
        addbook = findViewById(R.id.addBookbook);

        searchbook=(Button) findViewById(R.id.searchbook);
        bookname =(EditText) findViewById(R.id.book);

        // As duas listas que vão ser usadas
        book_list=(ListView) findViewById(R.id.list_book);
        ListView selectedBooksListView = findViewById(R.id.list_selected_books);

        librarybook=new Library();
        adapter=new ListViewAdapterBooks(getApplicationContext(),R.layout.list_item,librarybook.getBooks());
        book_list.setAdapter(adapter);

        selectedBooksAdapter = new SelectedBooksAdapter(this, R.layout.list_item_selected, selectedBooks);
        selectedBooksListView.setAdapter(selectedBooksAdapter);

        String ID_LIB = getIntent().getStringExtra("SELECTED_LIBRARY");

        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isbn1 = isbn.getText().toString();
                String stock1 = stock.getText().toString();

                // Adicionar um libro á bibliotrca
                if (ID_LIB.length() > 0 && isbn1.length() > 12 && stock1.length() > 0) {
                    addBookToLibrary("http://193.136.62.24/v1/library/" + ID_LIB + "/book/" + isbn1, stock1);
                    Toast.makeText(AddBookActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                }

                // Adicionar os livros á bibliotrca
                for (Book selectedBook : selectedBooks) {
                    addBookToLibrary("http://193.136.62.24/v1/library/" + ID_LIB + "/book/" + selectedBook.getIsbn(), String.valueOf(selectedBook.getStock()));
                    Toast.makeText(AddBookActivity.this, "Books Added", Toast.LENGTH_SHORT).show();
                }

                // Deletar a lista de livros selecionados após adicionar à biblioteca
                selectedBooks.clear();
                selectedBooksAdapter.notifyDataSetChanged();
            }
        });

        // Para ver a info do livro
        book_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =  new Intent(AddBookActivity.this, InfBookActivity.class);
                Book b = librarybook.getBooks().get(i);

                intent.putExtra("ISBN", b.getIsbn());
                startActivity(intent);
                Log.i("AMAINHAAPP", b.toString()+ "--" + b.getName() +"--" + b.getIsbn());
            }
        });

        book_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Book selectedBook = librarybook.getBooks().get(i);

                // Criar o AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBookActivity.this);
                builder.setTitle("Set Stock");

                // Criar um EditText para user por stock_______
                final EditText input = new EditText(AddBookActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Por stock que se quer
                        String stockInput = input.getText().toString();

                        if (!stockInput.isEmpty()) {

                            int stockValue = Integer.parseInt(stockInput);
                            selectedBook.setStock(stockValue); // mete o respetivo stock
                            selectedBooks.add(selectedBook); // mete o libro correspondente a este stock
                            adapter.notifyDataSetChanged();// Atualiza a exibição na lista

                            Toast.makeText(AddBookActivity.this, "Stock set for selected book.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                return true;
            }
        });

        // Butao para procuarr os livros
        searchbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookName=bookname.getText().toString();
                if (bookName.length()>0) {
                    // Cena para procurar livros
                    getLibraryBook("http://193.136.62.24/v1/search?page=1&query="+bookName);

                }
            }
        });

        registerForContextMenu(selectedBooksListView);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu_books_alocated,menu);
    }


    // REMOVER DA LISTA DE STOCK
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position; // Obtem a posição do item selecionado

        switch (item.getItemId()) {
            case R.id.REMOVE_NOWWWW:
                    Book bookToRemove = selectedBooks.get(itemPosition);
                    selectedBooksAdapter.remove(bookToRemove);
                    selectedBooksAdapter.notifyDataSetChanged();

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


    // Cena para procurar livros
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
    private void addBookToLibrary(String urlStr,String stock) {
        new Thread(){
            public void run(){
                try {

                    RequestService.addBookToLibrary(urlStr, stock);

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
