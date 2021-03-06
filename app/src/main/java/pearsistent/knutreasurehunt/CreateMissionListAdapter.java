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

//It's List for Admin Screen
public class CreateMissionListAdapter extends BaseAdapter{
    Context context;
    int layout;
    ArrayList<Item> choicedList;
    LayoutInflater inf;
    boolean[] checkBoxState;

    public CreateMissionListAdapter(Context context, int layout, ArrayList<Item> choicedList) {
        this.context = context;
        this.layout = layout;
        this.choicedList = choicedList;
        this.inf = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkBoxState = new boolean[choicedList.size()];
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

        final Item item = choicedList.get(position);

        //checkBox listener in Listview
        checkBox.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               if(((CheckBox)v).isChecked()) {
                   item.getCheckBox().setChecked(true);
               }else{
                   item.getCheckBox().setChecked(false);
               }
           }
        });

        objectName.setText(item.getName());
        checkBox = item.getCheckBox();

        return convertView;
    }
}
