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

public class MyRecipesViewHolder extends RecyclerView.ViewHolder{
    private AppCompatTextView recipeName;
    private AppCompatImageView recipeImage;
    private AppCompatTextView recipeMissingIngredients;
    public AppCompatImageView recipeRemoveButton;
    private AppDataRepo repository;
    ArrayList<String> missingIngredients;

    private static final String TAG = "RecipeViewHolder";

    MyRecipesViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeName = itemView.findViewById(R.id.recipe_name);
        recipeImage = itemView.findViewById(R.id.recipe_img);
        recipeMissingIngredients = itemView.findViewById(R.id.recipe_missing_ingredients);
        recipeRemoveButton = itemView.findViewById(R.id.remove);

        repository = new AppDataRepo(itemView.getContext());
    }

    @SuppressLint("StaticFieldLeak")
    void bindRow(final Recipe recipe, final Context context) {
        recipeName.setText(recipe.getRecipeName());
        int drawableResourceId = context.getResources().getIdentifier(recipe.getRecipeImage(), "drawable", context.getPackageName());
        recipeImage.setImageResource(drawableResourceId);

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
