package com.stevecrossin.whatcanicook.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stevecrossin.whatcanicook.R;

public class Logs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
    }
    String logdetail;

    /**
     * This function will append the logfile database with a log record
     */

    //Loads logs that currently exist in app.
    public void loadLogs() {
        /*
        This method is called when the user selects the View LogRecords button in the Info activity. When clicked, the UI will then:
        1. Navigate to logs scene
        2. Perform query of logs database, and fetch all records
        3. Pass that data to log scene and display in text box.
        */
    }

    //Appends log database when criteria met (e.g. caught exception)
    public void addLogRecord(String message) {
        /*
        When triggered due to exception or otherwise, this method will:
        1. Receive log message from method that has returned exception
        2. Add that log record to the logs database
        */
    }

    //Clear all logs from database
    public void clearLog() {
        /*
        When "Clear logs" button is clicked from log scene, this method will
        1. Perform database update to delete all records
        2. Clear the textview in the Log activity
        */
    }


}