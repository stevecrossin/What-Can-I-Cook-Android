package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

class RecipeViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView recipeName;
    private AppCompatTextView recipeMissingIngredients;
    private AppCompatImageView recipeImage;
    private AppCompatImageView recipeSaveButton;
    private AppDataRepo repository;
    private ArrayList<String> missingIngredients;


    private static final String TAG = "RecipeViewHolder";

    RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeName = itemView.findViewById(R.id.recipe_name);
        recipeImage = itemView.findViewById(R.id.recipe_img);
        recipeMissingIngredients = itemView.findViewById(R.id.recipe_missing_ingredients);
        recipeSaveButton = itemView.findViewById(R.id.recipe_save_button);

        repository = new AppDataRepo(itemView.getContext());
    }

    @SuppressLint("StaticFieldLeak")
    void bindRow(final Recipe recipe, final Context context) {
        recipeName.setText(recipe.getRecipeName());
        int drawableResourceId = context.getResources().getIdentifier(recipe.getRecipeImage(), "drawable", context.getPackageName());
        recipeImage.setImageResource(drawableResourceId);
        if (recipe.isSaved())
            recipeSaveButton.setImageResource(R.drawable.tick_icon);
        else
            recipeSaveButton.setImageResource(R.drawable.save_icon);


        recipeSaveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        repository.saveRecipe(recipe.getRecipeId());
                        return null;
                    }
                }.execute();
                recipeSaveButton.setImageResource(R.drawable.tick_icon);
            }
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                missingIngredients = new ArrayList<>(repository.getMissingIngredientsByName(recipe.getRecipeName(), 0));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                StringBuilder missing = new StringBuilder();
                for (String string : missingIngredients)
                    missing.append(string).append(", ");
                if (!missing.toString().equals(""))
                    missing = new StringBuilder(missing.substring(0, missing.length() - 2));

                if (missingIngredients.size() > 0) {
                    recipeMissingIngredients.setTextColor(Color.RED);
                    recipeMissingIngredients.setText(String.format("You need: %s for this recipe", missing.toString()));
                } else {
                    recipeMissingIngredients.setTextColor(Color.parseColor("#006400"));
                    recipeMissingIngredients.setText(context.getString(R.string.enoughIngredients));
                }

            }
        }.execute();
    }

    }