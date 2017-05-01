package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;

//last coder : seulki, 2017.04.27
public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

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

}
