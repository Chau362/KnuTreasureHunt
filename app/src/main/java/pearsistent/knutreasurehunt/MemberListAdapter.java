package pearsistent.knutreasurehunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zzeulki on 2017. 3. 30..
 */

public class MemberListAdapter extends BaseAdapter

    {
    Context context;
    int layout;
    ArrayList<TeamMember> teamMemberList;
    LayoutInflater inf;

    public MemberListAdapter(Context context, int layout, ArrayList<TeamMember> itemList) {
        this.context = context;
        this.layout = layout;
        this.teamMemberList = itemList;
        this.inf = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() { // 총 데이터의 개수를 리턴
        return teamMemberList.size();
    }

    @Override
    public Object getItem(int position) { // 해당번째의 데이터 값
        return teamMemberList.get(position);
    }

    @Override
    public long getItemId(int position) { // 해당번째의 고유한 id 값
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inf.inflate(layout, null);

        TextView memberName = (TextView) convertView.findViewById(R.id.membername);

        TeamMember teamMember = teamMemberList.get(position);

        memberName.setText(teamMember.memberName);

        return convertView;
    }
}


