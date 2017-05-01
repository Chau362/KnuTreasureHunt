package pearsistent.knutreasurehunt;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
//Edited by Bogyu 5.1

public class Tab2_admin extends Fragment implements View.OnClickListener {
    private Intent intent;
    private Button Point;
    private Button Selfie;
    private DatabaseReference mDatabase;

    public Tab2_admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab2_admin, container, false);
        Point = (Button)rootView.findViewById(R.id.changePoint);
        Point.setOnClickListener(this);

        Selfie = (Button)rootView.findViewById(R.id.show_selfie);
        Selfie.setOnClickListener(this);

        intent = new Intent(getContext(), Progress.class);
        // Inflate the layout for this fragment


    final ListView listView = (ListView) rootView.findViewById(R.id.teamListAdmin);
    final ArrayList<Team> teamList = new ArrayList<>();

    mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");

        mDatabase.child("Team").addValueEventListener(new ValueEventListener(){
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            teamList.clear();
            // Get Item data value
            for(DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                Team team = tempSnapshot.getValue(Team.class);

                teamList.add(team);
            }
            //when Tab2 work make a list
            if(getActivity()!=null) {
                //Set Item listview
                makeListView(listView, teamList);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i("Error","Loading data from teamMember");
        }
    });

        return rootView;
    }


    public void makeListView(ListView listView, final ArrayList<Team> teamList) {
        TeamListAdapter adapter = new TeamListAdapter(this.getActivity().getApplicationContext(),R.layout.teamview, teamList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.changePoint)
            startActivity(intent);
        else if(view.getId() == R.id.show_selfie){
            Fragment fragment = new CreatePDF();
            Log.i("PDF","1");
            Bundle bundle = new Bundle(1);
            fragment.setArguments(bundle);
        }
    }
}
