package com.stevecrossin.whatcanicook.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class About extends AppCompatActivity {
    private AdView mAdView;
    /*
    Loads About Activity when the class is called/created
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

        TextView aboutText = findViewById(R.id.aboutTextView);

        InputStream textInputStream = getResources().openRawResource(R.raw.about);

        ByteArrayOutputStream textOutputStream = new ByteArrayOutputStream();

        int t;
        try {
            t = textInputStream.read();
            while (t != -1)
            {
                textOutputStream.write(t);
                t = textInputStream.read();
            }
            textInputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        aboutText.setText(textOutputStream.toString());
    }

    //Navigate to Log View
    public void viewLogs(View view) {
        Intent intent = new Intent(this, Logs.class);
        startActivity(intent);
    }

}
