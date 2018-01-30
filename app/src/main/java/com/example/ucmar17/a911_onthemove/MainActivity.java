package com.example.ucmar17.a911_onthemove;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private TextView mTextMessage, xText, yText, zText, result;
    private Sensor accel, gyro;
    private SensorManager smAccel, smGyro;
    private ArrayList<double[]> accVals, currentAccVals, gyroVals, currentGyroVals;
    private Button record;
    private long currentTime;
    private SensorManager sm;
    private float acelVal, acelLast, shake; //acceleration difference
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nav;
    private String person[];
    private LocationManager lmanager;
    private Location location;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/text", message = "I am in a crisis situation. My location is at: http://maps.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("debug", "JAKE PAUL IS NUMBER ONE!!!!");
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        File dir = new File(path);
        dir.mkdirs();

        isFirstTime();

        nav = findViewById(R.id.nav_menu);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.nav_settings:
                        Intent change = new Intent(MainActivity.this, FirstTime.class);
                        startActivity(change);
                        person = loadData();
                        mTextMessage.setText(person[0]);
                        return true;
                }
                return false;
            }
        });
        mTextMessage = findViewById(R.id.input_name);

        smAccel = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        smGyro = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        try {
            accel = smAccel.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            gyro = smAccel.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
        } catch (NullPointerException e) {
            mTextMessage.setText("Sensor");
        }
        smAccel.registerListener(this, accel, SensorManager.SENSOR_STATUS_ACCURACY_LOW);
        smGyro.registerListener(this, gyro, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);

        /*xText = findViewById(R.id.xText);
        yText = findViewById(R.id.yText);
        zText = findViewById(R.id.zText);*/
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

        person = loadData();
        mTextMessage.setText(person[0]);

        lmanager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
        //
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        mTextMessage.setText("Welcome " + person[0]);
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            /*xText.setText("XA: " + event.values[0]);
            yText.setText("YA: " + event.values[1]);
            zText.setText("ZA: " + event.values[2]);*/
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
            /*xText.setText("XG: " + event.values[0]);
            yText.setText("YG: " + event.values[1]);
            zText.setText("ZG: " + event.values[2]);*/
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
            acelVal = (float) Math.sqrt((double)(x * x + y * y + z * z));
            float delta = acelVal-acelLast;
            shake  = shake * 0.9f + delta;

            if (shake > 30)
            {
                //Toast toast = Toast.makeText(getApplicationContext(),"Do not shake",Toast.LENGTH_SHORT);
                //toast.show();
                makeCallAndText();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //
        }
    };
    public void makeCallAndText() throws NullPointerException{
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            message = "I am in a crisis situation.";
        } else {
            location = lmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            message = "I am in a crisis situation. My location is at: http://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
        }
        SmsManager manager = SmsManager.getDefault();
        if (!person[2].equals("")) {
            manager.sendTextMessage(person[2], null, message, null, null);
            manager.sendTextMessage(person[2], null, message, null, null);
        }
        if (!person[3].equals("")) {
            manager.sendTextMessage(person[3], null, message, null, null);
            manager.sendTextMessage(person[3], null, message, null, null);
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + person[1]));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }
    }
    private void isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        FirstTime first = new FirstTime();
        String b4 = String.valueOf(ranBefore);
        Log.d("Before?",b4);
        if (!ranBefore) {
            Log.d("debug1", "3");
            Intent change = new Intent(MainActivity.this, first.getClass());
            startActivity(change);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.apply();
        }
    }
    public String[] loadData() {
        File file = getApplicationContext().getFileStreamPath("user.txt");
        String temp = "";
        String numbers = "";
        if(file.exists()){
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("user.txt")));
                while((temp = reader.readLine()) != null){
                    numbers += temp + " ";
                }
            } catch (Exception e){
                numbers = e.getMessage();
            }
        }
        return numbers.split(" ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){;;
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        smAccel.registerListener(this, accel, SensorManager.SENSOR_STATUS_ACCURACY_LOW);
        smGyro.registerListener(this, gyro, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
        Log.d("Debug", "Resume");
    }

    public void onStop() {
        super.onStop();
        smAccel.unregisterListener(this);

        Log.d("Debug", "Stop");
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

    public boolean compareLists(ArrayList<double[]> one, ArrayList<double[]> two){
        int countx = 0;
        int county = 0;
        int countz = 0;
        int size = (one.size() > two.size()) ? two.size(): one.size();
        int pass = (int)(0.9 * size);
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
        if(countx >= pass && county >= pass)
            return true;
        else if(county >= pass && countz >= pass)
            return true;
        else if(countx >= pass && countz >= pass)
            return true;
        else return false;
    }
}