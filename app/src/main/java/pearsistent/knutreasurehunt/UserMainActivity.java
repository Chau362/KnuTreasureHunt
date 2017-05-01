package pearsistent.knutreasurehunt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import static pearsistent.knutreasurehunt.R.id.textView;
//import static pearsistent.knutreasurehunt.R.id.itemList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserMainActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserMainActivity #newInstance} factory method to
 * create an instance of this fragment.
 */

// last coder : seulki, 2017.03.28

public class UserMainActivity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mRecyclerView;
    private CardListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ImageButton addMemberBtn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView timerView;
    private DatabaseReference mDatabase;
    private String teamName=null;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ListAdapter adapter;
    ImageView imageview;
    Item item;
    private boolean uploadFlag = false;
    private String itemName;
    private int itemPoint;
    private Item selectItem;
    private Item initailItem;
    private String itemKey;
    private int checkItemNum = 0;

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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String realTime = intent.getStringExtra("TIME");
            timerView.setText(realTime);

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("UserMain","come");
        View v = inflater.inflate(R.layout.fragment_tab1, container,false);


        mRecyclerView = (RecyclerView) v.findViewById(R.id.userItemList);
        addMemberBtn = (ImageButton) v.findViewById(R.id.addMember);
        timerView = (TextView)v.findViewById(R.id.timerView);

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(TimerService.BROADCAST_ACTION));


        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://treasurehunt-5d55f.appspot.com");

        final ArrayList<Item> getList = new ArrayList<>();

        addMemberBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(getContext(), AddMemberActivity.class);
                intent.putExtra("TEAM_NAME",teamName);
                startActivity(intent);
            }

        });

        mDatabase.child("Team").child(teamName).child("itemList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemKey  = String.valueOf(dataSnapshot.getChildrenCount());
                initailItem = dataSnapshot.child("0").getValue(Item.class);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                        //Log.i("cheeee",item.getName());
                    }
                }
                //Set Item listview
                makeListView(getList);
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

    public void makeListView(final ArrayList<Item> itemList){
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CardListAdapter(itemList, teamName);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        final GestureDetector gestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(),e.getY());
                int position = rv.getChildAdapterPosition(childView);

                if(childView !=null && gestureDetector.onTouchEvent(e)) {

                    TextView textview = (TextView) childView.findViewById(R.id.cardTitle);
                    selectItem = (Item) mAdapter.getItem(position);

                    itemName = textview.getText().toString();
                    imageview = (ImageView) childView.findViewById(R.id.cardImage);

                    Intent i = new Intent(getContext(), ObjectDetailActivity.class);
                    i.putExtra("ITEM_POINT",selectItem.getPoints());
                    i.putExtra("PATH_TO_SAVE", teamName + "/" + itemName + ".jpg");
                    startActivityForResult(i, 1);

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    //if user take a selfie, imageview will be update. if not then it will not be change.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1){
            if(resultCode==1 && data.getExtras().getInt("State")==1){

                if(itemKey.equals("1") && initailItem.getName()==null){
                    itemKey = "0";
                    //checkItemNum ++;
                }

                mDatabase.child("Team").child(teamName).child("itemList").child(itemKey).setValue(selectItem);
                //update cache
                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(findImageFile(itemName+".jpg"))
                        .error(R.drawable.marker)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageview);
            }
            else if(resultCode==1 && data.getExtras().getInt("State")==0){

                //update cache
                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(findImageFile(itemName+".jpg"))
                        .error(R.drawable.marker)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageview);
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