package pearsistent.knutreasurehunt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Timer extends AppCompatActivity {
    private LinearLayout editLinear;
    private int MILLISINFUTURE;
    private static final int COUNT_DOWN_INTERVAL = 1000;
    private int number;
    private String hstr = "0", mstr = "0", sstr = "0";
    private TextWatcher textWatcher;
    private TextView textView;

    private EditText hour, minute, second;

    private final Intent timer = new Intent("timer");

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
                Log.d("hi", MILLISINFUTURE + "");

                editLinear.setVisibility(View.INVISIBLE);
                Intent serviceIntent = new Intent(this, TimerService.class);
                serviceIntent.putExtra(TimerService.SERVICE_INTENT, MILLISINFUTURE + "");
                startService(serviceIntent);
        }
    }
}

