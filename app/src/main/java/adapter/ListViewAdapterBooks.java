package adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.dssmv_projectdroid.R;
import model.Book;
import java.util.List;
public class ListViewAdapterBooks extends BaseAdapter{


    Context context;
    int layout_id;
    List<Book> items;

    public ListViewAdapterBooks(final Context context, final int layout_id, final List<Book> items) {
        super();
        this.context=context;
        this.layout_id=layout_id;
        this.items = items;
    }
    public void setItems(final List<Book> items){
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

    public View getView(int position, View convertView, ViewGroup parent){
        String name = ((Book)getItem(position)).getName();
        //usado para inflar o layout de um item de lista a partir de um arquivo XML
        LayoutInflater inflater =LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.layout_id,parent,false);

        TextView textName =(TextView) convertView.findViewById(R.id.NAMEBOOK);
        textName.setText(name);
        return convertView;
    }
}
