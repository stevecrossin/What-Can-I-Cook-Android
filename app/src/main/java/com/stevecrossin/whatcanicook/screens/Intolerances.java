package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.IntoleranceViewAdapter;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;
import java.util.List;

public class Intolerances extends AppCompatActivity {
    private AppDataRepo repository;
    private static final String TAG = "IngredientPicker";
    private IntoleranceViewAdapter intoleranceViewAdapter;


    /**
     * Initialization of this scene. This will get the category string passed by last scene and display to 'categorychosentext'
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
     * This will do the setup step for our recycle view:
     * 1. find the recycle view in the layout with id ingredients_list
     * 2. set the layout manager
     * 3. set up event listener for recycleview on row clicked [DEBUGGING PURPOSE]
     * 4. set adapter for the recycle view
     * 5. finall, call loadingredients method to populate data
     */

    private void initRecyclerItems() {
        RecyclerView intoleranceList = findViewById(R.id.intolerance_list);
        intoleranceList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        intoleranceViewAdapter = new IntoleranceViewAdapter(new ArrayList<Intolerance>(), new IntoleranceViewAdapter.rowClickedListener() {
            @Override
            public void onRowClicked(Intolerance intolerance) {
                Log.d(TAG, "onRowClicked: " + intolerance.getIntoleranceName());
            }
        });

        intoleranceList.setAdapter(intoleranceViewAdapter);

        loadIntolerances();
    }

    /**
     * Core function to load ingredients and update recycleview adapter.
     * This function performs an async task in the background to get a list of ingredient from the database (getByCategory)
     * It will then store those ingredients in an ArrayList and return the list.
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