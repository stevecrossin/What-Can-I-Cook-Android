package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;

import java.util.ArrayList;

/**
 * Recipes View Adapter, which holds an ArrayList of the contents of the recipes database
 */
public class RecipeViewAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    private ArrayList<Recipe> recipes;
    private RecipeViewAdapter.rowClickedListener rowClickedListener;

    /**
     * Creates a new instance of RecipeViewAdapter, which contains the recipes to be displayed in the adapter and the rowclicklistener
     */
    public RecipeViewAdapter(ArrayList<Recipe> recipes, RecipeViewAdapter.rowClickedListener rowClickedListener) {
        this.recipes = recipes;
        this.rowClickedListener = rowClickedListener;
    }

    /**
     * Initialise RecipeViewHolder with the Row View that is to be inflated. Uses recipe_row as the layout for each row in the viewholder
     */
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecipeViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_row, viewGroup, false));
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the recipes are fetched and bound to the viewholder with the bindRow method. Then, an event listener is setup for each row when clicked.
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, @SuppressLint("RecyclerView") final int i) {
        recipeViewHolder.bindRow(recipes.get(i), recipeViewHolder.itemView.getContext());
        recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickedListener.onRowClicked(recipes.get(i));
            }
        });
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of recipes that exist
     */
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    /**
     * Updates the list with new values, and then notifies DB that the data has been changed
     */
    public void updateRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    /**
     * Override method that gets the ID of each element in the adapter.
     */
    @Override
    public long getItemId(int position) {
        Recipe recipe = recipes.get(position);
        return recipe.getRecipeId();
    }

    /**
     * Interface that has the onRowClicked method to implement
     */
    public interface rowClickedListener {
        void onRowClicked(Recipe recipe);
    }
}
