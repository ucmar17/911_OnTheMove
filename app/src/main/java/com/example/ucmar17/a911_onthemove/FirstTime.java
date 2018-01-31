package com.example.ucmar17.a911_onthemove;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
    private int THEVAR = 0;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
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

    @Override
    public void onBackPressed()
    {

    }

    public void name(View view) throws FileNotFoundException {
        try {
            String regexStr = "^[+]?[0-9]{10,13}$";

            String number=n1.getText().toString();

            boolean b = (n1.getText().toString().length()<10 || number.length()>13 || number.matches(regexStr)==false  );
            if( TextUtils.isEmpty(username.getText()) && !TextUtils.isEmpty(n1.getText())){
                Toast.makeText(getApplicationContext(),"NAME IS REQUIRED!",Toast.LENGTH_SHORT).show();

                username.setError( "Name is required!" );


            }
            else if( TextUtils.isEmpty(n1.getText()) && TextUtils.isEmpty(username.getText())){
                Toast.makeText(getApplicationContext(),"NAME AND CALL NUMBER ARE REQUIRED!",Toast.LENGTH_SHORT).show();

                n1.setError( "Number to call is required!" );
                username.setError( "Name is required!" );


            }
            else if( TextUtils.isEmpty(n1.getText())&& !TextUtils.isEmpty(username.getText())){
                Toast.makeText(getApplicationContext(),"CALL NUMBER IS REQUIRED!",Toast.LENGTH_SHORT).show();

                n1.setError( "NUMBER TO CALL is required!" );


            }
            else if (b)
            {
                Toast.makeText(FirstTime.this,"Please enter "+"\n"+" valid phone number",Toast.LENGTH_SHORT).show();
            }
            else{
            FileOutputStream file = openFileOutput("user.txt", MODE_PRIVATE);
            OutputStreamWriter outfile = new OutputStreamWriter(file);
            outfile.write(username.getText().toString() + "\n");
            outfile.write(n1.getText().toString() + "\n");
            outfile.write(n2.getText().toString() + "\n");
            outfile.write(n3.getText().toString());
            outfile.flush();
            outfile.close();
            Toast.makeText(FirstTime.this, "Sucessfully Saved!", Toast.LENGTH_SHORT).show();
            Intent change = new Intent(FirstTime.this, MainActivity.class);
            startActivity(change);}
        } catch (Exception e){
            Toast.makeText(FirstTime.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}