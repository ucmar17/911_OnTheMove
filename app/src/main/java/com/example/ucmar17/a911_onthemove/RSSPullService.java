package com.example.ucmar17.a911_onthemove;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Ajithk14 on 1/14/2018.
 */

public class RSSPullService extends IntentService {
    public RSSPullService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
    }

}
