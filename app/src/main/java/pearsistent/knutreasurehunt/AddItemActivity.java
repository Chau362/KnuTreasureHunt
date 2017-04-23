package pearsistent.knutreasurehunt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Chau Pham on 17.04.2017.
 */

public class AddItemActivity extends AppCompatActivity{
    private static final String TAG = "ADD_ITEM_TO_DATABASE";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private long countOfItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        final EditText itemName = (EditText) findViewById(R.id.itemNameEditText);
        final EditText itemDescription = (EditText) findViewById(R.id.itemDescriptionEditText);
        ImageButton confirmBtn = (ImageButton) findViewById(R.id.addItemToDatabaseBtn);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Items");


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = itemName.getText().toString();
                String description = itemDescription.getText().toString();
                Item newItem = new Item(name, description);
                String itemKey = "Item"+countOfItem;


                if(newItem != null) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userId = user.getUid();

                    Toast.makeText(AddItemActivity.this, newItem.getText(), Toast.LENGTH_SHORT).show();
                    //myRef.child(userId).child("Item").setValue(newItem);
                    myRef.child(itemKey).setValue(newItem);



                    itemName.setText("");
                    itemDescription.setText("");
                }
                finish();

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed__out");
                }
                ///// ...
            }
        };

        myRef.addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                countOfItem = dataSnapshot.getChildrenCount();
                Toast.makeText(AddItemActivity.this, "ee"+countOfItem, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
