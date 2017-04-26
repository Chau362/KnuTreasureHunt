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
    Context context;     // 현재 화면의 제어권자
    int layout;              // 한행을 그려줄 layout
    List<Team> teamList;     // 다량의 데이터
    LayoutInflater inf; // 화면을 그려줄 때 필요
    public TeamListAdapter(Context context, int layout, ArrayList<Team> teamList) {
        this.context = context;
        this.layout = layout;
        this.teamList = teamList;
        this.inf = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
    @Override // 해당번째의 행에 내용을 셋팅(데이터와 레이아웃의 연결관계 정의)
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inf.inflate(layout, null);

        TextView teamName = (TextView) convertView.findViewById(R.id.teamName);
        TextView teamPoint = (TextView) convertView.findViewById(R.id.teamPoint);
        Team team = teamList.get(position);

        teamName.setText(team.getTeamName());
        teamPoint.setText(team.getTeamPoint()+" pts");

        return convertView;
    }
}
