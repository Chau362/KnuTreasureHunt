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

import static com.google.android.gms.wearable.DataMap.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1_admin extends Fragment {

    private ArrayList<Item> choicedList;
    private DatabaseReference mDatabase;
    private Button createlist;
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

        ImageButton removeObjectBtn = (ImageButton) rootView.findViewById(R.id.removeItemBtn);


        final ListView listView = (ListView) rootView.findViewById(R.id.objectList);

        createlist = (Button)rootView.findViewById(R.id.createlist);
        //ArrayList<Item> choicedList = new ArrayList<Item>();
        final ArrayList<Item> createItemList = new ArrayList<>();

        mDatabase.child("Items").addValueEventListener(new ValueEventListener() {

            //ArrayList<Item> itemList = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                createItemList.clear();

                if(getContext()!=null) {
                    // Get Item data value
                    for (DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                        Item item = tempSnapshot.getValue(Item.class);

                        item.setCheckBox(new CheckBox(Tab1_admin.this.getContext()));
                        createItemList.add(item);

                    }
                    choicedList = createItemList;
                    //Set Item listview
                    makeListView(listView, createItemList);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }

        });

        removeObjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemCount = 0;
                final ArrayList<Item> updateItem = new ArrayList<Item>();
                int itemKey = 0;
                final DatabaseReference updateDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com");

                for (int i = 0; i < choicedList.size(); i++) {

                    if (!choicedList.get(i).getCheckBox().isChecked()) {
                        updateItem.add(choicedList.get(i));
                        itemCount++;
                    }
                }

                if(itemCount==choicedList.size()){
                    Toast.makeText(getContext(), "Have to choice one more Item!", Toast.LENGTH_SHORT).show();
                }
                else if( itemCount!=choicedList.size()) {
                    updateDatabase.child("Items").setValue(updateItem);
                    Toast.makeText(getContext(), "Remove Item Success!", Toast.LENGTH_SHORT).show();
                }


            }

        });


        createlist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int itemCount = 0;

                for (int i = 0; i < createItemList.size(); i++) {
                    //before update database, item choice flag have to set to false
                    Item initalItem = createItemList.get(i);
                    initalItem.setChoice(false);

                    if (createItemList.get(i).getCheckBox().isChecked()) {
                        createItemList.get(i).setChoice(true);

                    }else{
                        itemCount++;
                    }
                }
                if(itemCount!=createItemList.size()){
                    mDatabase.child("Items").setValue(createItemList);
                    Toast.makeText(getContext(), "Create List Success!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Have to choice one more Item!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        return rootView;
    }

    public void makeListView(ListView listView, ArrayList<Item> itemList) {
        CreateMissionListAdapter adapter = new CreateMissionListAdapter(this.getContext(), R.layout.objectitemview, itemList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

}
