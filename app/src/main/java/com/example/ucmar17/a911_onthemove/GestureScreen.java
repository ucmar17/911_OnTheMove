package com.example.ucmar17.a911_onthemove;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by UCMar17 on 1/29/18.
 */

public class GestureScreen extends AppCompatActivity implements SensorEventListener {
    private TextView success;
    private TextView v3;
    //accel stuff:

    private ArrayList<ArrayList<Double>> run1; //ax,ay,az,yx,gy,gz
    private ArrayList<ArrayList<Double>> run2;
    private ArrayList<ArrayList<Double>> run3;
    private ArrayList<ArrayList<Double>> run4;
    private ArrayList<ArrayList<Double>> run5;
    private CountDownTimer timer;
    private CountDownTimer cTimer;
    //gyroscope stuff:
    private int CAP = 200;
    private int RUN;
    private boolean isCollecting;
    private TextView t4;
    private Sensor accel, gyro;

    private SensorManager smAccel, smGyro;
    protected static final String TAG = "MainActivity";
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture_screen);
        v3 = findViewById(R.id.textView3);
        //v3.setText("MAKE GESTURE IN...");
        run1 = new ArrayList<ArrayList<Double>>();
        run2 = new ArrayList<ArrayList<Double>>();
        run3 = new ArrayList<ArrayList<Double>>();
        run4 = new ArrayList<ArrayList<Double>>();
        run5 = new ArrayList<ArrayList<Double>>();
        for (int k = 0; k < 6; k++)
        {
            run1.add(new ArrayList<Double>());
            run2.add(new ArrayList<Double>());
            run3.add(new ArrayList<Double>());
            run4.add(new ArrayList<Double>());
            run5.add(new ArrayList<Double>());
        }

        isCollecting = false;
        RUN=0;
        t4 = findViewById(R.id.editText);
        smAccel = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        smGyro = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        try {
            accel = smAccel.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            gyro = smAccel.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
        } catch (NullPointerException e) {
        }
        smAccel.registerListener(this, accel, SensorManager.SENSOR_STATUS_ACCURACY_LOW);
        smGyro.registerListener(this, gyro, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);


        //timer.start();

        Log.d("f", "WHTHAHH");

        Log.d("f", "timer done");
        int dubz = 0;

        //String fileName = "MINMAXVALUES.txt";
        //saveFile(fileName, entryMinAccel);
        //String entryMaxAccel = maxValX + " " + maxValY + " " + maxValZ + "\n";

        /*success = findViewById(R.id.editText);
        mApiClient = new GoogleApiClient.Builder(GestureScreen.this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(GestureScreen.this)
                .addOnConnectionFailedListener(GestureScreen.this)
                .build();
        mApiClient.connect();*/
        Log.e("tag", "WHAT IS GOING ON!!!!!!!!!");
        //success.setVisibility(View.INVISIBLE);
        /*
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

    }

    @Override
    public void onBackPressed() {

    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (isCollecting)
        {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
            {
                double xtemp = Double.parseDouble(""+sensorEvent.values[0]);
                double ytemp = Double.parseDouble(""+sensorEvent.values[1]);
                double ztemp = Double.parseDouble(""+sensorEvent.values[2]);
                switch (RUN)
                {
                    case 1:
                        run1.get(0).add(xtemp);
                        run1.get(1).add(ytemp);
                        run1.get(2).add(ztemp);
                    case 2:
                        run2.get(0).add(xtemp);
                        run2.get(1).add(ytemp);
                        run2.get(2).add(ztemp);
                    case 3:
                        run3.get(0).add(xtemp);
                        run3.get(1).add(ytemp);
                        run3.get(2).add(ztemp);
                    case 4:
                        run4.get(0).add(xtemp);
                        run4.get(1).add(ytemp);
                        run4.get(2).add(ztemp);
                    case 5:
                        run5.get(0).add(xtemp);
                        run5.get(1).add(ytemp);
                        run5.get(2).add(ztemp);
                }


            }
            else
            {
                double xtemp = Double.parseDouble(""+sensorEvent.values[0]);
                double ytemp = Double.parseDouble(""+sensorEvent.values[1]);
                double ztemp = Double.parseDouble(""+sensorEvent.values[2]);
                switch (RUN)
                {
                    case 1:
                        run1.get(3).add(xtemp);
                        run1.get(4).add(ytemp);
                        run1.get(5).add(ztemp);
                    case 2:
                        run2.get(3).add(xtemp);
                        run2.get(4).add(ytemp);
                        run2.get(5).add(ztemp);
                    case 3:
                        run3.get(3).add(xtemp);
                        run3.get(4).add(ytemp);
                        run3.get(5).add(ztemp);
                    case 4:
                        run4.get(3).add(xtemp);
                        run4.get(4).add(ytemp);
                        run4.get(5).add(ztemp);
                    case 5:
                        run5.get(3).add(xtemp);
                        run5.get(4).add(ytemp);
                        run5.get(5).add(ztemp);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public void onStop() {
        super.onStop();
        smAccel.unregisterListener(this);
        smGyro.unregisterListener(this);

        Log.d("Debug", "Stop");
    }
    public void saveFile(String file, String text)
    {
        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), file);
        try {
            FileOutputStream fos = new FileOutputStream(myFile,true);
            try {
                fos.write(text.getBytes());
                //Toast.makeText(this, "Saved to" + getFilesDir() + "/" + file,Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    /*
    protected void updateDetectedActivitiesList() {
        ArrayList<DetectedActivity> detectedActivities = Utils.detectedActivitiesFromJson(
                PreferenceManager.getDefaultSharedPreferences(mContext)
                        .getString(Constants.KEY_DETECTED_ACTIVITIES, ""));

        mAdapter.updateActivities(detectedActivities);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(Constants.KEY_DETECTED_ACTIVITIES)) {
            updateDetectedActivitiesList();
        }
    }
    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private boolean getUpdatesRequestedState() {
        return PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Constants.KEY_ACTIVITY_UPDATES_REQUESTED, false);
    }
    private void setUpdatesRequestedState(boolean requesting) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(Constants.KEY_ACTIVITY_UPDATES_REQUESTED, requesting)
                .apply();
    }
    public void requestActivityUpdatesButtonHandler() {
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(
                Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                getActivityDetectionPendingIntent());

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(mContext,
                        "enabled",
                        Toast.LENGTH_SHORT)
                        .show();
                setUpdatesRequestedState(true);
                updateDetectedActivitiesList();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "failed");
                Toast.makeText(mContext,
                        "failed",
                        Toast.LENGTH_SHORT)
                        .show();
                setUpdatesRequestedState(false);
            }
        });
    }
    public void removeActivityUpdatesButtonHandler() {
        Task<Void> task = mActivityRecognitionClient.removeActivityUpdates(
                getActivityDetectionPendingIntent());
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(mContext,
                        "removed",
                        Toast.LENGTH_SHORT)
                        .show();
                setUpdatesRequestedState(false);
                // Reset the display.
                mAdapter.updateActivities(new ArrayList<DetectedActivity>());
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Failed to enable activity recognition.");
                Toast.makeText(mContext, "failed",
                        Toast.LENGTH_SHORT).show();
                setUpdatesRequestedState(true);
            }
        });
    }*/
/*
    private double maxValY= Double.NEGATIVE_INFINITY;
    private double minValY = Double.POSITIVE_INFINITY;
    private double maxValX= Double.NEGATIVE_INFINITY;
    private double minValX = Double.POSITIVE_INFINITY;
    private double maxValZ = Double.NEGATIVE_INFINITY;
    private double minValZ = Double.POSITIVE_INFINITY;
 */
}
