package com.stevecrossin.whatcanicook.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;

class MyIngredientViewHolder extends RecyclerView.ViewHolder {

    private AppCompatTextView myIngredientName;
    AppCompatImageView closeImage;

    MyIngredientViewHolder(@NonNull View itemView) {
        super(itemView);
        myIngredientName = itemView.findViewById(R.id.category_name);
        closeImage = itemView.findViewById(R.id.close_img);

    }

    void bindRow(Ingredient ingredient) {
        myIngredientName.setText(ingredient.getIngredientName());
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
