package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

class IngredientViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView ingredientName;
    private AppCompatCheckBox ingredientCheckBox;
    private AppDataRepo repository;

    /**
     * Initialise the fields of each row in the viewholder - in this case the name of the ingredient and the checkbox.
     * Also creates a new instance of the AppDataRepo. This view is also not recyclable to prevent scrolling issues
     */
    IngredientViewHolder(@NonNull View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
        ingredientName = itemView.findViewById(R.id.ingredient_name);
        ingredientCheckBox = itemView.findViewById(R.id.ingredient_checkBox);
        repository = new AppDataRepo(itemView.getContext());
    }

    /**
     * Binds each row in the view holder to a record in the ingredients database and sets text in row to that database record.
     * Checks the ingredients database to see if the ingredient is noted as "selected", and if so, sets the checkbox to "checked"
     * otherwise the ingredient is not checked. Also contains instance of checkchangedlistener - once the ingredient is selected/deselected
     * this text is taken and converted to a string
     **/
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

    /**
     * This method is caled when an ingredient has been selected. This will perform an AsyncTask to call the database to change the value
     * of that ingredient to either selected (ingredientSelected=1), or deselected (ingredientSelected=0), based on the value of the string passed from bindrow() and whether the checkbox was ticked or unselected.
     */
    @SuppressLint("StaticFieldLeak")
    private void ingredientSelected(final boolean isSelected, final String ingredientName) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<Ingredient> list = new ArrayList<>(repository.getIngredientsByName(ingredientName));
                if (isSelected) {
                    for (Ingredient ingredient : list) {
                        repository.selectIngredient(ingredient.getIngredientName());
                    }
                } else {
                    for (Ingredient ingredient : list) {
                        repository.deselectIngredient(ingredient.getIngredientName());
                    }
                }
                return null;
            }
        }.execute();

    }
}
