package com.stevecrossin.whatcanicook.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.Recipe;

public class RecipeViewHolder  extends RecyclerView.ViewHolder{
    private AppCompatTextView recipeName;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeName = itemView.findViewById(R.id.category_name);
    }

    void bindRow(Recipe recipe) {
        recipeName.setText(recipe.getRecipeName());
    }
}
