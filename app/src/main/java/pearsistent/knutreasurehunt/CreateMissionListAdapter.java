package pearsistent.knutreasurehunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zzeulki on 2017. 4. 21..
 */

public class CreateMissionListAdapter extends BaseAdapter{
    Context context;
    int layout;
    ArrayList<Item> choicedList;
    LayoutInflater inf;

    public CreateMissionListAdapter(Context context, int layout, ArrayList<Item> choicedList) {
        this.context = context;
        this.layout = layout;
        this.choicedList = choicedList;
        this.inf = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { // 총 데이터의 개수를 리턴
        return choicedList.size();
    }

    @Override
    public Object getItem(int position) { // 해당번째의 데이터 값
        return choicedList.get(position);
    }

    @Override
    public long getItemId(int position) { // 해당번째의 고유한 id 값
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inf.inflate(layout, null);

        TextView objectName = (TextView) convertView.findViewById(R.id.objectNameTextView);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.objectCheckBox);

        Item item = choicedList.get(position);


        //Log.i("objectName",mission.getObjectName());
        objectName.setText(item.getName());
        checkBox = item.getCheckBox();

        return convertView;
    }
}
