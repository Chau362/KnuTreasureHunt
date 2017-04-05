package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register_admin extends AppCompatActivity {
    EditText Txt_user;
    EditText Txt_pwd;
    EditText Txt_cpwd;
    Button Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);
        Txt_user = (EditText) findViewById(R.id.rgstr_admin_usr);
        Txt_pwd = (EditText) findViewById(R.id.rgstr_admin_pwd);
        Txt_cpwd= (EditText) findViewById(R.id.rgstr_admin_cpwd);
        Register = (Button) findViewById(R.id.Btn_rgstr_admin_Register);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register_admin.this,MainActivity_admin.class);
                startActivity(i);
            }
        });
    }
}
