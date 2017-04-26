package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import static com.google.android.gms.wearable.DataMap.TAG;


/**
 * A simple {@link Fragment} subclass.
 */

//last coder : seulki, 2017.04.23
public class Tab1_admin extends Fragment implements View.OnClickListener {


    private DatabaseReference mDatabase;
    private Button countdown, createlist;
    private Intent intent;
    private ArrayList<Item> choicedList;


    public Tab1_admin() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton addObjectiveBtn = (ImageButton) view.findViewById(R.id.addItemBtn);

        addObjectiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addItemIntent =  new Intent(getContext(), AddItemActivity.class);
                startActivity(addItemIntent);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");
        final View rootView = inflater.inflate(R.layout.fragment_tab1_admin, container, false);
        intent = new Intent(getContext(), Timer.class);
        countdown = (Button)rootView.findViewById(R.id.countbutton);
        countdown.setOnClickListener(this);



        final ListView listView = (ListView) rootView.findViewById(R.id.objectList);

        createlist = (Button)rootView.findViewById(R.id.createlist);
        choicedList = new ArrayList<Item>();
        final ArrayList<Item> itemList = new ArrayList<>();

        mDatabase.child("Items").addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                // Get Item data value
                for(DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                    Item item = tempSnapshot.getValue(Item.class);
                    item.setCheckBox(new CheckBox(getContext()));
                    itemList.add(item);
                }
                //Set Item listview
                makeListView(listView,itemList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }

        });

        createlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0 ; i< itemList.size(); i ++){
                    //before update database, item choice flag have to set to false
                    Item initalItem = itemList.get(i);
                    initalItem.setChoice(false);
                    Map<String,Object> initalPostValues = initalItem.toMap();
                    mDatabase.child("Items").child("Item"+i).updateChildren(initalPostValues);

                    if(itemList.get(i).getCheckBox().isChecked()){
                        itemList.get(i).setChoice(true);
                        Log.i("choice?",":"+itemList.get(i).getChoice());
                        //update database : choice flag change to true
                        Item updateItem = itemList.get(i);
                        Map<String,Object> postValues = updateItem.toMap();
                        mDatabase.child("Items").child("Item"+i).updateChildren(postValues);

                        if(mDatabase.child("Items").child("Item"+i).updateChildren(postValues).isSuccessful()){
                            Toast.makeText(getContext(),"Create List Success!",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),"Create List Fail..",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        });

        return rootView;
    }

    public void makeListView(final ListView listView,ArrayList<Item> itemList){
        CreateMissionListAdapter adapter = new CreateMissionListAdapter(this.getContext(),R.layout.objectitemview, itemList);

        //현재 리스트의 상태를 보여주고 싶다..ㅠ
        /*for(int i = 0; i< adapter.getCount();i++){
            Log.i("choice?",":"+itemList.get(i).getChoice());
            if(itemList.get(i).getChoice())
            adapter.checkBoxState[i] = true;

        }*/

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.countbutton)
            startActivity(intent);
    }
}
