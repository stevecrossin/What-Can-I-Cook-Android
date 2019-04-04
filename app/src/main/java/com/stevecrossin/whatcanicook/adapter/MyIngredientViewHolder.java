package com.stevecrossin.whatcanicook.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;

public class MyIngredientViewHolder  extends RecyclerView.ViewHolder {
    private AppCompatTextView myIngredientName;

    public MyIngredientViewHolder(@NonNull View itemView) {
        super(itemView);
        myIngredientName = itemView.findViewById(R.id.category_name);
    }

    void bindRow(Ingredient ingredient) {
        myIngredientName.setText(ingredient.getIngredientName());
    }
}
