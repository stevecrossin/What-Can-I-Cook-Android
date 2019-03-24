package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;

import java.util.ArrayList;

public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private ArrayList<String> categories;
    private rowClickedListener rowClickedListener;

    public CategoryViewAdapter(ArrayList<String> categories, CategoryViewAdapter.rowClickedListener rowClickedListener) {
        this.categories = categories;
        this.rowClickedListener = rowClickedListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder ingredientViewHolder, @SuppressLint("RecyclerView") final int i) {
        ingredientViewHolder.bindRow(categories.get(i));
        ingredientViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickedListener.onRowClicked(categories.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updateCategories(ArrayList<String> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public interface rowClickedListener {
        void onRowClicked(String category);
    }
}
