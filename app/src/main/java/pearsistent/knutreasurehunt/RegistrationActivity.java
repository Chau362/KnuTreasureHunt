package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        LinearLayout teamRegistrationLayout = (LinearLayout) findViewById(R.id.teamRegistrationBlock);
        teamRegistrationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, LoginTeamActivity.class);
                    startActivity(i);
            }
        });

        LinearLayout adminRegistrationLayout = (LinearLayout) findViewById(R.id.adminRegistrationBlock);
        adminRegistrationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, LoginAdminActivity.class);
                startActivity(i);
            }
        });
    }
}
