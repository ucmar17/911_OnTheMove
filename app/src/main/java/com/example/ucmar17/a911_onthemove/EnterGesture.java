package com.example.ucmar17.a911_onthemove;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnterGesture extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_gesture);
    }
    public void enterGesture1(View v)
    {
        Button temp1 = findViewById(R.id.ITERATION1);
        setContentView(R.layout.gesture_screen);
    }
    public void enterGesture2(View v)
    {
        Button temp2 = findViewById(R.id.ITERATION2);
        setContentView(R.layout.gesture_screen);
    }
    public void enterGesture3(View v)
    {
        Button temp3 = findViewById(R.id.ITERATION3);
        setContentView(R.layout.gesture_screen);

    }
    public void enterGesture4(View v)
    {
        Button temp4 = findViewById(R.id.ITERATION4);
        setContentView(R.layout.gesture_screen);

    }
    public void enterGesture5(View v)
    {
        Button temp5 = findViewById(R.id.ITERATION5);
        setContentView(R.layout.gesture_screen);

    }
}
