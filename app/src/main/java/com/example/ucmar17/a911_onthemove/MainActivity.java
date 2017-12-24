package com.example.ucmar17.a911_onthemove;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.hardware.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView mTextMessage, xText, yText, zText, result;
    private Sensor sensor;
    private SensorManager sm;
    private ArrayList<double[]> accVals, currentAccVals;
    private Button record;
    private long currentTime;
    private BottomNavigationView mBottomNav;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
        xText.setText(accuracy + " ACCURACY");
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        xText.setText("X: " + event.values[0]);
        yText.setText("Y: " + event.values[1]);
        zText.setText("Z: " + event.values[2]);
        if(System.currentTimeMillis() - currentTime > 3000){
            changeButton();
        } else if(record.getText().equals("Recording...")){
            double[] temp = {event.values[0], event.values[1], event.values[2]};
            accVals.add(temp);
        } else if(record.getText().equals("Reading...")){
            double[] temp = {event.values[0], event.values[1], event.values[2]};
            currentAccVals.add(temp);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mTextMessage.setText(R.string.title_home);
                        return true;
                    case R.id.navigation_dashboard:
                        mTextMessage.setText(R.string.title_dashboard);
                        return true;
                    case R.id.navigation_notifications:
                        mTextMessage.setText(R.string.title_notifications);
                        return true;
                }
                return false;
            }
        });

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);
        result = (TextView) findViewById(R.id.result);

        record = (Button) findViewById(R.id.record);

        accVals = new ArrayList<>();
        currentAccVals = new ArrayList<>();

        currentTime = (long) Double.POSITIVE_INFINITY;
    }

    public void changeButton(){
        if(record.getText().equals("Recording...")){
            record.setText("Start Reading");
            record.setEnabled(true);
            currentTime = (long) Double.POSITIVE_INFINITY;
        } else if(record.getText().equals("Reading...")){
            record.setText("Start Recording");
            record.setEnabled(true);
            currentTime = (long) Double.POSITIVE_INFINITY;
            boolean check = compareLists(accVals, currentAccVals);
            Log.d("ArrayLIST", check + "");
            result.setText(check + "");
        }
    }

    public void startRecord(View view){
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
        int pass = (int)(0.9 * size) - 1;
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
        Log.d("ArrayLIST", displayList(one));
        Log.d("ArrayLIST", displayList(two));
        Log.d("ArrayLIST", countx + " " + county + " " + countz + " ");
        if(countx >= pass && county >= pass && countz >= pass)
            return true;
        else return false;
    }
}