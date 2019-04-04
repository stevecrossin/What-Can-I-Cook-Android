package com.stevecrossin.whatcanicook.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;

import java.util.ArrayList;

public class MyIngredientViewAdapter extends RecyclerView.Adapter<MyIngredientViewHolder> {
    private ArrayList<Ingredient> ingredients;
    private MyIngredientViewAdapter.rowClickedListener rowClickedListener;

    public MyIngredientViewAdapter(ArrayList<Ingredient> ingredients, MyIngredientViewAdapter.rowClickedListener rowClickedListener) {
        this.ingredients = ingredients;
        this.rowClickedListener = rowClickedListener;
    }

    @NonNull
    @Override
    public MyIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyIngredientViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyIngredientViewHolder myIngredientViewHolder,final int i) {
        myIngredientViewHolder.bindRow(ingredients.get(i));
        myIngredientViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickedListener.onRowClicked(ingredients.get(i));
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
