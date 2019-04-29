package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;
import com.stevecrossin.whatcanicook.screens.RecipesDetails;
import com.stevecrossin.whatcanicook.screens.SavedRecipes;

import java.util.ArrayList;

public class RecipeViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView recipeName;
    private AppCompatImageView recipeImage;
    private AppCompatImageView recipeSaveButton;
    private AppDataRepo repository;

    private static final String TAG = "RecipeViewHolder";

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeName = itemView.findViewById(R.id.recipe_name);
        recipeImage = itemView.findViewById(R.id.recipe_img);
        recipeSaveButton = itemView.findViewById(R.id.recipe_save_button);

        repository = new AppDataRepo(itemView.getContext());
    }

    void bindRow(final Recipe recipe, Context context) {
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
    }
}
