package pearsistent.knutreasurehunt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Timer extends AppCompatActivity {
    private LinearLayout editLinear;
    private long MILLISINFUTURE = 0;
    private String hstr = "0", mstr = "0", sstr = "0";
    private TextView textView;
    private EditText hour, minute, second;
    private long receivedTime;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        editLinear = (LinearLayout)findViewById(R.id.editLinear);
        textView = (TextView)findViewById(R.id.realTime);

        hour = (EditText) findViewById(R.id.hour);
        hour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hstr = s.toString();
            }
        });

        minute = (EditText) findViewById(R.id.minute);
        minute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mstr = s.toString();
            }
        });

        second = (EditText) findViewById(R.id.second);
        second.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sstr = s.toString();
            }
        });

        registerReceiver(broadcastReceiver, new IntentFilter(TimerService.BROADCAST_ACTION));


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("time");

    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String realTime = intent.getStringExtra("TIME");
            textView.setText(realTime);

        }

    };

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        try {
//            countDownTimer.cancel();
//        } catch (Exception e) {
//        }
//        countDownTimer = null;
//    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set:
                editLinear.setVisibility(View.VISIBLE);
                break;

            case R.id.timerButton:
                editLinear.setVisibility(View.INVISIBLE);
                int num = Integer.parseInt(sstr);
                MILLISINFUTURE = num * 1000;

                num = Integer.parseInt(mstr);
                MILLISINFUTURE += num * 60000;

                num = Integer.parseInt(hstr);
                MILLISINFUTURE += num * 60 * 60000;

                editLinear.setVisibility(View.INVISIBLE);

                myRef.setValue(MILLISINFUTURE);

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue(Long.class) != null){
                            String key = dataSnapshot.getKey();
                            if(key.equals("time"));{
                                receivedTime = dataSnapshot.getValue(Long.class);

                                Intent serviceIntent = new Intent(Timer.this, TimerService.class);
                                serviceIntent.putExtra(TimerService.SERVICE_INTENT, receivedTime + "");
                                startService(serviceIntent);
                            }
                        }
                        dataSnapshot.getValue(Long.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        }
    }
}

