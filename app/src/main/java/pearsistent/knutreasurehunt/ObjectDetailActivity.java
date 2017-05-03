package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

// last coder : seulki, 2017.04.27

public class ObjectDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ImageView objectImage;
    private TextView objectText;
    private TextView objectName;
    public final int CAM_REQUEST = 1;
    private StorageReference mStorageRef;
    public Button submitBtn;
    public File file;
    public Uri objectURI = null;
    public Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String imagePath;
    private String teamName;
    private String itemName;
    private String itemSubText;
    private int itemPoint;
    public String pathArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_selfie);
        objectImage = (ImageView) findViewById(R.id.objectImage);
        objectName = (TextView) findViewById(R.id.objectName);
        objectText = (TextView) findViewById(R.id.objectText);
        submitBtn = (Button) findViewById(R.id.selfieSubmitButton);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://treasurehunt-5d55f.appspot.com");

        //To save picture on Storage using team name and item name try to get path and something information from UserMainAcitivity
        Intent intent = getIntent();
        itemName = intent.getExtras().getString("ITEM_NAME");
        itemSubText = intent.getExtras().getString("ITEM_SUBTEXT");
        itemPoint = intent.getExtras().getInt("ITEM_POINT");
        imagePath = intent.getExtras().getString("PATH_TO_SAVE");


        objectName.setText(itemName);
        objectText.setText(itemSubText);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed__out");
                }
                ///// ...
            }
        };

        objectImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = getFile();

                //2017.04.06 : seulki : we have to use FileProvider because our API version is over 23.
                objectURI = FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()+".provider",file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, objectURI);
                startActivityForResult(intent,CAM_REQUEST);

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                //if user doesnt take a selfie, notice them
                if(objectURI==null){
                    Toast.makeText(ObjectDetailActivity.this, "Please submit with your selfie", Toast.LENGTH_SHORT).show();
                }
                else {
                    //can make a image file name
                    final StorageReference childRef = storageRef.child(getImagePath());

                    //can download : file already exist. we dont have to update point
                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        //already exist file : already counted team point
                        @Override
                        public void onSuccess(Uri uri) {

                            UploadTask uploadTask = childRef.putFile(objectURI);

                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {

                                    Toast.makeText(ObjectDetailActivity.this, "Upload Fail", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(ObjectDetailActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();


                                    backToPage(0);      //dont need to update itemlist in Team DB
                                }
                            });
                        }
                        //not exist file : have to count team point
                    }).addOnFailureListener(new OnFailureListener() { //cant download : file doenst exist. we have to update point
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            UploadTask uploadTask = childRef.putFile(objectURI);

                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {

                                    Toast.makeText(ObjectDetailActivity.this, "Upload Fail", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Toast.makeText(ObjectDetailActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();


                                    updateTeamPoint(); //update point
                                    backToPage(1);     //need to update itemlist in Team DB
                                }
                            });
                        }

                    });
                }


            }
        });
    }

    private void updateTeamPoint() {

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/Team");

        //there is teamName in pathArray[0](it was splited at getFile() function)
        teamName = pathArray[0];

        mDatabase.child(teamName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Team team = dataSnapshot.getValue(Team.class);
                team.setTeamPoint(team.getTeamPoint() + itemPoint);

                mDatabase.child(teamName).child("teamPoint").setValue(team.getTeamPoint());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //if state 0 : dont have to update itemlist in Team DB
    //if state 1 : have to update itemlist in Team DB
    private void backToPage(int state) {

        Intent i = new Intent(this.getApplicationContext(),UserMainActivity.class);
        i.putExtra("State",state);
        setResult(1,i);
        finish();
    }



    private String getImagePath(){
        return this.imagePath;
    }

    private File getFile(){

        File folder = new File("sdcard/carmera_app");

        if(!folder.exists()){
            folder.mkdir();
        }

        //Path format : TeamName/itemName.jpg
        pathArray = getImagePath().split("/");

        //dynamically make a file name
        File imageFile = new File(folder,pathArray[1]);

        if(imageFile.exists()){
            imageFile.delete();
            imageFile = new File(folder,pathArray[1]);
        }


        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String path = "sdcard/carmera_app/"+pathArray[1];

        //set Image on Imageview screen
        objectImage.setImageDrawable(Drawable.createFromPath(path));

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