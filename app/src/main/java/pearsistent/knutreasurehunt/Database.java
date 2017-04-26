package pearsistent.knutreasurehunt;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Zzeulki on 2017. 4. 22..
 */

public class Database {
    private DatabaseReference mDatabase;
    static private  ArrayList<Item> itemList;
    private ArrayList<Team> teamList;


    public Database() {
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");
    }

    public void getItemListFromDB(){

        final ArrayList<Item> tempList = new ArrayList<Item>();

        mDatabase.child("Items").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Item data value
                        for(DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                            Item item = tempSnapshot.getValue(Item.class);
                            tempList.add(item);
                        }
                        /*for(int i=0; i<tempList.size();i++){
                            Log.i("value1 " + i, tempList.get(i).getName());
                        }*/

                        setItemList(tempList);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }


                });
    }

    public void setItemList(ArrayList<Item> itemlist){
        this.itemList = itemlist;
    }

    public ArrayList<Item> getItemList(){

        getItemListFromDB();

        return  itemList;
    }

    public ArrayList<Team> getTeamList(){
        //final String userId = getUid();
        mDatabase.child("Team").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        teamList = new ArrayList<Team>();

                        // Get Item data value
                        for(DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                            Team team = tempSnapshot.getValue(Team.class);
                            teamList.add(team);
                        }
                        /*for(int i=0; i<itemList.size();i++){
                            Log.i("value " + i, itemList.get(i).getName());
                        }*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

        return  teamList;
    }


}
