package com.stevecrossin.whatcanicook.screens;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * A login screen that offers login via email/password. This code is based off the default Login Activity provided in Android Studio, and was modified based on that.
 * I learned how to implement this by creating a standalone app through a tutorial on YouTube I found here: https://www.youtube.com/watch?v=uV037mLG_Ps&t=676s
 */
public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }
}