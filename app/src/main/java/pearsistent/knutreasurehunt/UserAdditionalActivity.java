package pearsistent.knutreasurehunt;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserAdditionalActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserAdditionalActivity#newInstance} factory method to
 * create an instance of this fragment.
 */

// last coder : seulki, 2017.04.29
public class UserAdditionalActivity extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MapView mapView;
    private LocationManager locationManager;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private DatabaseReference mDatabase;


    public UserAdditionalActivity() {
        // Required empty public constructor
    }

    public static UserAdditionalActivity newInstance(String param1, String param2) {
        UserAdditionalActivity fragment = new UserAdditionalActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab2,container,false);


        final ListView listView = (ListView) v.findViewById(R.id.teamList);
        final ArrayList<Team> teamList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");

        mDatabase.child("Team").orderByChild("teamPoint").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teamList.clear();
                // Get Item data value
                for (DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                    Team team = tempSnapshot.getValue(Team.class);

                    teamList.add(team);
                }
                //when Tab2 work make a list
                if (getActivity() != null) {
                    //Set Item listview
                    makeListView(listView, teamList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Error", "Loading data from teamMember");
            }
        });

        return v;
    }

    public void makeListView(ListView listView, final ArrayList<Team> teamList) {
        TeamListAdapter adapter = new TeamListAdapter(this.getActivity().getApplicationContext(), R.layout.teamview, teamList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
