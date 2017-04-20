package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

// last coder : seulki, 2017.04.06

public class ObjectDetailActivity extends AppCompatActivity {

    private ImageView objectImage;
    private TextView objectText;
    public final int CAM_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_selfie);


        objectImage = (ImageView) findViewById(R.id.objectImage);
        objectText = (TextView) findViewById(R.id.objectText);


        objectImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();

                //2017.04.06 : seulki : we have to user FileProvider because our API version is over 23.
                Uri objectURI = FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()+".provider",file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, objectURI);
                startActivityForResult(intent,CAM_REQUEST);

            }
        });
    }

    private File getFile(){

        File folder = new File("sdcard/carmera_app");

        if(!folder.exists()){
            folder.mkdir();
        }

        File imageFile = new File(folder,"cam_image.jpg");

        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        String path = "sdcard/carmera_app/cam_image.jpg";
        objectImage.setImageDrawable(Drawable.createFromPath(path));

    }
}
