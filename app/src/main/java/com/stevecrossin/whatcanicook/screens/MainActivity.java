package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.User;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

/**
 * The main class for the activity and the main entry point after login. This is mainly a landing page for the app where all other activities are navigated from, and has very little
 * actual functionality apart from navigation and logging out of the application.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_main XML and load the UI elements in that XML file into that view.
     * Load Google Ads for the activity and send an adRequest to load an ad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

     /**
     * This is an OnClick method that is called when the "Pick Ingredients" button is clicked in the activity. It will load the CategoryPicker.class/
     */
    public void pickIngredients(View view) {
        Intent intent = new Intent(this, CategoryPicker.class);
        startActivity(intent);
    }

    /**
     * This is an OnClick method that is called when the "Saved Recipes" button is clicked in the activity. It will load the SavedRecipes.class, and then start that activity.
     */
    public void savedRecipes(View view) {
        Intent intent = new Intent(this, SavedRecipes.class);
        startActivity(intent);
    }

    /**
     * This is an OnClick method that is called when the "My Pantry" button is clicked in the activity. It will load the Pantry.class, and then start that activity.
     */
    public void userPantry(View view) {
        Intent intent = new Intent(this, Pantry.class);
        startActivity(intent);
    }

    /**
     * This is an OnClick method that is called when the "Dietary Preferences" button is clicked in the activity. It will load the Intolerances.class, and then start that activity.
     */
    public void dietNeeds(View view) {
        Intent intent = new Intent(this, Intolerances.class);
        startActivity(intent);
    }

    /**
     * This is an OnClick method that is called when the "Logout" button is clicked in the activity.
     * It will create a new instance of the AppDataRepo and get the signed in user, and their user ID will be updated as per the DB operation, to set isLogin for that user in
     * the DB to "false".
     * <p>
     * Once this is completed, the method will load the Login.class, and then start that activity.
     */
    @SuppressLint("StaticFieldLeak")
    public void logout(View view) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                AppDataRepo repo = new AppDataRepo(MainActivity.this);
                User user = repo.getSignedUser();
                repo.updateLoginStatus(user.getUserID(), false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        }.execute();
    }

    /**
     * This is an OnClick method that is called when the "About" icon is clicked in the activity. It will handle changing the view from the current view, to the About class, and then start that activity.
     */
    public void aboutApp(View view) {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }
}