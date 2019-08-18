package com.stevecrossin.whatcanicook.adapter;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;

/**
 * ViewHolder, which dictates and sets the contents of the view
 */
class MyIngredientViewHolder extends RecyclerView.ViewHolder {

    private AppCompatTextView myIngredientName;
    AppCompatImageView closeImage;

    /**
     * Initialise the fields of each row in the viewholder - in this case the name of the ingredient and the clickable close image.
     */
    MyIngredientViewHolder(@NonNull View itemView) {
        super(itemView);
        myIngredientName = itemView.findViewById(R.id.category_name);
        closeImage = itemView.findViewById(R.id.close_img);

    }

    /**
     * Binds each row in the view holder to a record in the ingredients database and sets text in row to that respective database record
     * Also sets up an onClick listener for the closeImage element on each row.
     */
    void bindRow(Ingredient ingredient) {
        myIngredientName.setText(ingredient.getIngredientName());
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
