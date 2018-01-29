package com.example.ucmar17.a911_onthemove;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Ajithk14 on 1/28/2018.
 */

@SuppressLint("ParcelCreator")
public class MessageReceiver extends ResultReceiver {
    private MainActivity.Message message;

    public MessageReceiver(MainActivity.Message message) {
        super(new Handler());

        this.message = message;
    }
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData){
        message.displayMessage(resultCode, resultData);

    }
}
