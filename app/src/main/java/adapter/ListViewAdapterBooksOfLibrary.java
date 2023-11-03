package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dssmv_projectdroid.R;
import model.Book;

import java.util.List;

public class ListViewAdapterBooksOfLibrary extends BaseAdapter {

    private Context context;
    private int layoutId;
    private List<Book> books;

    public ListViewAdapterBooksOfLibrary(Context context, int layoutId, List<Book> books) {
        this.context = context;
        this.layoutId = layoutId;
        this.books = books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = books.get(position);
        String name = book.getName();

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutId, parent, false);
        }

        TextView textName = convertView.findViewById(R.id.NAMEBOOK);
        textName.setText(name);

        return convertView;
    }
}
