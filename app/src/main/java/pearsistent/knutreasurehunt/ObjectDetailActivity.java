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
import com.google.firebase.database.DatabaseReference;
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
    public final int CAM_REQUEST = 1;
    private StorageReference mStorageRef;
    public Button submitBtn;
    public File file;
    public Uri objectURI = null;
    public Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String imagePath;
    public String pathArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_selfie);
        objectImage = (ImageView) findViewById(R.id.objectImage);
        objectText = (TextView) findViewById(R.id.objectText);
        submitBtn = (Button) findViewById(R.id.selfieSubmitButton);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://treasurehunt-5d55f.appspot.com");

        //To save picture on Storage using team name and item name try to get path information from UserMainAcitivity
        Intent intent = getIntent();
        imagePath= intent.getExtras().getString("PATH_TO_SAVE");

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
                    StorageReference childRef = storageRef.child(getImagePath());
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
                        backToPage();
                        }
                });}

            }
        });
    }

    private void backToPage() {

        Intent i = new Intent(this.getApplicationContext(),UserMainActivity.class);
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
