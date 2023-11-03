package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.dssmv_projectdroid.R;
import model.Book;
import model.Reviewer;

import java.util.List;

public class ListViewAdapterReviewsOfBook extends BaseAdapter {


    Context context;
    int layout_id;
    List<Reviewer> items;

    public ListViewAdapterReviewsOfBook(final Context context, final int layout_id, final List<Reviewer> items) {
        super();
        this.context=context;
        this.layout_id=layout_id;
        this.items = items;
    }
    public void setItems(final List<Reviewer> items){
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

        String reviewer = ((Reviewer)getItem(position)).getReviewer();
        String review = ((Reviewer)getItem(position)).getReview();
        String createdDate = ((Reviewer)getItem(position)).getCreatedDate();
        boolean recommended = ((Reviewer)getItem(position)).isRecommended();


        LayoutInflater inflater =LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.layout_id,parent,false);

        TextView textName =(TextView) convertView.findViewById(R.id.NAME_REVIEWER);
        TextView textName1 =(TextView) convertView.findViewById(R.id.TXT_createdDate);
        TextView textName2 =(TextView) convertView.findViewById(R.id.TXT_review);
        TextView textName3 =(TextView) convertView.findViewById(R.id.TXT_recommended);


        textName.setText(reviewer);
        if(!recommended) {
            textName1.setText("I recommend: NO");
        }
        else {
            textName1.setText("I recommend: YES");
        }


        textName2.setText(review);
        textName3.setText("DATA: "+createdDate);

        return convertView;
    }
}
