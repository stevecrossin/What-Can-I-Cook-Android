package com.stevecrossin.whatcanicook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private AppCompatTextView categoryName;
    private AppCompatImageView categoryImage;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryName = itemView.findViewById(R.id.category_name);
        categoryImage = itemView.findViewById(R.id.recipe_img);
    }

    void bindRow(Ingredient category, Context context) {
        categoryName.setText(category.getIngredientCategory());
        int drawableResourceId = context.getResources().getIdentifier(category.getCategoryIconName(), "drawable", context.getPackageName());
        categoryImage.setImageResource(drawableResourceId);

    }
}