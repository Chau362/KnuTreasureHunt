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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

// last coder : seulki, 2017.04.21

public class ObjectDetailActivity extends AppCompatActivity {

    private ImageView objectImage;
    private TextView objectText;
    public final int CAM_REQUEST = 1;
    private StorageReference mStorageRef;
    public Button submitBtn;
    public File file;
    public Uri objectURI;
    public Uri filePath;
    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_selfie);


        objectImage = (ImageView) findViewById(R.id.objectImage);
        objectText = (TextView) findViewById(R.id.objectText);
        submitBtn = (Button) findViewById(R.id.selfieSubmitButton);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://treasurehunt-5d55f.appspot.com");



        objectImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

               /*     //이미지를 선택
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);*/

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


                //uploadFile();
               //Toast.makeText(ObjectDetailActivity.this,"button click", Toast.LENGTH_SHORT).show();

                StorageReference childRef = storageRef.child(objectURI.getLastPathSegment());

                UploadTask uploadTask = childRef.putFile(objectURI);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        //Log.i(TAG, exception.toString());
                        Toast.makeText(ObjectDetailActivity.this, "upload fail", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(ObjectDetailActivity.this, "upload success", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private File getFile(){

        File folder = new File("sdcard/carmera_app");

        if(!folder.exists()){
            folder.mkdir();
        }



        File imageFile = new File(folder,"cam_image.jpg");
        if(imageFile.exists()){
            imageFile.delete();
            imageFile = new File(folder,"cam_image.jpg");
        }


        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String path = "sdcard/carmera_app/cam_image.jpg";

        objectImage.setImageDrawable(Drawable.createFromPath(path));

        /*
       try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), objectURI);
            objectImage.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


}
