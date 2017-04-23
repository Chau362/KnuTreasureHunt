package pearsistent.knutreasurehunt;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by Zzeulki on 2017. 4. 22..
 */

public class Database {
    private DatabaseReference mDatabase;

    public Database() {
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/RQUnAn3LHyToX7WmCLWCRMIG7qT2");

        //final String userId = getUid();
        mDatabase.child("Items").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Item data value
                        Item itemList = dataSnapshot.getValue(Item.class);
                        Log.i("getValue", itemList.getName());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

        //I will try to get Team data from DB
        /*mDatabase.child("Team").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Team  = dataSnapshot.getValue(Item.class);
                        Log.i("getValue",temp.getName());
                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });*/
    }
}
