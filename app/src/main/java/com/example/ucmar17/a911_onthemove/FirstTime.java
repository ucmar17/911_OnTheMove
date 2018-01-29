package com.example.ucmar17.a911_onthemove;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Scanner;

public class FirstTime extends AppCompatActivity {
    private EditText username, n1, n2, n3;
    String name = "", path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/text";;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time2);
        username = findViewById(R.id.name);
        n1 = findViewById(R.id.number1);
        n2 = findViewById(R.id.number2);
        n3 = findViewById(R.id.number3);
    }
    public void name(View view) throws FileNotFoundException {
        try {
            FileOutputStream file = openFileOutput("user.txt", MODE_PRIVATE);
            OutputStreamWriter outfile = new OutputStreamWriter(file);
            outfile.write(username.getText().toString() + "\n");
            outfile.write(n1.getText().toString() + "\n");
            outfile.write(n2.getText().toString() + "\n");
            outfile.write(n3.getText().toString());
            outfile.flush();
            outfile.close();
            Toast.makeText(FirstTime.this, "Sucessfully Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(FirstTime.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Intent change = new Intent(FirstTime.this, EnterGesture.class);
        startActivity(change);
    }
}