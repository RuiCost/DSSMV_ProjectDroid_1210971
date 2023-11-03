package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.dssmv_projectdroid.R;
import model.Book;
import model.Library;

import java.util.List;

public class ListViewAdapterLibrarys extends BaseAdapter{


    Context context;
    int layout_id1;
    List<Library> items;

    public ListViewAdapterLibrarys(final Context context, final int layout_id, final List<Library> items) {
        super();
        this.context=context;
        this.layout_id1=layout_id;
        this.items = items;
    }
    public void setItems(final List<Library> items){
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public List<Library> getItems() {
        return items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Library library = (Library) getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layout_id1, parent, false);
        }

        TextView textName = convertView.findViewById(R.id.NAMELIB);
        TextView textOpenTime = convertView.findViewById(R.id.OPEN_TIME);
        TextView textCloseTime = convertView.findViewById(R.id.CLOSE_TIME);
        TextView textOpenDays = convertView.findViewById(R.id.OPEN_DAYS);

        textName.setText(library.getName());
        textOpenTime.setText("Open Time: " + library.getOpenTime());
        textCloseTime.setText("Close Time: " + library.getCloseTime());
        textOpenDays.setText("Open Days: " + library.getOpenDays());

        // Alterne as cores de fundo para cada item da lista
        int backgroundColor = position % 2 == 0 ? R.color.even_item_background : R.color.odd_item_background;
        convertView.setBackgroundResource(backgroundColor);

        return convertView;
    }

}