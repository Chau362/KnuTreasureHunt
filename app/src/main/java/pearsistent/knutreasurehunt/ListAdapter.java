package pearsistent.knutreasurehunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.util.ArrayList;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */

//Adapter for
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

        //Seulki : if loading has error then I will instead it to marker image in ImageView
        Glide.with(this.context)
                .using(new FirebaseImageLoader())
                .load(item.getImageReference())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.marker)
                .into(photo);

        title.setText(item.getName());

        return convertView;
    }
}
