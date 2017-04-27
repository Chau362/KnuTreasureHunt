package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;
import static pearsistent.knutreasurehunt.R.id.imageView;
//import static pearsistent.knutreasurehunt.R.id.itemList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserMainActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserMainActivity #newInstance} factory method to
 * create an instance of this fragment.
 */

// last coder : seulki, 2017.04.27

public class UserMainActivity extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private DatabaseReference mDatabase;
    private String teamName=null;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ListAdapter adapter;
    ImageView imageview;
    Item item;


    public UserMainActivity() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        teamName = getArguments().getString("TEAM_NAME");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab1, container,false);
        final RecyclerView listView = (RecyclerView) v.findViewById(itemList);
        ImageButton addMember = (ImageButton) v.findViewById(R.id.imageButton3);

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://treasurehunt-5d55f.appspot.com");

        final ArrayList<Item> getList = new ArrayList<>();


        addMember.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(getContext(), AddMemberActivity.class);
                startActivity(intent);
            }

        });


        mDatabase.child("Items").addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getList.clear();
                // Get Item data value
                for(DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                    Item item = tempSnapshot.getValue(Item.class);

                    if(item.getChoice()) {
                        //Set item image Reference
                        item.setImageReference(findImageFile(item.getName()+".jpg"));
                        getList.add(item);
                    }
                }
                //Set Item listview
                makeListView(listView,getList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });


        return v;
    }

    private StorageReference findImageFile(String imageFileName) {

        if(teamName!=null){

            //can make a image file name
            StorageReference childRef = storageRef.child(teamName+"/"+imageFileName);
            return childRef;
        }

        return null;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void makeListView(final ListView listView, final ArrayList<Item> itemList){
        adapter = new ListAdapter(this.getContext(),R.layout.itemview,itemList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //clicked item
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Item> temp = new ArrayList<Item>();
                item = (Item) listView.getAdapter().getItem(position);

                Intent i = new Intent(getContext(),ObjectDetailActivity.class);
                i.putExtra("PATH_TO_SAVE",teamName+"/"+item.getName()+".jpg");
                startActivityForResult(i,1);

                imageview = (ImageView) view.findViewById(imageView);
            }

        });
    }

    //if user take a selfie, imageview will be update. if not then it will not be change.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1){
            if(resultCode==1){
                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(findImageFile(item.getName()+".jpg"))
                        .error(R.drawable.marker).into(imageview);
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
