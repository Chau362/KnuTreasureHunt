package pearsistent.knutreasurehunt;

//Edited by Bogyu 4.4

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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

//last coder :seulki, 04.05
//Edited by bogyu, 4.18 : distinguish success or failure

public class RegisterAdminActivity extends BaseActivity {
    private static final String TAG = "EmailPassword";

    private EditText adminName;
    private EditText adminPwd;
    private EditText adminConfirmPwd;
    private Button registerBtn;

    String id,pwd;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //////Add Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);
        adminName = (EditText) findViewById(R.id.rgstr_admin_usr);
        adminPwd = (EditText) findViewById(R.id.rgstr_admin_pwd);
        adminConfirmPwd= (EditText) findViewById(R.id.rgstr_admin_cpwd);
        registerBtn = (Button) findViewById(R.id.Btn_rgstr_admin_Register);

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

        //hide type password
        adminPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        adminConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = adminName.getText().toString();
                pwd = adminPwd.getText().toString();
                Log.i("eeee","etest");
                Log.i("why","id:"+id);
                Log.i("pw","pass:"+pwd);
                createAdmin(id, pwd);
                //Intent i = new Intent(getApplicationContext(),LoginAdminActivity.class);
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

    private void createAdmin(String email, String password) {
        Log.d(id, "createAdminAccount:" + email);
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
                        hideProgressDialog();
                        // [END_EXCLUDE]
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            Toast.makeText(RegisterAdminActivity.this, "Success!",
                                    Toast.LENGTH_SHORT).show();

                            //Bogyu, 04.18 : if admin sigup is successful, go to next step.
                            Intent i = new Intent(RegisterAdminActivity.this, LoginAdminActivity.class);
                            startActivity(i);

                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        else if (!task.isSuccessful()) {
                            Log.w(TAG, "signUpWithEmail:failed", task.getException());
                        }
                    }
                });
    }
    // [END create_user_with_email]

    private boolean validateForm() {
        boolean valid = true;

        //String email = Txt_user.getText().toString();

        if (TextUtils.isEmpty(id)) {
            adminName.setError("Required.");
            valid = false;
        } else {
            adminName.setError(null);
        }

        //  String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            adminPwd.setError("Required.");
            valid = false;
        } else {
            adminPwd.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterAdminActivity.this, LoginTeamActivity.class));
        finish();
        overridePendingTransition(R.anim.righttoleft, R.anim.lefttoright);


    }
}