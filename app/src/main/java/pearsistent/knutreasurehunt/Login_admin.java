package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login_admin extends AppCompatActivity {
    EditText Txt_user;
    EditText Txt_pwd;
    Button Login;
    Button Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        Txt_user= (EditText) findViewById(R.id.login_admin_usr);
        Txt_pwd= (EditText) findViewById(R.id.login_admin_pwd);
        Login = (Button) findViewById(R.id.Btn_login_admin_Login);
        Register = (Button) findViewById(R.id.Btn_login_admin_Register);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = Txt_user.getText().toString();
                String pwd = Txt_pwd.getText().toString();

                Intent i = new Intent(Login_admin.this,MainActivity_admin.class);
                startActivity(i);
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_admin.this,Register_admin.class);
                startActivity(i);
            }
        });
    }
}
