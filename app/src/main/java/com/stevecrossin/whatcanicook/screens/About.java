package com.stevecrossin.whatcanicook.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class About extends AppCompatActivity {

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_about XML and load the UI elements in that XML file into that view, being the TextView box.
     * Load Google Ads for the activity and send an adRequest to load an ad.
     * <p>
     * Using a TextInputStream, the application will read the contents of the about text file and while the contents are not equal to -1,
     * it will take the contents and write them to a buffer in memory. Once this is complete. the output will be converted to a string and then
     * the AboutText UI element will be updated with the contents of this string
     * <p>
     * If the file for some reason is missing, the exception is caught and logged
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        TextView aboutText = findViewById(R.id.aboutTextView);
        InputStream textInputStream = getResources().openRawResource(R.raw.about);
        ByteArrayOutputStream textOutputStream = new ByteArrayOutputStream();

        int aboutTextContents;
        try {
            aboutTextContents = textInputStream.read();
            while (aboutTextContents != -1) {
                textOutputStream.write(aboutTextContents);
                aboutTextContents = textInputStream.read();
            }
            textInputStream.close();
        } catch (IOException ioex) {
            new AppDataRepo(About.this).insertLogs("Error getting about text - file doesn't exist");
            Toast.makeText(About.this, "About text file does not exist", Toast.LENGTH_SHORT).show();
        }
        aboutText.setText(textOutputStream.toString());
    }


    /**
     * This is an OnClick method that is called when the "View Logs" button is clicked in the activity. It will handle changing the view from the current view, to the Logs.class, and then start that activity.
     */
    public void viewLogs(View view) {
        Intent intent = new Intent(this, Logs.class);
        startActivity(intent);
    }

}
