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
import android.widget.Toast;

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

//last coder : seulki, 2017.05.01
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

        //set spinner
        Spinner spinner = (Spinner) convertView.findViewById(R.id.listspinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(convertView.getContext(),R.array.data_spinner,android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        TeamMember teamMember = teamMemberList.get(position);

        memberName.setText(teamMember.getMemberName());


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

                            final long memberCount = dataSnapshot.getChildrenCount();
                            //get teamMember from DB
                            for (final DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                                final TeamMember temp = tempSnapshot.getValue(TeamMember.class);
                                //delete teamMember
                                if (memberName.getText().equals(temp.getMemberName())) {
                                    //popup about delete
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
                                                    if(memberCount!=1) {
                                                        DatabaseReference updateDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/Team" + "/" + teamName + "/teamMembers/" + tempSnapshot.getKey());
                                                        updateDatabase.removeValue();
                                                    }else{
                                                         Toast.makeText(context, "Member must have one more", Toast.LENGTH_SHORT).show();
                                                    }
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
                                    // Get teamMember data value
                                    for (DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                                        final TeamMember temp = tempSnapshot.getValue(TeamMember.class);
                                        //delete teamMember
                                        if (memberName.getText().equals(temp.getMemberName())) {

                                            final LinearLayout popupLayout = (LinearLayout) tempView.inflate(context,R.layout.popup,null);
                                            final DatabaseReference updateDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/Team" + "/" + teamName + "/teamMembers/" + tempSnapshot.getKey());

                                            //popup about modify
                                            new AlertDialog.Builder(context)
                                                    .setTitle("Modify Name")
                                                    .setView(popupLayout)
                                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                                    //update Member Name
                                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            EditText changeName = (EditText) popupLayout.findViewById(R.id.newMemberName);

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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }
}


