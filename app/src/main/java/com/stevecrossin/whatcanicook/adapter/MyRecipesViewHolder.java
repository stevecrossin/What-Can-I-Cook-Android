package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

public class MyRecipesViewHolder extends RecyclerView.ViewHolder{
    private AppCompatTextView recipeName;
    private AppCompatImageView recipeImage;
    public AppCompatImageView recipeRemoveButton;
    private AppDataRepo repository;

    private static final String TAG = "RecipeViewHolder";

    MyRecipesViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeName = itemView.findViewById(R.id.recipe_name);
        recipeImage = itemView.findViewById(R.id.recipe_img);
        recipeRemoveButton = itemView.findViewById(R.id.remove);

        repository = new AppDataRepo(itemView.getContext());
    }

    void bindRow(final Recipe recipe, Context context) {
        recipeName.setText(recipe.getRecipeName());
        int drawableResourceId = context.getResources().getIdentifier(recipe.getRecipeImage(), "drawable", context.getPackageName());
        recipeImage.setImageResource(drawableResourceId);
    }
}
