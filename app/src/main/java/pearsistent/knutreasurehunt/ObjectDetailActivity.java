package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.graphics.Bitmap;
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

// last coder : seulki, 2017.04.25

public class ObjectDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ImageView objectImage;
    private TextView objectText;
    public final int CAM_REQUEST = 1;
    private StorageReference mStorageRef;
    public Button submitBtn;
    public File file;
    public Uri objectURI;
    public Uri filePath;
    public Bitmap bitmap;
    private String teamName;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String imageFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_selfie);
        objectImage = (ImageView) findViewById(R.id.objectImage);
        objectText = (TextView) findViewById(R.id.objectText);
        submitBtn = (Button) findViewById(R.id.selfieSubmitButton);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://treasurehunt-5d55f.appspot.com");

        //get item name from UserMainAcitivity
        Intent intent = getIntent();
        imageFileName= intent.getExtras().getString("Item name")+".jpg";

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

        //check on android phone
        submitBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                FirebaseUser user = mAuth.getCurrentUser();
                String userProId = user.getProviderId();
                final String userId = user.getUid();

                mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");

                mDatabase.child("Team").addListenerForSingleValueEvent(new ValueEventListener(){

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Item data value
                        for(DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                            boolean check = false;
                            Team team = new Team();
                            team = tempSnapshot.getValue(Team.class);
                            for(int i = 0 ; i < team.getTeamMembers().size() ; i++) {

                                //finding team name using member's userId
                                if (team.getTeamMembers().get(i).getUserId().equals(userId)) {
                                    teamName = team.getTeamName();
                                    check = true;
                                    break;
                                }
                            }
                            if(check){
                                savePictures(teamName);
                                break;

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }

                });

            }
        });
    }

    private void backToPage(String teamName) {

        Intent i = new Intent(this.getApplicationContext(),UserMainActivity.class);
        i.putExtra("TEAM_NAME",teamName);
        setResult(1,i);
        finish();
    }

    //saving pictures on Storage
    private void savePictures(String teamname){


        //make a Folder
        StorageReference parentRef = storageRef.child(teamname);
        //can make a image file name
        StorageReference childRef = storageRef.child(teamname+"/"+this.getImageFileName());
        //upload task
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
                backToPage(teamName);
            }
        });
    }


    private String getImageFileName(){
        return this.imageFileName;
    }

    private File getFile(){

        File folder = new File("sdcard/carmera_app");

        if(!folder.exists()){
            folder.mkdir();
        }

        //dynamically make a file name
        File imageFile = new File(folder,this.getImageFileName());
        if(imageFile.exists()){
            imageFile.delete();
            imageFile = new File(folder,this.getImageFileName());
        }


        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String path = "sdcard/carmera_app/"+this.getImageFileName();

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
