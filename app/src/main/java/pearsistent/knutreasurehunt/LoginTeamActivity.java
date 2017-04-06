package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginTeamActivity extends AppCompatActivity {
    EditText userName;
    EditText userPwd;
    Button loginBtn;
    Button registerBtn;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__team);

        userName = (EditText) findViewById(R.id.login_team_usr);
        userPwd = (EditText) findViewById(R.id.login_team_pwd);
        loginBtn = (Button) findViewById(R.id.Btn_login_team_Login);
        registerBtn = (Button) findViewById(R.id.Btn_login_team_Register);

        //hide type password
        userPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userName.getText().toString();
                String pwd = userPwd.getText().toString();

                //2017.04.03 seulki : If you complete login function, you can use it.
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginTeamActivity.this, RegisterTeamActivity.class);
                startActivity(i);
            }
        });
    }
}