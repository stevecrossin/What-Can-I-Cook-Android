package com.stevecrossin.whatcanicook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;

public class RecipeViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView recipeName;
    private AppCompatImageView recipeImage;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeName = itemView.findViewById(R.id.recipe_name);
        recipeImage = itemView.findViewById(R.id.recipe_img);
    }

    void bindRow(Recipe recipe, Context context) {
        recipeName.setText(recipe.getRecipeName());
        int drawableResourceId = context.getResources().getIdentifier(recipe.getRecipeImage(), "drawable", context.getPackageName());
        recipeImage.setImageResource(drawableResourceId);
    }
}
