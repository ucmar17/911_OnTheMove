package com.example.ucmar17.a911_onthemove;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static android.app.Service.START_STICKY;
import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by Ajithk14 on 1/28/2018.
 */

public class RSSPullService extends IntentService {
    private static final String DEBUG_TAG = "AccelerServuice";

    private SensorManager sensorManager = null;
    private Sensor sensor = null;
    public RSSPullService() {
        super("something");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);


        return START_STICKY;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null)
        {
            for (int i = 0; i < 5; i++)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            vibrate();
            return;
        }
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        int time = intent.getIntExtra("time",0);
        for (int i = 0; i < 5; i++)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        Bundle bundle = new Bundle();
        bundle.putString("message", "COUNTING DONE...");
        receiver.send(1234,bundle);

    }
    private void vibrate()
    {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150,100));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
    }
}
