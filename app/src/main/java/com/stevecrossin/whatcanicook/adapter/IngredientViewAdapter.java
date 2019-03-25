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

public class IngredientViewAdapter extends RecyclerView.Adapter<IngredientViewHolder> {
    private ArrayList<Ingredient> ingredients;
    private IngredientViewAdapter.rowClickedListener rowClickedListener;


    public IngredientViewAdapter(ArrayList<Ingredient> ingredients, IngredientViewAdapter.rowClickedListener rowClickedListener) {
        this.ingredients = ingredients;
        this.rowClickedListener = rowClickedListener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new IngredientViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewHolder, @SuppressLint("RecyclerView") final int i) {
        ingredientViewHolder.bindRow(ingredients.get(i));
        ingredientViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickedListener.onRowClicked(ingredients.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void updateIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public interface rowClickedListener {
        void onRowClicked(Ingredient ingredient);
    }
}
