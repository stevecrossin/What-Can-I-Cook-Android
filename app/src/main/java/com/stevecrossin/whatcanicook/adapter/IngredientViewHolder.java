package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

class IngredientViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView ingredientName;
    private AppCompatCheckBox ingredientCheckBox;
    private AppDataRepo repository;

    IngredientViewHolder(@NonNull View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
        ingredientName = itemView.findViewById(R.id.ingredient_name);
        ingredientCheckBox = itemView.findViewById(R.id.ingredient_checkBox);
        repository = new AppDataRepo(itemView.getContext());
    }

    void bindRow(Ingredient ingredient) {
        ingredientName.setText(ingredient.getIngredientName());
        if (ingredient.isIngredientSelected())
            ingredientCheckBox.setChecked(true);
        else
            ingredientCheckBox.setChecked(false);
        ingredientCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ingredientSelected(isChecked, ingredientName.getText().toString());
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void ingredientSelected(final boolean isSelected, final String ingredientName) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<Ingredient> list = new ArrayList<>(repository.getIngredientsByName(ingredientName));
                if (isSelected){
                    for (Ingredient ingredient : list){
                        repository.selectIngredient(ingredient.getIngredientName());
                    }
                } else {
                    for (Ingredient ingredient : list){
                        repository.deselectIngredient(ingredient.getIngredientName());
                    }
                }
                return null;
            }
        }.execute();

    }
}
