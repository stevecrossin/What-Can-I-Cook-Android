package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.CategoryViewAdapter;
import com.stevecrossin.whatcanicook.adapter.IngredientViewAdapter;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

public class IngredientPicker extends AppCompatActivity {
    private AppDataRepo repository;
    private static final String TAG = "IngredientPicker";
    private String category;
    IngredientViewAdapter ingredientViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientspicker);

        Intent intent = getIntent();

        category = intent.getStringExtra("CATEGORY");
        TextView textView = findViewById(R.id.categorychosentext);
        textView.setText(category);


        repository = new AppDataRepo(this);

        initRecyclerItems();
    }

    private void initRecyclerItems() {
        RecyclerView ingredientsList = findViewById(R.id.ingredients_list);
        ingredientsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //ingredientsList.setHasFixedSize(false);
        ingredientViewAdapter = new IngredientViewAdapter(new ArrayList<Ingredient>(), new IngredientViewAdapter.rowClickedListener() {
            @Override
            public void onRowClicked(Ingredient ingredient) {
                Log.d(TAG, "onRowClicked: " + ingredient.getIngredientName());
            }
        } );

        ingredientsList.setAdapter(ingredientViewAdapter);

        loadIngredients();
    }

    @SuppressLint("StaticFieldLeak")
    public void loadIngredients() {
        new AsyncTask<Void, Void, ArrayList<Ingredient>>() {
            @Override
            protected ArrayList<Ingredient> doInBackground(Void... voids) {
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ingredients.addAll(repository.getIngredientsByCategory(category));
                for (Ingredient ingredient : ingredients){
                    Log.d(TAG, "ingredient name: " + ingredient.getIngredientName());
                    Log.d(TAG, "excluded : " + ingredient.isIngredientExcluded());
                }
                return ingredients;
            }

            @Override
            protected void onPostExecute(ArrayList<Ingredient> ingredients) {
                super.onPostExecute(ingredients);
                ingredientViewAdapter.updateIngredients(ingredients);
            }
        }.execute();


    }

    public void myIngredients(View view) {
        Intent intent = new Intent(IngredientPicker.this, MyIngredients.class);
        startActivity(intent);
    }
}
