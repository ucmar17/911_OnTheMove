package com.example.ucmar17.a911_onthemove;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

/**
 * Created by UCMar17 on 1/29/18.
 */

public class ActivityRecognizedService extends IntentService {
    public ActivityRecognizedService()
    {
        super("ActivityRecognizedService");
    }
    public ActivityRecognizedService(String name)
    {
        super(name);
    }
    public static final String BROADCAST_FILTER = "ActivityRecognizedService_broadcast_receiver_intent_filter";

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent))
        {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivity(result.getProbableActivities());
        }
    }
    private void handleDetectedActivity(List<DetectedActivity> probableActivities)
    {
        for (DetectedActivity activity: probableActivities)
        {
            //return activity.getType() == DetectedActivity.UNKNOWN;
            String tempo = String.valueOf(activity.getType()==DetectedActivity.UNKNOWN);
            Intent i = new Intent(BROADCAST_FILTER);
            i.putExtra("connection_established", true);
            i.putExtra("isEasy",tempo);
            sendBroadcast(i);
        }
    }
}
