package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;

import java.util.List;

public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private List<Ingredient> categories;
    private rowClickedListener rowClickedListener;

    /**
     * Creates a new instance of CategoryView Adapter, which contains the categories in the adapter
     */
    public CategoryViewAdapter(List<Ingredient> categories, CategoryViewAdapter.rowClickedListener rowClickedListener) {
        this.categories = categories;
        this.rowClickedListener = rowClickedListener;
    }

    /**
     * Initialise CategoryViewHolder with the Row View that is to be inflated. Uses category_row as the layout for each row in the viewholder
     */
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_row, viewGroup, false));
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the Ingredient Categories is fetched and bound to the viewholder with the bindRow method. Then, an onClick listener is setup for each row,
     * which when clicked gets the ingredients for that category
     */
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder ingredientViewHolder, @SuppressLint("RecyclerView") final int i) {
        ingredientViewHolder.bindRow(categories.get(i), ingredientViewHolder.itemView.getContext());
        ingredientViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickedListener.onRowClicked(categories.get(i).getIngredientCategory());
            }
        });
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of categories that exist
     */
    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * Updates the list with new values, and then notifies DB that the data has been changed
     */
    public void updateCategories(List<Ingredient> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    /**
     * Interface that has the onRowClicked method to implement
     */
    public interface rowClickedListener {
        void onRowClicked(String category);
    }
}