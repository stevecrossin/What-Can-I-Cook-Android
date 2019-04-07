package com.stevecrossin.whatcanicook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.stevecrossin.whatcanicook.screens.Login;
import com.stevecrossin.whatcanicook.screens.MainActivity;

//Splash screen code. This has essentially been coded the same way as my contacts application in SIT207 - worked well, no need to reinvent the wheel here

public class SplashScreenInit extends AppCompatActivity {

    /**
     * On creation of the activity, the toolbar will be removed, the splash screen will be displayed as full screen and the current view will be set to the contents of
     * activity_splash screen XML layout. It will then create an instance of the splash screen and start it.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.start();
    }

    private class SplashScreen extends Thread {
        /**
         * Runs splash screen for 3 seconds, and handles any errors if they come up with a try/catch error
         */
        public void run() {
            try {
                sleep(3000);
            } catch (InterruptedException error) {
                error.printStackTrace();
            }

            /*
             This will occur once the timer expires. The splash screen activity will finish, and the main activity will load. This will be altered to the login activity later once it is coded.
             The splash screen instance will then be destroyed.
             */

            Intent intent = new Intent(com.stevecrossin.whatcanicook.SplashScreenInit.this, Login.class);
            startActivity(intent);
            com.stevecrossin.whatcanicook.SplashScreenInit.this.finish();
        }
    }
}
