package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.User;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

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
     * This is an OnClick method that is called when the "Breakfast" button is clicked in the activity. It will load the CategoryPicker.class,
     * and pass via intent the dish type (breakfast), and then start that activity.
     */
    public void breakfastIngredients(View view) {
        findViewById(R.id.breakfastbutton);
        String breakfast = ("What's for Breakfast");

        Intent intent = new Intent(this, CategoryPicker.class);
        intent.putExtra("DISH_TYPE", breakfast);
        startActivity(intent);
    }

    /**
     * This is an OnClick method that is called when the "Lunch" button is clicked in the activity. It will load the CategoryPicker.class,
     * and pass via intent the dish type (lunch), and then start that activity.
     */
    public void lunchIngredients(View view) {
        findViewById(R.id.lunch);
        String lunch = ("What's for Lunch?");

        Intent intent = new Intent(this, CategoryPicker.class);
        intent.putExtra("DISH_TYPE", lunch);
        startActivity(intent);
    }

    /**
     * This is an OnClick method that is called when the "Dinner" button is clicked in the activity. It will load the CategoryPicker.class,
     * and pass via intent the dish type (dinner), and then start that activity.
     */
    public void dinnerIngredients(View view) {
        findViewById(R.id.dinner);
        String dessert = ("What's for Dinner?");

        Intent intent = new Intent(this, CategoryPicker.class);
        intent.putExtra("DISH_TYPE", dessert);
        startActivity(intent);
    }

    /**
     * This is an OnClick method that is called when the "Dessert" button is clicked in the activity. It will load the CategoryPicker.class,
     * and pass via intent the dish type (dessert), and then start that activity.
     */
    public void dessertIngredients(View view) {
        findViewById(R.id.dessert);
        String dessert = ("What's for Dessert?");

        Intent intent = new Intent(this, CategoryPicker.class);
        intent.putExtra("DISH_TYPE", dessert);
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
    public void dietaryPreferences(View view) {
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