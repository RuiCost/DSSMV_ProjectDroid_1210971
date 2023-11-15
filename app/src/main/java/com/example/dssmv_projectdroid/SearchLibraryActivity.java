package com.example.dssmv_projectdroid;

import adapter.ListViewAdapterBooks;
import adapter.ListViewAdapterLibrarys;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import dto.BookDTO;
import dto.LibraryDTO;
import helper.Utils;
import model.Book;
import model.Library;
import network.HttpOperation;
import service.RequestService;

import java.util.ArrayList;
import java.util.List;import java.net.HttpURLConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class SearchLibraryActivity extends AppCompatActivity {
    Library librarybook=null;
    EditText bookname;
    ListView book_list;

    ListViewAdapterLibrarys adapter;

    Button searchbook;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchlivrary);

        book_list = findViewById(R.id.list_lib);

        // Inicialize o adaptador antes de usá-lo
        adapter = new ListViewAdapterLibrarys(getApplicationContext(), R.layout.list_of_libs, new ArrayList<>());
        book_list.setAdapter(adapter);

        // Chamar o método getLibraryBook() e exibir a lista de bibliotecas retornada pela API
        loadAllLibraries();

        bookname = findViewById(R.id.LIB_BARRA);

        bookname.addTextChangedListener(new TextWatcher() {
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
                    // Se a barra de pesquisa estiver vazia, exibir novamente a lista de todas as bibliotecas
                    loadAllLibraries();
                } else {
                    // Caso contrário, filtrar a lista com base no termo de pesquisa
                    searchLibraryByName(searchText);
                }
            }
        });

        book_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =  new Intent(SearchLibraryActivity.this, ListOfBooksOfOneLibrary.class);
                Library selectedLibrary = (Library) adapter.getItem(i);
                intent.putExtra("SELECTED_LIBRARY", selectedLibrary.getId());
                startActivity(intent);
            }
        });

        // PARA CENA DE PRESSIONAR
        registerForContextMenu(book_list);


    }
    // PARA CENA DE PRESSIONAR POR UM TEMPO
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu_of_librarys_main,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position; // Obtem a posição do item selecionado

        switch (item.getItemId()) {
            case R.id.METERLIBRO:

                Intent intent1 =  new Intent(SearchLibraryActivity.this, AddBookActivity.class);
                Library selectedLibrary = (Library) adapter.getItem(itemPosition);
                intent1.putExtra("SELECTED_LIBRARY", selectedLibrary.getId());
                startActivity(intent1);
                return true;

            case R.id.DELETE:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Tem certeza que deseja excluir esta biblioteca?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Library selectedLibrary = (Library) adapter.getItem(itemPosition);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    HttpOperation.delete("http://193.136.62.24/v1/library/"+selectedLibrary.getId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }




    private void loadAllLibraries() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Library> allLibraries = getLibraryBook();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setItems(allLibraries);
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void searchLibraryByName(final String searchText) {
        new Thread() {
            public void run() {
                try {
                    final List<Library> searchResults = new ArrayList<>();

                    // Iterar sobre a lista original de bibliotecas e adicionar as correspondentes ao termo de pesquisa na lista de resultados
                    for (Library library : getLibraryBook()) {
                        if (library.getName().toLowerCase().contains(searchText.toLowerCase())) {
                            searchResults.add(library);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setItems(searchResults);
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private List<Library> getLibraryBook() {
        List<Library> libraries = new ArrayList<>();
        try {
            libraries = RequestService.getLibrarys(Utils.FIND_LIBRARYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return libraries;
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
