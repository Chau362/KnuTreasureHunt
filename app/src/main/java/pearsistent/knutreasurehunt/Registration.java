package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        LinearLayout Layout1 = (LinearLayout) findViewById(R.id.Layout1);
        Layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this,Login_Team.class);
                    startActivity(i);
            }
        });
        LinearLayout Layout2 = (LinearLayout) findViewById(R.id.Layout2);
        Layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this,Login_admin.class);
                startActivity(i);
            }
        });
    }
}
