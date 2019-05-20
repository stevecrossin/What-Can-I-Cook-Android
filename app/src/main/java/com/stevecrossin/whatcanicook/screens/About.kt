package com.stevecrossin.whatcanicook.screens

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * An About screen that contains information about the application. It loads from a text file into the view, and allows navigation to the log scene
 */
class About : AppCompatActivity() {

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_about XML and load the UI elements in that XML file into that view, being the TextView box.
     * Load Google Ads for the activity and send an adRequest to load an ad.
     *
     *
     * Using a TextInputStream, the application will read the contents of the about text file and while the contents are not equal to -1,
     * it will take the contents and write them to a buffer in memory. Once this is complete. the output will be converted to a string and then
     * the AboutText UI element will be updated with the contents of this string
     *
     *
     * If the file for some reason is missing, the exception is caught and logged
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        val aboutText = findViewById<TextView>(R.id.aboutTextView)
        val textInputStream = resources.openRawResource(R.raw.about)
        val textOutputStream = ByteArrayOutputStream()

        var aboutTextContents: Int
        try {
            aboutTextContents = textInputStream.read()
            while (aboutTextContents != -1) {
                textOutputStream.write(aboutTextContents)
                aboutTextContents = textInputStream.read()
            }
            textInputStream.close()
        } catch (ioex: IOException) {
            AppDataRepo(this@About).insertLogs("Error getting about text - file doesn't exist")
            Toast.makeText(this@About, "About text file does not exist", Toast.LENGTH_SHORT).show()
        }

        aboutText.text = textOutputStream.toString()
    }

    /**
     * This is an OnClick method that is called when the "View Logs" button is clicked in the activity. It will load the Logs.class, and then start that activity.
     */
    fun viewLogs(view: View) {
        val intent = Intent(this, Logs::class.java)
        startActivity(intent)
    }

}
