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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginTeamActivity extends BaseActivity{
    private static final String TAG = "LOGIN_TEAM_USER";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String teamName;
    //String userId;
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
                String user = userName.getText().toString();
                String pwd = userPwd.getText().toString();

                signIn(user,pwd);

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
    public void goToNextPage(String name){

        //2017.04.03 seulki : If you complete login function, you can use it.
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        i.putExtra("TEAM_NAME",name);
        startActivity(i);
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
                            Toast.makeText(LoginTeamActivity.this, "Success!",
                                    Toast.LENGTH_SHORT).show();
                            getTeamName();

                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        else if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginTeamActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]

                    }
                });
        // [END sign_in_with_email]
    }

    //To get team name information from DB
    private void getTeamName() {

        FirebaseUser user = mAuth.getCurrentUser();
        String userProId = user.getProviderId();
        final String userId = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");
        mDatabase.child("Team").addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get Item data value
                for(DataSnapshot tempSnapshot : dataSnapshot.getChildren()) {
                    boolean check = false;
                    Team team = new Team();
                    team = tempSnapshot.getValue(Team.class);
                    for(int i = 0 ; i < team.getTeamMembers().size() ; i++) {

                        //finding team name using member's userId
                        if (team.getTeamMembers().get(i).getUserId().equals(userId)) {

                            //if find right team information
                            goToNextPage(team.getTeamName());
                            check = true;
                            break;
                        }
                    }
                    if(check){
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });
    }



}

