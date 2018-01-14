package com.example.ucmar17.a911_onthemove;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView mTextMessage, xText, yText, zText, result;
    private Sensor accel, gyro;
    PowerManager.WakeLock wL;
    private SensorManager smAccel, smGyro;
    private ArrayList<double[]> accVals, currentAccVals, gyroVals, currentGyroVals;
    private Button record;
    TextView gyroxak, gyroyak, gyrozak;
    private long currentTime;
    private Toolbar toolbar;
    private SensorManager sensorManagerAK;
    private Sensor gyroScopesensorAK;
    private SensorEventListener gyroscopeEventListenerAK;

    private SensorManager sm;
    private float acelVal, acelLast, shake; //acceleration difference
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
        //
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            xText.setText("XA: " + event.values[0]);
            yText.setText("YA: " + event.values[1]);
            zText.setText("ZA: " + event.values[2]);
            if (System.currentTimeMillis() - currentTime > 3000) {
                changeButton();
            } else if (record.getText().equals("Recording...")) {
                double[] temp = {event.values[0], event.values[1], event.values[2]};
                accVals.add(temp);
            } else if (record.getText().equals("Reading...")) {
                double[] temp = {event.values[0], event.values[1], event.values[2]};
                currentAccVals.add(temp);
            }
        } else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE_UNCALIBRATED){
            xText.setText("XG: " + event.values[0]);
            yText.setText("YG: " + event.values[1]);
            zText.setText("ZG: " + event.values[2]);
            if (System.currentTimeMillis() - currentTime > 3000) {
                changeButton();
            } else if (record.getText().equals("Recording...")) {
                double[] temp = {event.values[0], event.values[1], event.values[2]};
                gyroVals.add(temp);
            } else if (record.getText().equals("Reading...")) {
                double[] temp = {event.values[0], event.values[1], event.values[2]};
                currentGyroVals.add(temp);
            }
        }
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double)(x*x+y*y+z*z));
            float delta = acelVal-acelLast;
            shake  = shake *0.9f + delta;

            if (shake > 20)
            {
                Toast toast = Toast.makeText(getApplicationContext(),"Do not shake",Toast.LENGTH_SHORT );
                toast.show();
                if (Build.VERSION.SDK_INT >= 26) {
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(500,200));
                } else {
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
                }

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wL = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"911onthemove");
        super.onCreate(savedInstanceState);
        wL.acquire();
        setContentView(R.layout.activity_main);

        gyroxak = (TextView) findViewById(R.id.textView2);
        gyroyak = (TextView) findViewById(R.id.textView3);
        gyrozak = (TextView) findViewById(R.id.textView4);
        sensorManagerAK=(SensorManager)getSystemService(SENSOR_SERVICE);
        gyroScopesensorAK=sensorManagerAK.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (gyroScopesensorAK==null)
        {
            Toast.makeText(this,"No gyroscope", Toast.LENGTH_SHORT).show();
            finish();
        }

        gyroscopeEventListenerAK = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float gyroAkx = sensorEvent.values[0];
                float gyroAky = sensorEvent.values[1];
                float gyroAkz = sensorEvent.values[2];
                gyroxak.setText("X : " + (int)gyroAkx + " rad/s");
                gyroyak.setText("Y : " + (int)gyroAky + " rad/s");
                gyrozak.setText("Z : " + (int)gyroAkz + " rad/s");
                Log.d("val", gyroAkx + "");


            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        acelVal=SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        isFirstTime();

        mTextMessage = findViewById(R.id.message);

        smAccel = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        smGyro = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        try {
            accel = smAccel.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            gyro = smAccel.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
        }catch (NullPointerException e){
            mTextMessage.setText("Sensor");
        }
        smAccel.registerListener(this, accel, SensorManager.SENSOR_STATUS_ACCURACY_LOW);
        smGyro.registerListener(this, gyro, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);

        xText = findViewById(R.id.xText);
        yText = findViewById(R.id.yText);
        zText = findViewById(R.id.zText);
        result = findViewById(R.id.result);

        record = findViewById(R.id.record);

        accVals = new ArrayList<>();
        currentAccVals = new ArrayList<>();
        gyroVals = new ArrayList<>();
        currentGyroVals = new ArrayList<>();

        currentTime = (long) Double.POSITIVE_INFINITY;

        drawerLayout = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            //show dialog if app never launch
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_user);
            dialog.show();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        sensorManagerAK.registerListener(gyroscopeEventListenerAK,gyroScopesensorAK,SensorManager.SENSOR_DELAY_FASTEST);
        smAccel.registerListener(this, accel, SensorManager.SENSOR_STATUS_ACCURACY_LOW);
        Log.d("Debug", "Resume");
    }

    public void onStop() {
        super.onStop();
        smAccel.unregisterListener(this);
        Log.d("Debug", "Stop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManagerAK.unregisterListener(gyroscopeEventListenerAK);
    }

    public void changeButton(){
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150,100));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
        if(record.getText().equals("Recording...")){
            record.setText("Start Reading");
            record.setEnabled(true);
            currentTime = (long) Double.POSITIVE_INFINITY;
        } else if(record.getText().equals("Reading...")){
            record.setText("Start Recording");
            record.setEnabled(true);
            currentTime = (long) Double.POSITIVE_INFINITY;
            boolean check1 = compareLists(accVals, currentAccVals);
            boolean check2 = compareLists(gyroVals, currentGyroVals);
            Log.d("Debug", check1 + " " + check2);
            if(check1 && check2)
                result.setText("True");
            else
                result.setText("False");
        }
    }

    public void startRecord(View view) throws InterruptedException{
        Thread.sleep(3000);
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150,10));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
        if(record.getText().equals("Start Recording")) {
            accVals = new ArrayList<>();
            record.setText("Recording...");
            record.setEnabled(false);
            currentTime = System.currentTimeMillis();
            result.setText("TextView");
        } else if(record.getText().equals("Start Reading")) {
            currentAccVals = new ArrayList<>();
            record.setText("Reading...");
            record.setEnabled(false);
            currentTime = System.currentTimeMillis();
        }
    }

    private String displayList(ArrayList<double[]> array){
        String printer = "[";
        for(double[] element: array){
            printer += "[" + element[0] + ", " + element[1] + ", " + element[2] + "], ";
        }
        printer += "]";
        return printer + " " + array.size();
    }
    public void releaseWake(View v)
    {
        wL.release();
    }

    public boolean compareLists(ArrayList<double[]> one, ArrayList<double[]> two){
        int countx = 0;
        int county = 0;
        int countz = 0;
        int size = (one.size() > two.size()) ? two.size(): one.size();
        int pass = (int)(0.6 * size);
        for(int x = 0; x < size; x++){
            if(one.get(x)[0] + 0.15 > two.get(x)[0] && one.get(x)[0] - 0.15 < two.get(x)[0]){
                countx++;
            }
            if(one.get(x)[1] + 0.15 > two.get(x)[1] && one.get(x)[1] - 0.15 < two.get(x)[1]){
                county++;
            }
            if(one.get(x)[2] + 0.15 > two.get(x)[2] && one.get(x)[2] - 0.15 < two.get(x)[2]){
                countz++;
            }
        }
        Log.d("Debug", one.size() + "");
        Log.d("Debug", two.size() + "");
        Log.d("Debug", countx + " " + county + " " + countz + " ");
        if(countx >= pass && county >= pass)
            return true;
        else if(county >= pass && countz >= pass)
            return true;
        else if(countx >= pass && countz >= pass)
            return true;
        else return false;
    }
}