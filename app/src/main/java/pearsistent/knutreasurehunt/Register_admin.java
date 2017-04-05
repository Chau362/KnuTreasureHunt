package pearsistent.knutreasurehunt;

//Edited by Bogyu 4.4
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.w3c.dom.Text;

public class Register_admin extends BaseActivity {
    private static final String TAG = "EmailPassword";

    private EditText Txt_user;
    private EditText Txt_pwd;
    private EditText Txt_cpwd;
    private Button Register;

    String id,pwd;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //////Add Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);
        Txt_user = (EditText) findViewById(R.id.rgstr_admin_usr);
        Txt_pwd = (EditText) findViewById(R.id.rgstr_admin_pwd);
        Txt_cpwd= (EditText) findViewById(R.id.rgstr_admin_cpwd);
        Register = (Button) findViewById(R.id.Btn_rgstr_admin_Register);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                //updateUI(user);
            }
        };

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = Txt_user.getText().toString();
                pwd = Txt_pwd.getText().toString();
                createAccount(id,pwd);
                //Intent i = new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(i);
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(String email, String password) {
        Log.d(id, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register_admin.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END create_user_with_email]
    private boolean validateForm() {
        boolean valid = true;

        //String email = Txt_user.getText().toString();

        if (TextUtils.isEmpty(id)) {
            Txt_user.setError("Required.");
            valid = false;
        } else {
            Txt_user.setError(null);
        }

        //  String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            Txt_pwd.setError("Required.");
            valid = false;
        } else {
            Txt_pwd.setError(null);
        }

        return valid;
    }
}