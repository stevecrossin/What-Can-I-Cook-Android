package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

public class MyRecipesViewAdapter extends RecyclerView.Adapter<MyRecipesViewHolder>{
    private ArrayList<Recipe> recipes;
    private MyRecipesViewAdapter.rowClickedListener rowClickedListener;
    private Context context;
    AppDataRepo repo = new AppDataRepo(context);


    public MyRecipesViewAdapter(ArrayList<Recipe> recipes, MyRecipesViewAdapter.rowClickedListener rowClickedListener) {
        this.recipes = recipes;
        this.rowClickedListener = rowClickedListener;
    }

    @NonNull
    @Override
    public MyRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new MyRecipesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.saved_recipe_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyRecipesViewHolder recipeViewHolder, @SuppressLint("RecyclerView") final int i) {
        final Recipe recipe = recipes.get(i);
        recipeViewHolder.bindRow(recipe, recipeViewHolder.itemView.getContext());
        recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickedListener.onRowClicked(recipes.get(i));
            }
        });

        recipeViewHolder.recipeRemoveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (recipe.isSaved()){
                            repo.unSaveRecipe(recipe.getRecipeId());
                        }
                        else{
                            repo.removeRecipe(recipe.getRecipeId());
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        recipes.remove(recipeViewHolder.getAdapterPosition());
                        notifyItemRemoved(recipeViewHolder.getAdapterPosition());
                    }
                }.execute();
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
