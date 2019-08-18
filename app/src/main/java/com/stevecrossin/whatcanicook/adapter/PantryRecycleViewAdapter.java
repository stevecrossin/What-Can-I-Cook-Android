package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.List;

/**
 * ViewAdapter, which dictates and sets the contents of the view adapter
 */
public class PantryRecycleViewAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private List<Ingredient> categories;
    private Context context;

    /**
     * Initialise the fields of each row in the viewholder - in this case the categories and list of ingredients
     */
    public PantryRecycleViewAdapter(List<Ingredient> categories) {
        this.categories = categories;
    }

    /**
     * Initialise CategoryViewHolder with the Row View that is to be inflated. Uses pantry_list_item as the layout for each row in the viewholder
     */
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new CategoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pantry_list_item, viewGroup, false));
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the ingredients are fetched and bound to the viewholder with the bindRow method. Then, an event listener is setup for each row is clicked.
     * <p>
     * Additionally, an onClick listener exists for the categoryImage UI element, which in this case controls the removal of ingredients from pantry. When this element is clicked, it gets the position of the row in the adapter and gets the ingredient name.
     * <p>
     * It will then pass this via an Async task to the removeIngredientFromPantry method, and the savePantrytoUserDB method (as pantry ingredients are also stored in user DB), in AppRepo which will in turn perform
     * the removal and updates. Once this is completed, the adapter will be notified of the removal, and the ingredient
     * will be removed from the ViewHolder and the list is updated without the removed item.
     */
    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder viewHolder, int i) {
        final Ingredient ingredient = categories.get(i);
        viewHolder.getCategoryName().setText(ingredient.getIngredientName());
        viewHolder.getCategoryImage().setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        AppDataRepo repo = new AppDataRepo(context);
                        repo.removeIngredientFromPantry(ingredient.getIngredientID());
                        repo.savePantryToUserDB();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        categories.remove(viewHolder.getAdapterPosition());
                        notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                }.execute();
            }
        });
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of ingredients that are in the pantry
     */
    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * Updates the list with new values, and then notifies DB that the data has been changed. This is called by loadPantry in Pantry class
     */
    public void updateCategories(List<Ingredient> categories) {
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    /**
     * Updates the list with new values, and then notifies DB that the data has been changed. This is called by onPostExecute in Pantry class
     */
    public void updateCategories(Ingredient category) {
        this.categories.add(category);
        notifyDataSetChanged();
    }

}