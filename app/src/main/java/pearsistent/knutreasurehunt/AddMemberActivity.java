package pearsistent.knutreasurehunt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// last coder : seulki, 2017.04.28

public class AddMemberActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ListAdapter adapter;
    private String teamName;
    private ArrayList<TeamMember> teamMembers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Button addMemberBtn = (Button) this.findViewById(R.id.addmemberbutton);
        final ListView listView = (ListView) this.findViewById(R.id.memberlist);
        teamMembers = new ArrayList<>();
        Intent intent = getIntent();
        teamName = intent.getStringExtra("TEAM_NAME");


        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");
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

        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LinearLayout popupLayout = (LinearLayout) v.inflate(AddMemberActivity.this,R.layout.popup,null);
                //popup
                new AlertDialog.Builder(AddMemberActivity.this)
                        .setTitle("Add Member Name")
                        .setView(popupLayout)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText newMemberName = (EditText) popupLayout.findViewById(R.id.newMemberName);

                                //Log.i("inputName",changeName.getText().toString()+"  ");
                                TeamMember updateMember = new TeamMember(newMemberName.getText().toString(), teamMembers.get(0).getUserId());
                                teamMembers.add(updateMember);
                                mDatabase.child("Team").child(teamName).child("teamMembers").setValue(teamMembers);
                            }
                        }).show();
            }
        });


    }

    public void makeListView(ListView listView, final ArrayList<TeamMember> teamList) {
        MemberListAdapter adapter = new MemberListAdapter(this,R.layout.memberview,teamList,teamName);
        listView.setAdapter(adapter);
    }

}
