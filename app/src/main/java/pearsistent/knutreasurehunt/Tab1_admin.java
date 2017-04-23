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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1_admin extends Fragment implements View.OnClickListener {

    private ArrayList<Item> itemList;
    private DatabaseReference mDatabase;
    Button countdown, createlist;
    Intent intent;


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
        Database database = new Database();

        itemList = new ArrayList<>();

        mDatabase.child("Items").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // Get Item data value
                        for(DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                            Item item = tempSnapshot.getValue(Item.class);
                            item.setCheckBox(new CheckBox(getContext()));
                            itemList.add(item);
                        }
                       makeListView(listView);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }

                });

       /* ArrayList<Item>  choicedList = new ArrayList<>();

        Item Item1 = new Item("mission itme 1",new CheckBox(getContext()));
        choicedList.add(Item1);

        Item Item2 = new Item("mission itme 2",new CheckBox(getContext()));
        choicedList.add(Item2);

        Item Item3 = new Item("mission itme 3",new CheckBox(getContext()));
        choicedList.add(Item3);

        Item Item4 = new Item("mission itme 4",new CheckBox(getContext()));
        choicedList.add(Item4);

        Item Item5 = new Item("mission itme 5",new CheckBox(getContext()));
        choicedList.add(Item5);*/

//        for(int i=0; i<itemList.size();i++){
//               Log.i("value1 " + i, itemList.get(i).getName());
//        }






        return rootView;
    }

    public void makeListView(ListView listView){
        CreateMissionListAdapter adapter = new CreateMissionListAdapter(this.getContext(),R.layout.objectitemview, itemList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.countbutton)
            startActivity(intent);
    }
}
