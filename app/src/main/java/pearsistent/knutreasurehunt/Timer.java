package pearsistent.knutreasurehunt;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Timer extends AppCompatActivity {
    private int MILLISINFUTURE;
    private static final int COUNT_DOWN_INTERVAL = 1000;
    private int number;
    private String hstr = "0", mstr = "0", sstr = "0";
    private TextWatcher textWatcher;

    private EditText hour, minute, second;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

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


        // countTxt = (TextView)findViewById(R.id.count_txt);
    //    countDownTimer();

//        MILLISINFUTURE = Integer.parseInt(hour.getText().toString()) * 3600 +
//                Integer.parseInt(minute.getText().toString()) * 60 +
//                Integer.parseInt(second.getText().toString());


    }


    public void countDownTimer() {

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                long durationSeconds = millisUntilFinished / 1000;
                Log.d("hi", "hi");


                hour.setText(String.format("%02d", durationSeconds / 3600));
                minute.setText(String.format("%02d", (durationSeconds % 3600) / 60));
                second.setText(String.format("%02d", durationSeconds % 60));

            }

            public void onFinish() {
            }
        };
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            countDownTimer.cancel();
        } catch (Exception e) {
        }
        countDownTimer = null;
    }

    public void onClick(View v) {
        int num = Integer.parseInt(sstr);
        MILLISINFUTURE = num * 1000;

        num = Integer.parseInt(mstr);
        MILLISINFUTURE += num * 60000;

        num = Integer.parseInt(hstr);
        MILLISINFUTURE += num * 60 * 60000;
        countDownTimer();

        countDownTimer.start();
    }
}

