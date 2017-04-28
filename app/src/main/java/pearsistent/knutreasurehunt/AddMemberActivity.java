package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

// last coder : seulki, 2017.04.28

public class AddMemberActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ListAdapter adapter;
    private String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        final ListView listView = (ListView) this.findViewById(R.id.memberlist);
        final ArrayList<TeamMember> teamMembers = new ArrayList<>();
        Intent intent = getIntent();
        teamName = intent.getStringExtra("TEAM_NAME");

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://treasurehunt-5d55f.appspot.com");


        mDatabase.child("Team").child(teamName).child("teamMembers").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teamMembers.clear();
                // Get Item data value
                for(DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                    TeamMember teamMember = tempSnapshot.getValue(TeamMember.class);

                    teamMembers.add(teamMember);
                }
                //Set Item listview
                makeListView(listView,teamMembers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Error","Loading data from teamMember");
            }
        });


        /*TeamMember member1 = new TeamMember();
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
        arr.add(member7);*/


    }

    public void makeListView(ListView listView, final ArrayList<TeamMember> teamList) {
        MemberListAdapter adapter = new MemberListAdapter(this.getApplicationContext(),R.layout.memberview,teamList,teamName);
        listView.setAdapter(adapter);
    }

}
