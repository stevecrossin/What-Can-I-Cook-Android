package com.stevecrossin.whatcanicook.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stevecrossin.whatcanicook.R;

public class About extends AppCompatActivity {

    /*
    Loads About Activity when the class is called/created
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
