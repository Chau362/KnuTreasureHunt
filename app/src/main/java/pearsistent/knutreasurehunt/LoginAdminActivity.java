package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginAdminActivity extends AppCompatActivity {
    EditText userName;
    EditText userPwd;
    Button loginBtn;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        userName= (EditText) findViewById(R.id.login_admin_usr);
        userPwd= (EditText) findViewById(R.id.login_admin_pwd);
        loginBtn = (Button) findViewById(R.id.Btn_login_admin_Login);
        registerBtn = (Button) findViewById(R.id.Btn_login_admin_Register);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userName.getText().toString();
                String pwd = userPwd.getText().toString();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginAdminActivity.this, RegisterAdminActivity.class);
                startActivity(i);
            }
        });
    }
}
