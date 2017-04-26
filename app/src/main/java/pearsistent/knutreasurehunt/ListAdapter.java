package pearsistent.knutreasurehunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */

class ListAdapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<Item> itemList;
    LayoutInflater inf;

    public ListAdapter(Context context, int layout, ArrayList<Item> itemList) {
        this.context = context;
        this.layout = layout;
        this.itemList = itemList;
        this.inf = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { // 총 데이터의 개수를 리턴
        return itemList.size();
    }

    @Override
    public Object getItem(int position) { // 해당번째의 데이터 값
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) { // 해당번째의 고유한 id 값
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inf.inflate(layout, null);

        ImageView photo = (ImageView)convertView.findViewById(R.id.imageView);
        TextView title = (TextView) convertView.findViewById(R.id.textView);

        final Item item = itemList.get(position);

        photo.setImageResource(item.getImage_i());
        //Glide.with(this.context).using(new FirebaseImageLoader()).load().into(photo);
        title.setText(item.getName());


        return convertView;
    }
}
