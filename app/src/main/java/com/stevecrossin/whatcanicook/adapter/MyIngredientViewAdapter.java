package com.stevecrossin.whatcanicook.adapter;

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

    public MyIngredientViewAdapter(ArrayList<Ingredient> ingredients, MyIngredientViewAdapter.rowClickedListener rowClickedListener) {
        this.ingredients = ingredients;
        this.rowClickedListener = rowClickedListener;
    }

    @NonNull
    @Override
    public MyIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new MyIngredientViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_ingredient_row, viewGroup, false));
    }

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
