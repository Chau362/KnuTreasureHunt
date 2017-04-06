package pearsistent.knutreasurehunt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterAdminActivity extends AppCompatActivity {

    EditText adminName;
    EditText adminPwd;
    EditText adminConfirmPwd;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_admin);
        adminName = (EditText) findViewById(R.id.rgstr_admin_usr);
        adminPwd = (EditText) findViewById(R.id.rgstr_admin_pwd);
        adminConfirmPwd= (EditText) findViewById(R.id.rgstr_admin_cpwd);
        registerBtn = (Button) findViewById(R.id.Btn_rgstr_admin_Register);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}
