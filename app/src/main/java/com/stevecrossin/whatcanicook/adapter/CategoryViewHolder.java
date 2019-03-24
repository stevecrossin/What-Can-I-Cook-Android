package com.stevecrossin.whatcanicook.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stevecrossin.whatcanicook.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private AppCompatTextView categoryName;
    private AppCompatImageView categoryImage;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryName = itemView.findViewById(R.id.category_name);
    }

    void bindRow(String category) {
        categoryName.setText(category);
    }
}
