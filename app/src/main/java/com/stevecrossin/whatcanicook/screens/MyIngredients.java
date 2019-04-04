package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.IngredientViewAdapter;
import com.stevecrossin.whatcanicook.adapter.MyIngredientViewAdapter;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

public class MyIngredients extends AppCompatActivity {
    private AppDataRepo repository;
    MyIngredientViewAdapter myIngredientViewAdapter;
    private static final String TAG = "MyIngredients";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myingredients);

        repository = new AppDataRepo(this);

        initRecyclerItems();
    }

    private void initRecyclerItems() {
        RecyclerView myIngredientsList = findViewById(R.id.my_ingredients_list);
        myIngredientsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //ingredientsList.setHasFixedSize(false);
        myIngredientViewAdapter = new MyIngredientViewAdapter(new ArrayList<Ingredient>(), new MyIngredientViewAdapter.rowClickedListener() {
            @Override
            public void onRowClicked(Ingredient ingredient) {
                Log.d(TAG, "onRowClicked: " + ingredient.getIngredientName());
            }
        } );
        myIngredientsList.setAdapter(myIngredientViewAdapter);
        loadIngredients();
    }

    @SuppressLint("StaticFieldLeak")
    public void loadIngredients() {
        new AsyncTask<Void, Void, ArrayList<Ingredient>>() {
            @Override
            protected ArrayList<Ingredient> doInBackground(Void... voids) {
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ingredients.addAll(repository.getAllCheckedIngredients());
                for (Ingredient ingredient : ingredients){
                    Log.d(TAG, "ingredient name: " + ingredient.getIngredientName());
                    Log.d(TAG, "checked : " + ingredient.isIngredientSelected());
                }
                return ingredients;
            }

            @Override
            protected void onPostExecute(ArrayList<Ingredient> ingredients) {
                super.onPostExecute(ingredients);
                myIngredientViewAdapter.updateIngredients(ingredients);
            }
        }.execute();
    }
}
