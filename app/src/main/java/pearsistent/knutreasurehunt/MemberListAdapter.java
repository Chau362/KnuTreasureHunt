package pearsistent.knutreasurehunt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Zzeulki on 2017. 3. 30..
 */

public class MemberListAdapter extends BaseAdapter{
    Context context;
    int layout;
    ArrayList<TeamMember> teamMemberList;
    LayoutInflater inf;
    private DatabaseReference mDatabase;
    private String teamName;



    public MemberListAdapter(Context context, int layout, ArrayList<TeamMember> teamList, String teamName) {
        this.context = context;
        this.layout = layout;
        this.teamMemberList = teamList;
        this.teamName = teamName;
        this.inf = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");
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

        final View tempView = convertView;
        final TextView memberName = (TextView) convertView.findViewById(R.id.membername);
        Spinner spinner = (Spinner) convertView.findViewById(R.id.listspinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(convertView.getContext(),R.array.data_spinner,android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        TeamMember teamMember = teamMemberList.get(position);

        memberName.setText(teamMember.getMemberName());
        Log.i("MemberName",memberName.getText().toString());

        //memberName.setOnClickListener(this);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Delete team member
                if(position==1){
                    Log.i("Spinner","1");
                    mDatabase.child("Team").child(teamName).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.i("Team Children"," "+dataSnapshot.getChildrenCount());

                            for (final DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                                final TeamMember temp = tempSnapshot.getValue(TeamMember.class);
                                //delete teamMember
                                if (memberName.getText().equals(temp.getMemberName())) {
                                    //popup
                                    new AlertDialog.Builder(context)
                                            .setTitle("Delete Member")
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    DatabaseReference updateDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/Team" + "/" + teamName + "/teamMembers/" + tempSnapshot.getKey());
                                                    updateDatabase.removeValue();
                                                }
                                            }).show();
                                    break;
                                }
                            }
                        }

                        //update team Member key
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            ArrayList<TeamMember> teamMembers = new ArrayList<TeamMember>();

                            for (DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                                TeamMember temp = tempSnapshot.getValue(TeamMember.class);
                                teamMembers.add(temp);
                            }
                            DatabaseReference updateDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/Team" + "/" + teamName + "/teamMembers");
                            updateDatabase.setValue(teamMembers);
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                //modify member
                else if(position==2) {
                    Log.i("Spinner", "2");

                            mDatabase.child("Team").child(teamName).child("teamMembers").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get Item data value
                                    for (DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                                        final TeamMember temp = tempSnapshot.getValue(TeamMember.class);
                                        //delete teamMember
                                        if (memberName.getText().equals(temp.getMemberName())) {
                                            final LinearLayout popupLayout = (LinearLayout) tempView.inflate(context,R.layout.popup,null);
                                            final DatabaseReference updateDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/Team" + "/" + teamName + "/teamMembers/" + tempSnapshot.getKey());

                                            //popup
                                            new AlertDialog.Builder(context)
                                                    .setTitle("Modify Name")
                                                    .setView(popupLayout)
                                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            EditText changeName = (EditText) popupLayout.findViewById(R.id.newMemberName);

                                                            //Log.i("inputName",changeName.getText().toString()+"  ");
                                                            TeamMember updateMember = new TeamMember(changeName.getText().toString(), temp.getUserId());

                                                            Map<String, Object> updateValues = updateMember.toMap();
                                                            updateDatabase.updateChildren(updateValues);
                                                        }
                                                    }).show();

                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.i("Error", "Loading data from teamMember");
                                }
                            });
                            /*    }
                            }).show();*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }

   /* @Override
    public void onClick(final View v) {
        new AlertDialog.Builder(this.context)
                .setTitle("Modify")
                .setMessage("Which do you want to work?")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AlertDialog.Builder(context)
                                .setTitle("Delete")
                                .setMessage("Are you sure to delete this team member?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDatabase.child("Team").child(teamName).child("teamMembers").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                // Get Item data value
                                                for (DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                                                    TeamMember temp = tempSnapshot.getValue(TeamMember.class);
                                                    //delete teamMember
                                                    if (teamMember.getMemberName().equals(temp.getMemberName())) {
                                                        DatabaseReference updateDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/Team" + "/" + teamName + "/teamMembers/" + tempSnapshot.getKey());
                                                        updateDatabase.removeValue();
                                                        break;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.i("Error", "Loading data from teamMember");
                                            }
                                        });
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
                    }
                }).setPositiveButton("Modify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //View layout = inf.inflate(R.layout.popup, findViewById(R.id.popup));
                new AlertDialog.Builder(context)
                        .setView(R.layout.popup)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText text = (EditText) v.findViewById(R.id.newMemberName);
                        final String newTeamName = text.getText().toString();

                        mDatabase.child("Team").child(teamName).child("teamMembers").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get Item data value
                                for (DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                                    TeamMember temp = tempSnapshot.getValue(TeamMember.class);
                                    //delete teamMember
                                    if (teamMember.getMemberName().equals(temp.getMemberName())) {
                                        DatabaseReference updateDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/Team" + "/" + teamName + "/teamMembers/" + tempSnapshot.getKey());
                                        TeamMember updateMember = new TeamMember(newTeamName, temp.getUserId());
                                        Map<String, Object> updateValues = updateMember.toMap();
                                        updateDatabase.updateChildren(updateValues);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.i("Error", "Loading data from teamMember");
                            }
                        });
                    }
                }).create().show();
            }
        }).show();

    }*/

}


