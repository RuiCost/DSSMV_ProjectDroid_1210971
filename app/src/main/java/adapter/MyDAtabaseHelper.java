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
    private static final String DATABASE_NAME = "sacas.db";
    private static final Integer DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_library";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "book_title";
    private static final String COLUMN_ISBN = "book_ISBN";

    private static final String COLUMN_LIKE = "book_Like";

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
                        COLUMN_ISBN + " TEXT, " +
                        COLUMN_LIKE + " INTEGER);";


        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void addBook(String title, String isbn, String like) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_ISBN, isbn);
        cv.put(COLUMN_LIKE,like);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context,"FAILED",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"Added book to favorites",Toast.LENGTH_SHORT).show();

        }
    }

    public List<String> getAllBookTitles() {
        List<String> bookTitles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {COLUMN_TITLE};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String bookTitle = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
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
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
    public void deleteBookByISBN(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ISBN + "=?", new String[]{title});
        Toast.makeText(context,"Removed from favorites",Toast.LENGTH_SHORT).show();
        db.close();
    }

    public boolean checkISBNExists(String isbn) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ISBN};
        String selection = COLUMN_ISBN + " = ?";
        String[] selectionArgs = {isbn};

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        boolean exists = (cursor != null && cursor.getCount() > 0);

        if (cursor != null) {
            cursor.close();
        }
        if(exists){
            Toast.makeText(context,"Already have this book in favorites",Toast.LENGTH_SHORT).show();
        }
        db.close();
        return exists;
    }
    public void updateLikeStatus(String isbn, String likeStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LIKE,likeStatus);

        db.update(TABLE_NAME, cv, COLUMN_ISBN + "=?", new String[]{isbn});
        db.close();
    }
    @SuppressLint("Range")
    public String getLikeStatusByISBN(String isbn) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_LIKE};
        String selection = COLUMN_ISBN + " = ?";
        String[] selectionArgs = {isbn};

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        String likeStatus = "0"; // Valor padrão de "unliked"

        if (cursor != null && cursor.moveToFirst()) {
            likeStatus = cursor.getString(cursor.getColumnIndex(COLUMN_LIKE));
            cursor.close();
        }

        db.close();
        return likeStatus;
    }



}
