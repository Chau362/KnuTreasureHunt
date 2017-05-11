package pearsistent.knutreasurehunt;

import android.content.Intent;
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

//Timer for admin user
public class Timer extends AppCompatActivity {
    private LinearLayout editLinear;
    private long MILLISINFUTURE = 0;
    private String hstr = "0", mstr = "0", sstr = "0";
    private TextView textView;
    private EditText hour, minute, second;
    private long receivedTime;

    private boolean initialFlag = false;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private  DatabaseReference timeStamp;

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

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        timeStamp = mFirebaseDatabase.getReference("TimeStamp");

        timeStamp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    String key = (String) dataSnapshot.getValue();
                    textView.setText(key);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //get Time information from DB
        myRef = mFirebaseDatabase.getReference("time");

        if(initialFlag==false){

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(Long.class) != null){
                        String key = dataSnapshot.getKey();
                        if(key.equals("time"));{
                            receivedTime = dataSnapshot.getValue(Long.class);

                            long durationSeconds = receivedTime / 1000;

                            String remainedTime = String.format("%02d", durationSeconds / 3600)+":"
                                    +String.format("%02d", (durationSeconds % 3600) / 60)+":"+
                                    String.format("%02d", durationSeconds % 60);

                            textView.setText(remainedTime);
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set:
                editLinear.setVisibility(View.VISIBLE);
                initialFlag = true;
                break;

            case R.id.timerButton:

                if(initialFlag==false){
                    String currentText = textView.getText().toString();

                    String splitText[] = currentText.split(":");

                    sstr = splitText[2];
                    mstr = splitText[1];
                    hstr = splitText[0];

                    initialFlag = true;

                }
                    editLinear.setVisibility(View.INVISIBLE);
                    int num = Integer.parseInt(sstr);
                    MILLISINFUTURE = num * 1000;

                    num = Integer.parseInt(mstr);
                    MILLISINFUTURE += num * 60000;

                    num = Integer.parseInt(hstr);
                    MILLISINFUTURE += num * 60 * 60000;

                    editLinear.setVisibility(View.INVISIBLE);

                    myRef.removeValue();
                    myRef.setValue(MILLISINFUTURE);

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue(Long.class) != null) {
                                String key = dataSnapshot.getKey();
                                if (key.equals("time")) ;
                                {
                                    receivedTime = dataSnapshot.getValue(Long.class);

                                    Intent serviceIntent = new Intent(Timer.this, TimerService.class);
                                    serviceIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
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

