package pearsistent.knutreasurehunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */


class TeamListAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<Team> teamList;
    LayoutInflater inf;
    public TeamListAdapter(Context context, int layout, ArrayList<Team> teamList) {
        this.context = context;
        this.layout = layout;
        this.teamList = teamList;
        this.inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() { // 총 데이터의 개수를 리턴
        return teamList.size();
    }
    @Override
    public Object getItem(int position) { // 해당번째의 데이터 값
        return teamList.get(position);
    }
    @Override
    public long getItemId(int position) { // 해당번째의 고유한 id 값
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inf.inflate(layout, null);

        TextView teamName = (TextView) convertView.findViewById(R.id.teamName);
        TextView teamPoint = (TextView) convertView.findViewById(R.id.teamPoint);
        TextView teamRank = (TextView) convertView.findViewById(R.id.rank);

        //set team data decrease order
        Team team = teamList.get(getCount() - 1 - position);

        teamRank.setText(String.valueOf(position+1).toString());
        teamName.setText(team.getTeamName());
        teamPoint.setText(team.getTeamPoint() + " pts");


        return convertView;
    }
}
