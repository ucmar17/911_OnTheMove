package com.example.ucmar17.a911_onthemove;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.Task;

/**
 * Created by UCMar17 on 1/29/18.
 */

public class GestureScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public GoogleApiClient mApiClient;
    private TextView success;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture_screen);
        success = findViewById(R.id.editText);
        mApiClient = new GoogleApiClient.Builder(GestureScreen.this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(GestureScreen.this)
                .addOnConnectionFailedListener(GestureScreen.this)
                .build();
        mApiClient.connect();
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
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String myString = intent.getExtras().getString("isEasy");

            if (myString.equals("false")){
                Toast.makeText(getApplicationContext(),"TOO EASY", Toast.LENGTH_SHORT);}
            else
            {
                success.setVisibility(View.VISIBLE);
                //accel stuff
            }
        }
    };
    @Override
    public void onBackPressed()
    {

    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(ActivityRecognizedService.BROADCAST_FILTER));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("sock", "ima socket!!!!");
        Intent intent = new Intent(GestureScreen.this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(GestureScreen.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognitionClient activityRecognitionClient = ActivityRecognition.getClient(this);
        Task task = activityRecognitionClient.requestActivityUpdates( 3000, pendingIntent);

        //ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient,2000,pendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("Debug", "SCREW U");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Debug", "SCREW U 2");
    }
}
