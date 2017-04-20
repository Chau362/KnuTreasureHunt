package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab1, container,false);
        ListView listView = (ListView) v.findViewById(R.id.itemList);
        Button addMember = (Button) v.findViewById(R.id.addmember);

        addMember.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(getContext(), AddMemberActivity.class);
                startActivity(intent);
            }

        });

        ArrayList<Item> arr = new ArrayList<>();

/*        Item item1 = new Item("Objective1",R.drawable.marker);
        arr.add(item1);

        Item item2 = new Item("Objective2",R.drawable.marker);
        arr.add(item2);

        Item item3 = new Item("Objective3",R.drawable.marker);
        arr.add(item3);

        Item item4 = new Item("Objective4",R.drawable.marker);
        arr.add(item4);

        Item item5 = new Item("Objective5",0);
        arr.add(item5);

        Item item6 = new Item("Objective6",0);
        arr.add(item6);

        Item item7 = new Item("Objective7",0);
        arr.add(item7);

        Item item8 = new Item("Objective8",0);
        arr.add(item8);*/

        ListAdapter adapter = new ListAdapter(this.getContext(),R.layout.itemview,arr);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Intent i = new Intent(getContext(),ObjectDetailActivity.class);
                    startActivity(i);
                }

            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
