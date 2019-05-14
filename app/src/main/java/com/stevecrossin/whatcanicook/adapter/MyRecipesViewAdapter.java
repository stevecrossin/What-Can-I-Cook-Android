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
    private MyRecipesViewAdapter.removeClickedListener removeClickedListener;
    private Context context;
    private AppDataRepo repo = new AppDataRepo(context);


    //Adapter for Recyclerview of Saved Recipes. Contains onClick listeners for the clicking of the recipes and also of the X that removes recipes
    public MyRecipesViewAdapter(ArrayList<Recipe> recipes, MyRecipesViewAdapter.rowClickedListener rowClickedListener, MyRecipesViewAdapter.removeClickedListener removeClickedListener) {
        this.recipes = recipes;
        this.rowClickedListener = rowClickedListener;
        this.removeClickedListener = removeClickedListener;
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
                removeClickedListener.onRemoveClicked(recipes.get(i), recipeViewHolder);
            }
        });
    }

    /**
     * Task to remove the recipe. This is performed in background. If the recipe was a saved recipe, it will be "unsaved" otherwise if it was a custom
     * recipe it will be removed from the DB
     */

    public void removeRecipefromSaved(final Recipe recipe, final MyRecipesViewHolder recipeViewHolder) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (recipe.isSaved()) {
                    repo.unSaveRecipe(recipe.getRecipeId());
                } else {
                    repo.removeRecipe(recipe.getRecipeId());
                }
                return null;
            }

            //After the task has been executed, the recipe row will be removed from the viewholder
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                recipes.remove(recipeViewHolder.getAdapterPosition());
                notifyItemRemoved(recipeViewHolder.getAdapterPosition());
            }
        }.execute();
    }

    //Gets count of items to be in recyclerview? - trong - help
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    //Updates recipes in the view - again help!
    public void updateRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    //Listener for row click
    public interface rowClickedListener {
        void onRowClicked(Recipe recipe);
    }

    //Listener for remove click
    public interface removeClickedListener {
        void onRemoveClicked(Recipe recipe, MyRecipesViewHolder recipeViewHolder);
    }
}
