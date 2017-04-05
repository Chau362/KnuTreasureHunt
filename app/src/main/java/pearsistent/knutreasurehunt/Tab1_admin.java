package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import static java.security.AccessController.getContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1_admin extends Fragment implements View.OnClickListener {
    Button countdown, createlist;
    Intent intent;
    CheckBox checkBox;

    public Tab1_admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab1_admin, container,    false);
        intent = new Intent(getContext(), Timer.class);
        countdown = (Button)rootView.findViewById(R.id.countbutton);
        countdown.setOnClickListener(this);

        createlist = (Button)rootView.findViewById(R.id.createlist);

        checkBox = (CheckBox)rootView.findViewById(R.id.option1);
        checkBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                countdown.setText("Modify");
                createlist.setText("Delete");
                return true;
            }
        });



        // Inflate the layout for this fragment


        return rootView;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.countbutton)
            startActivity(intent);

    }
}
