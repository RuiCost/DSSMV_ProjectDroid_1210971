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

        book_list=(ListView) findViewById(R.id.list_book);

        librarybook=new Library();

        adapter=new ListViewAdapterBooks(getApplicationContext(),R.layout.list_item,librarybook.getBooks());
        book_list.setAdapter(adapter);

        selectedBooksAdapter = new SelectedBooksAdapter(this, R.layout.list_item_selected, selectedBooks);
        ListView selectedBooksListView = findViewById(R.id.list_selected_books);
        selectedBooksListView.setAdapter(selectedBooksAdapter);

        String ID_LIB = getIntent().getStringExtra("SELECTED_LIBRARY");

        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isbn1 = isbn.getText().toString();
                String stock1 = stock.getText().toString();

                if (ID_LIB.length() > 0 && isbn1.length() > 12 && stock1.length() > 0) {
                    addBookToLibrary("http://193.136.62.24/v1/library/" + ID_LIB + "/book/" + isbn1, stock1);
                    Toast.makeText(AddBookActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                }

                // Criar uma lista temporária para os livros selecionados com seus estoques
                ArrayList<BookStockPair> booksToAdd = new ArrayList<>();
                for (Book selectedBook : selectedBooks) {
                    String selectedBookISBN = selectedBook.getIsbn();
                    int selectedBookStock = selectedBook.getStock();
                    booksToAdd.add(new BookStockPair(selectedBook, selectedBookStock));
                }

                // Adicionar os livros à biblioteca
                for (BookStockPair pair : booksToAdd) {
                    String selectedBookISBN = pair.getBook().getIsbn();
                    int selectedBookStock = pair.getStock();
                    addBookToLibrary("http://193.136.62.24/v1/library/" + ID_LIB + "/book/" + selectedBookISBN, String.valueOf(selectedBookStock));
                }

                // Limpar a lista de livros selecionados após adicionar à biblioteca
                selectedBooks.clear();
                selectedBooksAdapter.notifyDataSetChanged();
            }
        });


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
                // Handle long-press action (Select multiple books)
                Book selectedBook = librarybook.getBooks().get(i);

                // Create and configure the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBookActivity.this);
                builder.setTitle("Set Stock");

                // Create an EditText for the user to input the stock
                final EditText input = new EditText(AddBookActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Parse the input as an integer and set the stock for the selected book
                        String stockInput = input.getText().toString();
                        if (!stockInput.isEmpty()) {
                            int stockValue = Integer.parseInt(stockInput);
                            selectedBook.setStock(stockValue);
                            if (!selectedBooks.contains(selectedBook)) {
                                selectedBooks.add(selectedBook);
                            }
                            adapter.notifyDataSetChanged(); // Atualiza a exibição na lista
                            Toast.makeText(AddBookActivity.this, "Stock set for selected book.", Toast.LENGTH_SHORT).show();
                        }
                        if (!selectedBooks.contains(selectedBook)) {
                            selectedBooks.add(selectedBook);
                            selectedBooksAdapter.notifyDataSetChanged(); // Atualiza a lista de livros selecionados
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

                return true; // Returning true indicates that the long-click event is consumed and prevents the single-click event from firing.
            }
        });


        searchbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookName=bookname.getText().toString();
                if (bookName.length()>0) {
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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position; // Obtem a posição do item selecionado

        switch (item.getItemId()) {
            case R.id.REMOVE_NOWWWW:
                if (itemPosition >= 0 && itemPosition < selectedBooks.size()) {
                    Book bookToRemove = selectedBooks.get(itemPosition);
                    selectedBooksAdapter.remove(bookToRemove);
                    selectedBooksAdapter.notifyDataSetChanged();
                }
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void getLibraryBook(String urlStr){

        new Thread(){
            public void run(){
                try {

                    librarybook = RequestService.getLibraryBook(urlStr);
                    Book book=librarybook.getBooks().get(0);


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
    private static class BookStockPair {
        private Book book;
        private int stock;

        public BookStockPair(Book book, int stock) {
            this.book = book;
            this.stock = stock;
        }

        public Book getBook() {
            return book;
        }

        public int getStock() {
            return stock;
        }
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