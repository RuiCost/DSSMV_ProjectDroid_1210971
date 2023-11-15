package adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDAtabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "myBox.db";
    private static final Integer DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_library";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "book_title";
    private static final String COLUMN_ISBN = "book_ISBN";



    public MyDAtabaseHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COLUMN_TITLE + " TEXT, " +
                        COLUMN_ISBN + " TEXT);";


        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }



    public void addBook(String title, String isbn) {
        SQLiteDatabase db = this.getWritableDatabase(); // Permite a escrita no banco de dados
        ContentValues cv = new ContentValues(); // Permite inserir dados

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_ISBN, isbn);


        long result = db.insert(TABLE_NAME, null, cv); // inserindo os valores armazenados no objeto ContentValues na tabela do banco de dados
        // e retorna -1 caso falhe e se der retorn o numero da linha inserida na tabela
        if(result == -1){
            Toast.makeText(context,"FAILED",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"Added book to favorites",Toast.LENGTH_SHORT).show();
        }
    }

    public List<String> getAllBookTitles() {

        List<String> bookTitles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();// Permite a leitura do banco de dados

        String[] projection = {COLUMN_TITLE};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null); // consulta na tabela sem cláusulas WHERE, GROUP BY, HAVING, ORDER BY, ou LIMIT


        //verificar se o cursor é nullo e se pode mover para a primeira linha
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String bookTitle = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));// Busca
                //obtem info do titlo em cada linha
                bookTitles.add(bookTitle);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return bookTitles;

    }

    public List<String> getAllBookISBNS() {
        List<String> bookISBNs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {COLUMN_ISBN};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String bookISBN = cursor.getString(cursor.getColumnIndex(COLUMN_ISBN));
                bookISBNs.add(bookISBN);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return bookISBNs;
    }

    public void deleteBookByISBN(String isbn) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ISBN + "=?", new String[]{isbn}); //ilimina linha com o isbn associado
        Toast.makeText(context,"Removed from favorites",Toast.LENGTH_SHORT).show();
        db.close();
    }

    public boolean checkISBNExists(String isbn) {
        SQLiteDatabase db = this.getReadableDatabase();

        // colunas a ser retornadas
        String[] projection = {COLUMN_ISBN};
        // selecionar registros onde o valor da coluna ISBN seja igual a um valor específico
        String selection = COLUMN_ISBN + " = ?";
        //isbn a procurar
        String[] selectionArgs = {isbn};

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        boolean exists = (cursor != null && cursor.getCount() > 0);

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return exists;
    }



}
