package com.example.dotomorrow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Settings extends AppCompatActivity {



    MainActivity main = new MainActivity();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTitle("Hap");


        //((MainActivity)getCallingActivity()).setTitle("yes");


    }

}
