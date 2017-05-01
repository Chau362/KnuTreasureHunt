package pearsistent.knutreasurehunt;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class TimerService extends Service {
    private CountDownTimer countDownTimer;
    private TextView realTime;
    private Intent intent;
    private int initialTime;
    public static final String BROADCAST_ACTION = "TimeUpdate";
    public static final String SERVICE_INTENT = "InitialTime";


    public TimerService() {

    }

    public void countDownTimer() {

        countDownTimer = new CountDownTimer(initialTime, 1000) {
            public void onTick(long millisUntilFinished) {
                long durationSeconds = millisUntilFinished / 1000;

                String remainedTime = String.format("%02d", durationSeconds / 3600)+":"
                        +String.format("%02d", (durationSeconds % 3600) / 60)+":"+
                        String.format("%02d", durationSeconds % 60);

                intent = new Intent(BROADCAST_ACTION);
                intent.putExtra("TIME",remainedTime);
                sendBroadcast(intent);
            }

            public void onFinish() {
            }
        };
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String timestr = intent.getStringExtra(SERVICE_INTENT);
        initialTime = Integer.parseInt(timestr);

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

