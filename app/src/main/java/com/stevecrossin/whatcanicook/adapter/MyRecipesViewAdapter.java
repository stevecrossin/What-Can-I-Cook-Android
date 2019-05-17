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


    /**
     * Initialise the fields of each row in the viewholder - in this case the name of the recipe, and onClick listeners to detemine if the row was clicked
     * (which causes the UI to navigate to the recipe itself,  and the close element, which causes the recipe to be removed  - these are all per the defined methods in the SavedRecipes class.
     */
    public MyRecipesViewAdapter(ArrayList<Recipe> recipes, MyRecipesViewAdapter.rowClickedListener rowClickedListener, MyRecipesViewAdapter.removeClickedListener removeClickedListener) {
        this.recipes = recipes;
        this.rowClickedListener = rowClickedListener;
        this.removeClickedListener = removeClickedListener;
    }

    /**
     * Initialise MyRecipesViewHolder with the Row View that is to be inflated. Uses saved_recipe_row as the layout for each row in the viewholder
     */
    @NonNull
    @Override
    public MyRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new MyRecipesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.saved_recipe_row, viewGroup, false));
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the select recipes are fetched and bound to the viewholder with the bindRow method. Then, an event listener is setup for each row is clicked.
     * <p>
     * Additionally, an onClick listener exists for the removeRecipe UI element. This UI element controls the removal of recipes. When this element is clicked, it gets the position of the row in the adapter and gets the recipe name.
     */
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
     * Task to remove the recipe. This is performed in the background via AsyncTask to either the unSaveRecipe method, or the removeRecipe method in AppDB, based on whether the recipe was already in the DB
     * but "saved", or a custom recipe that was added by the user. These operations will then perform the relevant database function, and then once completed, the recipe will be removed from the ViewHolder and the list will be updated without the removed item
     */

    @SuppressLint("StaticFieldLeak")
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

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                recipes.remove(recipeViewHolder.getAdapterPosition());
                notifyItemRemoved(recipeViewHolder.getAdapterPosition());
            }
        }.execute();
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of saved recipes that exist in the database
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
     * Interface that has the onRowClicked method to implement
     */
    public interface rowClickedListener {
        void onRowClicked(Recipe recipe);
    }

    /**
     * Interface that has the onRemoveClicked method to implement
     */
    public interface removeClickedListener {
        void onRemoveClicked(Recipe recipe, MyRecipesViewHolder recipeViewHolder);
    }
}
