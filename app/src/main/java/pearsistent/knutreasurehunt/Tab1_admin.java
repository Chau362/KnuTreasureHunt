package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import android.widget.ListView;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1_admin extends Fragment implements View.OnClickListener {

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

        final View rootView = inflater.inflate(R.layout.fragment_tab1_admin, container, false);
        intent = new Intent(getContext(), Timer.class);
        countdown = (Button)rootView.findViewById(R.id.countbutton);
        countdown.setOnClickListener(this);

        ListView listView = (ListView) rootView.findViewById(R.id.objectList);

        createlist = (Button)rootView.findViewById(R.id.createlist);


        ArrayList<Item>  choicedList = new ArrayList<>();

        Item Item1 = new Item("mission itme 1",new CheckBox(getContext()));
        choicedList.add(Item1);

        Item Item2 = new Item("mission itme 2",new CheckBox(getContext()));
        choicedList.add(Item2);

        Item Item3 = new Item("mission itme 3",new CheckBox(getContext()));
        choicedList.add(Item3);

        Item Item4 = new Item("mission itme 4",new CheckBox(getContext()));
        choicedList.add(Item4);

        Item Item5 = new Item("mission itme 5",new CheckBox(getContext()));
        choicedList.add(Item5);

        CreateMissionListAdapter adapter = new CreateMissionListAdapter(this.getContext(),R.layout.objectitemview, choicedList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        /*        checkBox = (CheckBox)rootView.findViewById(R.id.option1);




/*        checkBox = (CheckBox)rootView.findViewById(R.id.option1);

        checkBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                countdown.setText("Modify");
                createlist.setText("Delete");
                return true;
            }
        });*/



        // Inflate the layout for this fragment


        return rootView;
    }



    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.countbutton)
            startActivity(intent);
    }
}
