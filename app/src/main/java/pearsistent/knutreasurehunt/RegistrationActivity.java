package pearsistent.knutreasurehunt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

//last coder : seulki, 2017.04.27
public class RegistrationActivity extends AppCompatActivity {

    static final Integer APP_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        askForPermission(permission,APP_PERMISSION);


        deleteCache();

        LinearLayout teamRegistrationLayout = (LinearLayout) findViewById(R.id.teamRegistrationBlock);
        teamRegistrationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, LoginTeamActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        LinearLayout adminRegistrationLayout = (LinearLayout) findViewById(R.id.adminRegistrationBlock);
        adminRegistrationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, LoginAdminActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
            }
        });
    }

    //SeulKi : Because of Using cache on application, delete cache whenever turn on application. it is same work as update pictures.
    public void deleteCache() {
        File dir = getApplicationContext().getCacheDir();
        dir.delete();
        deleteDir(dir);
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    //check permission
    private void askForPermission(String permission[], Integer requestCode){

        for(int i = 0; i< permission.length;i++) {
            if (ContextCompat.checkSelfPermission(RegistrationActivity.this, permission[i]) != PackageManager.PERMISSION_GRANTED) {
                //one time is enough
                if(i==0) {
                    //multiple check permission ( location and storage)
                    ActivityCompat.requestPermissions(RegistrationActivity.this, permission, requestCode);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],int[] grantResult){
        super.onRequestPermissionsResult(requestCode,permissions,grantResult);

        //for(int i = 0 ; i< permissions.length ; i++) {
            if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();


                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.LOCATION_HARDWARE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return;
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
       // }
    }


}
