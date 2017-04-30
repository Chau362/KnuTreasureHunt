package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAdminActivity extends BaseActivity {
    private static final String TAG = "LOGIN_ADMIN";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText adminName;
    private EditText adminPwd;
    private Button loginBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        adminName = (EditText) findViewById(R.id.login_admin_usr);
        adminPwd = (EditText) findViewById(R.id.login_admin_pwd);
        loginBtn = (Button) findViewById(R.id.Btn_login_admin_Login);
        registerBtn = (Button) findViewById(R.id.Btn_login_admin_Register);

        //hide type password
        adminPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());



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
                    Log.d(TAG, "onAuthStateChanged:signed__out");
                }
                ///// ...
            }
        };

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = adminName.getText().toString();
                String pwd = adminPwd.getText().toString();
                signIn(user,pwd);


            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginAdminActivity.this,RegisterAdminActivity.class);
                startActivity(i);
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
    private void signIn(String email, String password) {

        Log.d(TAG, "signIn:" + email);
        //if (!validateForm()) {
        //    return;
        //}

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            Toast.makeText(LoginAdminActivity.this, "Success!",
                                    Toast.LENGTH_SHORT).show();

//                            seulki, 04.06 : if user login successful, go to next step.
//                                    Intent i = new Intent(LoginAdminActivity.this,MainActivity_admin.class);
//                                    startActivity(i);
                            Intent i = new Intent(LoginAdminActivity.this,CreatePDF.class);
                            startActivity(i);
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        else if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginAdminActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]

                    }
                });
        // [END sign_in_with_email]
    }
}
