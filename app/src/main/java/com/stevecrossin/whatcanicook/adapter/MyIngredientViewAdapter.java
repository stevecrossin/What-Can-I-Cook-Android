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
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

public class MyIngredientViewAdapter extends RecyclerView.Adapter<MyIngredientViewHolder> {
    private ArrayList<Ingredient> ingredients;
    private MyIngredientViewAdapter.rowClickedListener rowClickedListener;
    private Context context;

    /**
     * Creates a new instance of MyIngredientView Adapter, which contains the ingredients to be displayed in the adapter and the rowclicklistener
     */
    public MyIngredientViewAdapter(ArrayList<Ingredient> ingredients, MyIngredientViewAdapter.rowClickedListener rowClickedListener) {
        this.ingredients = ingredients;
        this.rowClickedListener = rowClickedListener;
    }

    /**
     * Initialise MyIngredientViewHolder with the Row View that is to be inflated. Uses selected_ingredient_row as the layout for each row in the viewholder
     */
    @NonNull
    @Override
    public MyIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new MyIngredientViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_ingredient_row, viewGroup, false));
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the select ingredients are fetched and bound to the viewholder with the bindRow method. Then, an event listener is setup for each row is clicked.
     * <p>
     * Additionally, an onClick listener exists for the closeImage UI element. This UI element controls the removal of ingredients.When this element is clicked, it gets the position of the row in the adapter and gets the ingredient name.
     * <p>
     * It will then pass this via an Async task to the deSelectIngredient method
     * in AppRepo which will in turn perform the deselection of the ingredient in the database. Once this is completed, the adapter will be notified of the removal, and the ingredient
     * will be removed from the ViewHolder and the list is updated without the removed item.
     */
    @Override
    public void onBindViewHolder(@NonNull final MyIngredientViewHolder myIngredientViewHolder, final int i) {
        final Ingredient ingredient = ingredients.get(i);
        myIngredientViewHolder.bindRow(ingredient);
        myIngredientViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickedListener.onRowClicked(ingredient);
            }
        });

        myIngredientViewHolder.closeImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        AppDataRepo repo = new AppDataRepo(context);
                        myIngredientViewHolder.getAdapterPosition();
                        repo.deselectIngredient(ingredient.getIngredientName());
                        return null;
                    }


                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        ingredients.remove(myIngredientViewHolder.getAdapterPosition());
                        notifyItemRemoved(myIngredientViewHolder.getAdapterPosition());
                    }
                }.execute();
            }
        });

    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of selected ingredients that exist
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

    /**
     * Interface that has the onRowClicked method to implement
     */
    public interface rowClickedListener {
        void onRowClicked(Ingredient ingredient);
    }
}
