package com.stevecrossin.whatcanicook.adapter;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;

import java.util.ArrayList;

public class IngredientViewAdapter extends RecyclerView.Adapter<IngredientViewHolder> {
    private ArrayList<Ingredient> ingredients;

    /**
     * Creates a new instance of IngredientView Adapter, which contains the ingredients to be displayed in the adapter and the rowclicklistener
     */
    public IngredientViewAdapter(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Initialise IngredientViewHolder with the Row View that is to be inflated. Uses ingredient_row as the layout for each row in the viewholder
     */
    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new IngredientViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_row, viewGroup, false));
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the Ingredients are fetched and bound to the viewholder with the bindRow method.
     */
    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewHolder, @SuppressLint("RecyclerView") final int i) {
        ingredientViewHolder.bindRow(ingredients.get(i));
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of ingredients that exist
     */
    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    /**
     * Updates the list with new values, and then notifies DB that the data has been changed
     */
    public void updateIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }
}
