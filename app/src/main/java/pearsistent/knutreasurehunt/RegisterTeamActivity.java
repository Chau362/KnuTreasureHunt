package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterTeamActivity extends AppCompatActivity {
    EditText userName;
    EditText userPwd;
    EditText userConfirmPwd;
    EditText userTeamname;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__team);

        userName = (EditText) findViewById(R.id.rgstr_team_usr);
        userPwd = (EditText) findViewById(R.id.rgstr_team_pwd);
        userConfirmPwd= (EditText) findViewById(R.id.rgstr_team_cpwd);
        userTeamname = (EditText) findViewById(R.id.rgstr_teamname);
        registerBtn = (Button) findViewById(R.id.Btn_rgstr_team_Register);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterTeamActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}
