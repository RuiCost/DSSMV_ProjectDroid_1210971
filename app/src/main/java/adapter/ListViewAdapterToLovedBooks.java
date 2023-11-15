package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.dssmv_projectdroid.R;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListViewAdapterToLovedBooks extends BaseAdapter {

    Context context;
    int layout_id1;
    List<String> items;

    public ListViewAdapterToLovedBooks(Context context, int layout_id, List<String> items) {
        this.context = context;
        this.layout_id1 = layout_id;
        this.items = items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return getItem(position);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layout_id1, parent, false);

        TextView textName1 = convertView.findViewById(R.id.NAMEBOOK);

        String item = items.get(position);

        textName1.setText(item);

        return convertView;
    }
}
