package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.dssmv_projectdroid.R;
import model.Book;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SelectedBooksAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> selectedBooks;

    public SelectedBooksAdapter(Context context, int resource, ArrayList<Book> selectedBooks) {
        super(context, resource, selectedBooks);
        this.selectedBooks = selectedBooks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_selected, parent, false);
        }

        // Get the book at the specified position
        Book book = selectedBooks.get(position);

        // Set the book name and stock in the views
        TextView bookNameTextView = convertView.findViewById(R.id.text_book_name);
        TextView stockTextView = convertView.findViewById(R.id.text_stock);

        bookNameTextView.setText(book.getName());
        stockTextView.setText("Stock: " + book.getStock());

        return convertView;
    }
}
