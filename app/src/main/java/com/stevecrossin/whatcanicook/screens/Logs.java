package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.LogsAdapter;
import com.stevecrossin.whatcanicook.entities.LogRecords;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

public class Logs extends AppCompatActivity {

    private LogsAdapter logsAdapter;
    AppDataRepo logsRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        logsRepo = new AppDataRepo(this);
        initialiseRecyclerItems();

        Button button = findViewById(R.id.clearLogs);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logsRepo.clearLog();
                refreshContent();
            }
        });
    }

    private void initialiseRecyclerItems() {
        RecyclerView logsList = findViewById(R.id.logView);
        logsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        logsList.setHasFixedSize(false);
        logsAdapter = new LogsAdapter(new ArrayList<LogRecords>());
        logsList.setAdapter(logsAdapter);
        refreshContent();

    }

    /**
     * Handles refresh of the recyclerview. Performs a database query in the background, adds all the logs from log database
     * and updates the recyclerview with the content.
     * Performed in the background as if this is performed on the UI thread, an exception will occur.
     */

    @SuppressLint("StaticFieldLeak")
    private void refreshContent() {

        new AsyncTask<Void, Void, ArrayList<LogRecords>>() {
            @Override
            protected ArrayList<LogRecords> doInBackground(Void... voids) {
                return new ArrayList<>(logsRepo.getLogs());
            }

            /**
             *  Update LogsAdapter with result of getLogs that occurred in the background
             */
            @Override
            protected void onPostExecute(ArrayList<LogRecords> logs) {
                super.onPostExecute(logs);
                logsAdapter.updateLogs(logs);
            }
        }.execute();
    }


    /**
     * Refreshes content when activity is resumed
     */

    @Override
    protected void onResume() {
        super.onResume();
        refreshContent();
    }


}