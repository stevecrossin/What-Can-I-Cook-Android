package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;

import java.util.ArrayList;

public class RecipeViewAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    private ArrayList<Recipe> recipes;
    private RecipeViewAdapter.rowClickedListener rowClickedListener;

    public RecipeViewAdapter(ArrayList<Recipe> recipes, RecipeViewAdapter.rowClickedListener rowClickedListener) {
        this.recipes = recipes;
        this.rowClickedListener = rowClickedListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecipeViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_row, viewGroup, false));
    }

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

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void updateRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public interface rowClickedListener {
        void onRowClicked(Recipe recipe);
    }
}
