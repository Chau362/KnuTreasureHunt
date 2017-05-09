package pearsistent.knutreasurehunt;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TimerService extends Service {
    private CountDownTimer countDownTimer;
    private TextView realTime;
    private Intent intent;
    private int initialTime;
    public static final String BROADCAST_ACTION = "TimeUpdate";
    public static final String SERVICE_INTENT = "InitialTime";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    public TimerService() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("TimeStamp");
    }

    public CountDownTimer getCountDownTimer(){
        return this.countDownTimer;
    }

    public void countDownTimer() {

        countDownTimer = new CountDownTimer(initialTime, 1000) {
            public void onTick(long millisUntilFinished) {
                long durationSeconds = millisUntilFinished / 1000;

                String remainedTime = String.format("%02d", durationSeconds / 3600)+":"
                        +String.format("%02d", (durationSeconds % 3600) / 60)+":"+
                        String.format("%02d", durationSeconds % 60);

                //intent = new Intent(BROADCAST_ACTION);
                //intent.putExtra("TIME",remainedTime);
                //sendBroadcast(intent);
                //Log.i("Receive","Start broadcast");

                myRef.setValue(remainedTime);

            }

            public void onFinish() {
                myRef.setValue("Finish");
            }
        };
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String timestr = intent.getStringExtra(SERVICE_INTENT);
        if(!timestr.equals(""))
            initialTime = Integer.parseInt(timestr);

        if(countDownTimer!=null){
            countDownTimer.cancel();
        }

        countDownTimer();
        countDownTimer.start();


        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

