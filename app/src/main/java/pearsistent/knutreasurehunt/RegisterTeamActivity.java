package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Member;


///////Edited by bogyu 4.18
public class RegisterTeamActivity extends BaseActivity {
    private EditText teamName;
    private EditText userName;
    private EditText email;
    private EditText userPwd;
    private EditText userConfirmPwd;
    private Button registerBtn;

    ///////FOR DATABASE
    private DatabaseReference mDatabase;
    //////For Register
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //////Add Firebase
    private String auth_id,auth_pwd,rgstr_team,rgstr_user;
    private boolean CheckRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__team);

        userName = (EditText) findViewById(R.id.rgstr_team_usr);
        email = (EditText) findViewById(R.id.regtr_team_account);
        userPwd = (EditText) findViewById(R.id.rgstr_team_pwd);
        userConfirmPwd= (EditText) findViewById(R.id.rgstr_team_cpwd);
        teamName = (EditText) findViewById(R.id.rgstr_teamname);
        registerBtn = (Button) findViewById(R.id.Btn_rgstr_team_Register);

        ////For register
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("STATE", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("STATE", "onAuthStateChanged:signed_out");
                }
                //updateUI(user);
            }
        };
        ///////For DB
        mDatabase = FirebaseDatabase.getInstance().getReference();

        /////////

        //hide type password
        userPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        userConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     registerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auth_id = email.getText().toString();
                        auth_pwd = userPwd.getText().toString();
                        rgstr_team = teamName.getText().toString();
                        rgstr_user = userName.getText().toString();

                        Log.i("eeee","etest");

                        createTeam(auth_id,auth_pwd);
                        addTeamToDB(rgstr_team,auth_id,auth_pwd,rgstr_user);
                    }
                });
            }
        });
    }
    ///////Register Account to Firebase
    private void createTeam(String email, String pwd){
        Log.d(auth_id, "createAdminAccount:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("CREATE", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        hideProgressDialog();
                        // [END_EXCLUDE]
                        if(task.isSuccessful()) {
                            Log.d("CREATE", "signInWithEmail:onComplete:" + task.isSuccessful());
                            Toast.makeText(RegisterTeamActivity.this, "Success!",
                                    Toast.LENGTH_SHORT).show();

                            //if admin sign up is successful, go to Login.
                            Intent i = new Intent(RegisterTeamActivity.this,LoginAdminActivity.class);
                            startActivity(i);
                        }
                        // If sign up fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        else if (!task.isSuccessful()) {
                            Log.w("CREATE", "signUpWithEmail:failed", task.getException());
                        }
                    }
                });
    }

    /////Put team into DB
    private void addTeamToDB(String teamname, String email, String pwd, String username){
        Team team = new Team(teamname, email, pwd, null, 0);
        TeamMember member = new TeamMember();
        member.setMemberName(username);
      //  team.addTeamMember(member);

        mDatabase.child("team").push();
        mDatabase.child("team").setValue(team);


    }
    private boolean validateForm() {
        boolean valid = true;

        if (TextUtils.isEmpty(auth_id)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        if (TextUtils.isEmpty(auth_pwd)) {
            userPwd.setError("Required.");
            valid = false;
        } else {
            userPwd.setError(null);
        }



        return valid;
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

}
