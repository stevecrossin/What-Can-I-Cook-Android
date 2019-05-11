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

import java.util.List;

public class PantryRecycleViewAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private List<Ingredient> categories;
    private Context context;


    public PantryRecycleViewAdapter(List<Ingredient> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new CategoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pantry_list_item, viewGroup, false));
    }

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
                        repo.removeIngredientFromPanty(ingredient.getIngredientID());
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

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updateCategories(Ingredient category) {
        this.categories.add(category);
        notifyDataSetChanged();
    }

    public void updateCategories(List<Ingredient> categories) {
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

}