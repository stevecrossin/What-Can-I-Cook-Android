package com.stevecrossin.whatcanicook.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;

public class IngredientViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView ingredientName;
    private AppCompatImageView ingredientImage;

    public IngredientViewHolder(@NonNull View itemView) {
        super(itemView);

        ingredientName = itemView.findViewById(R.id.ingredient_name);
    }


    void bindRow(Ingredient ingredient) {
        ingredientName.setText(ingredient.getIngredientName());
    }
}
