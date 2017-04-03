package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register_Team extends AppCompatActivity {
    EditText Txt_user;
    EditText Txt_pwd;
    EditText Txt_cpwd;
    EditText Txt_teamname;
    Button Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__team);
        Txt_user = (EditText) findViewById(R.id.rgstr_team_usr);
        Txt_pwd = (EditText) findViewById(R.id.rgstr_team_pwd);
        Txt_cpwd= (EditText) findViewById(R.id.rgstr_team_cpwd);
        Txt_teamname = (EditText) findViewById(R.id.rgstr_teamname);
        Register = (Button) findViewById(R.id.Btn_rgstr_team_Register);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register_Team.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}
