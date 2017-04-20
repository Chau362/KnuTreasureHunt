package pearsistent.knutreasurehunt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

// last coder : seulki, 2017.03.30

public class AddMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        ListView listView = (ListView) this.findViewById(R.id.memberlist);

        ArrayList<TeamMember> arr = new ArrayList<>();


        TeamMember member1 = new TeamMember();
        member1.setMemberName("Tim");
        arr.add(member1);

        TeamMember member2 = new TeamMember();
        member2.setMemberName("Henna");
        arr.add(member2);

        TeamMember member3 = new TeamMember();
        member3.setMemberName("David");
        arr.add(member3);

        TeamMember member4 = new TeamMember();
        member4.setMemberName("Bogyu");
        arr.add(member4);

        TeamMember member5 = new TeamMember();
        member5.setMemberName("Chau");
        arr.add(member5);

        TeamMember member6 = new TeamMember();
        member6.setMemberName("Song");
        arr.add(member6);

        TeamMember member7 = new TeamMember();
        member7.setMemberName("SeulKi");
        arr.add(member7);


        MemberListAdapter adapter = new MemberListAdapter(this.getApplicationContext(),R.layout.memberview,arr);
        listView.setAdapter(adapter);
    }
}
