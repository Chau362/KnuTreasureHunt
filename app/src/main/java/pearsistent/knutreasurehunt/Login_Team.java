package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login_Team extends AppCompatActivity {
    EditText Txt_user;
    EditText Txt_pwd;
    Button Login;
    Button Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__team);
        Txt_user = (EditText) findViewById(R.id.login_team_usr);
        Txt_pwd = (EditText) findViewById(R.id.login_team_pwd);
        Login = (Button) findViewById(R.id.Btn_login_team_Login);
        Register = (Button) findViewById(R.id.Btn_login_team_Register);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = Txt_user.getText().toString();
                String pwd = Txt_pwd.getText().toString();

                //2017.04.03 seulki : If you complete login function, you can use it.
                //Intent i = new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(i);
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Team.this,Register_Team.class);
                startActivity(i);
            }
        });
    }
}