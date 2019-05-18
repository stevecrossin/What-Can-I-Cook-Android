package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.IntoleranceViewAdapter;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Intolerances is the class which is run when the user clicks on the "Dietary Requriements" from MainActivity. It allows the user to select dietary requirements they have which in turn excludes
 * recipes and ingredients that they cannot eat
 */
public class Intolerances extends AppCompatActivity {
    private AppDataRepo repository;
    private IntoleranceViewAdapter intoleranceViewAdapter;

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_dietaryneeds XML and load the UI elements in that XML file into that view.
     * <p>
     * Initialise an instance of the AppDataRepo
     * Call the initRecyclerItems method
     * Load Google Ads for the activity and send an adRequest to load an ad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietaryneeds);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        repository = new AppDataRepo(this);
        initRecyclerItems();
    }

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the recyclerView in the layout, with the ID being intolerance_list.
     * 2. Set the layout manager as a LinerarLayout manager with elements in vertical order
     * 3. Set up the adapter for the recycler view as intoleranceViewAdapter
     * 4. Call the loadIntolerances method
     */
    private void initRecyclerItems() {
        RecyclerView intoleranceList = findViewById(R.id.intolerance_list);
        intoleranceList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        intoleranceViewAdapter = new IntoleranceViewAdapter(new ArrayList<Intolerance>());

        intoleranceList.setAdapter(intoleranceViewAdapter);
        loadIntolerances();
    }

    /**
     * This will perform the initial load of the intolerances list, specifically the list of all unique intolerances
     * by calling the getUniqueTolerance operation from the App Data Repo as an async task, and return the intolerances.
     * Once this task is complete, the contents of the intoleranceViewAdapter will be updated.
     */
    @SuppressLint("StaticFieldLeak")
    public void loadIntolerances() {
        new AsyncTask<Void, Void, List<Intolerance>>() {
            @Override
            protected List<Intolerance> doInBackground(Void... voids) {
                return repository.getUniqueTolerance();
            }

            @Override
            protected void onPostExecute(List<Intolerance> intolerances) {
                super.onPostExecute(intolerances);
                intoleranceViewAdapter.updateIntolerances(intolerances);
            }
        }.execute();
    }
}