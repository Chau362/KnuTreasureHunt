package pearsistent.knutreasurehunt;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2_admin extends Fragment implements View.OnClickListener {
    Intent intent;
    Button button;

    public Tab2_admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab2_admin, container, false);
        button = (Button)rootView.findViewById(R.id.changePoint);
        button.setOnClickListener(this);
        intent = new Intent(getContext(), Progress.class);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.changePoint)
            startActivity(intent);

    }
}
