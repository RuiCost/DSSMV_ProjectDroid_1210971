package com.example.dssmv_projectdroid;

import adapter.ListViewAdapterBooks;
import adapter.ListViewAdapterInfoBook;
import adapter.ListViewAdapterToLovedBooks;
import adapter.MyDAtabaseHelper;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import model.Book;
import service.RequestService;
import android.view.ContextMenu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class LikedBooksActivity extends AppCompatActivity {
    MyDAtabaseHelper myDB;

    List<String> lista_amor = new ArrayList<>();
    ListView list;
    ListViewAdapterToLovedBooks adapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_for_liked_books);
        list = findViewById(R.id.list_book);

        adapter = new ListViewAdapterToLovedBooks(this, R.layout.list_item, lista_amor);
        list.setAdapter(adapter);
        myDB = new MyDAtabaseHelper(this);

        List<String> bookTitles = myDB.getAllBookTitles();

        getBooksFromMyDB(bookTitles);

        list = findViewById(R.id.list_book);

        registerForContextMenu(list); // Move this line here

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Chamado quando o usuário desliza para cima para atualizar
                // Coloque aqui a lógica de atualização dos dados, caso necessário

                // Por exemplo, você pode recarregar os livros da sua base de dados aqui
                List<String> bookTitles = myDB.getAllBookTitles();
                getBooksFromMyDB(bookTitles);

                // Ao finalizar a atualização, chame setRefreshing(false) para indicar que a ação de atualização terminou.
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String bookISBN = myDB.getAllBookISBNS().get(i); // Obtém o ISBN do livro selecionado
                Intent intent = new Intent(LikedBooksActivity.this, InfBookActivity.class);
                intent.putExtra("ISBN", bookISBN); // Coloca o ISBN no putExtra
                startActivity(intent);
            }
        });


    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu_main,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position; // Obtem a posição do item selecionado

        switch (item.getItemId()) {
            case R.id.delete:
                List<String> bookISBNs = myDB.getAllBookISBNS();
                String bookISBNToDelete = bookISBNs.get(itemPosition); // Obtem o isbn do livro selecionado
                deleteBookFromDB(bookISBNToDelete); // Remove o livro da base de dados
                adapter.notifyDataSetChanged(); // Atualiza a ListView
                myDB.updateLikeStatus(bookISBNToDelete,"0");
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.edit:
                Toast.makeText(this, "Edits", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void deleteBookFromDB(String bookISBN) {
        myDB.deleteBookByISBN(bookISBN);

        List<String> bookTitles = myDB.getAllBookTitles();
        adapter.setItems(bookTitles);
        adapter.notifyDataSetChanged();
    }

    private void getBooksFromMyDB(List<String> bookTitles) {
        new Thread() {
            public void run() {
                try {

                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {

                            adapter.setItems(bookTitles);
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
        adapter.notifyDataSetChanged();

        Log.i("MAINACTIVITY","onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.notifyDataSetChanged();

        Log.i("MAINACTIVITY","onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

    }
}
