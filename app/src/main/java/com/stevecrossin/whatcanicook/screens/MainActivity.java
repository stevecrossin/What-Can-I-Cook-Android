package com.stevecrossin.whatcanicook.screens;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.User;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //Navigate to Ingredients view with search for Breakfast Dishes
    public void breakfastIngredients(View view) {
        findViewById(R.id.breakfastbutton);
        String breakfast = ("What's for Breakfast");

        Intent intent = new Intent(this, CategoryPicker.class);
        intent.putExtra("DISH_TYPE", breakfast);
        startActivity(intent);
    }

    //Navigate to Ingredients view with search for Lunch Dishes
    public void lunchIngredients(View view) {
        findViewById(R.id.lunch);
        String lunch = ("What's for Lunch?");

        Intent intent = new Intent(this, CategoryPicker.class);
        intent.putExtra("DISH_TYPE", lunch);
        startActivity(intent);
    }

    //Navigate to Ingredients view with search for Dinner Dishes
    public void dinnerIngredients(View view) {
        findViewById(R.id.dinner);
        String dessert = ("What's for Dinner?");

        Intent intent = new Intent(this, CategoryPicker.class);
        intent.putExtra("DISH_TYPE", dessert);
        startActivity(intent);
    }

    //Navigate to Ingredients view with search for Dessert Dishes
    public void dessertIngredients(View view) {
        findViewById(R.id.dessert);
        String dessert = ("What's for Dessert?");

        Intent intent = new Intent(this, CategoryPicker.class);
        intent.putExtra("DISH_TYPE", dessert);
        startActivity(intent);
    }

    //Navigate to Saved Recipes view
    public void savedRecipes(View view) {
        Intent intent = new Intent(this, SavedRecipes.class);
        startActivity(intent);
    }

    //Navigate to Pantry view
    public void userPantry(View view) {
        Intent intent = new Intent(this, Pantry.class);
        startActivity(intent);
    }

    //Navigate to Dietary Preferences view
    public void dietaryPreferences(View view) {
        Intent intent = new Intent(this, Intolerances.class);
        startActivity(intent);
    }

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


    //Navigate to About View
    public void aboutApp(View view) {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }
}