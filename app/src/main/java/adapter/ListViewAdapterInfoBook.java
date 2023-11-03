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

public class ListViewAdapterInfoBook extends BaseAdapter {

    Context context;
    int layout_id1;
    List<String> items;

    public ListViewAdapterInfoBook(Context context, int layout_id, List<String> items) {
        this.context = context;
        this.layout_id1 = layout_id;
        this.items = items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size() / 2; // Dividimos por 2, pois queremos exibir dois itens por linha
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layout_id1, parent, false);
        }

        // Posição do primeiro item na linha
        int firstItemPosition = position * 2;

        // Posição do segundo item na linha
        int secondItemPosition = firstItemPosition + 1;

        TextView textName1 = convertView.findViewById(R.id.NAME_OF_INFO);
        TextView textOpenTime1 = convertView.findViewById(R.id.INFO_IT_SELF);

        // Obtém os dois itens da lista para exibição
        String firstItem = items.get(firstItemPosition);
        String secondItem = items.get(secondItemPosition);

        // Define os textos nos elementos de layout
        textName1.setText(firstItem);
        textOpenTime1.setText(secondItem);

        // Alterne as cores de fundo para cada linha da lista
        int backgroundColor = position % 2 == 0 ? R.color.even_item_background : R.color.odd_item_background;
        convertView.setBackgroundResource(backgroundColor);

        return convertView;
    }
}
