package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

/**
 * ViewHolder, which dictates and sets the contents of the view
 */
class RecipeViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView recipeName;
    private AppCompatTextView recipeMissingIngredients;
    private AppCompatImageView recipeSaveButton;
    private AppDataRepo repository;
    private ArrayList<String> missingIngredients;

    /**
     * Initialise the fields of each row in the viewholder - in this case the name of the recipe, the missing ingredients, and the save button. Also initialises an instance of AppDataRepo.
     */
    RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeName = itemView.findViewById(R.id.recipe_name);
        recipeMissingIngredients = itemView.findViewById(R.id.recipe_missing_ingredients);
        recipeSaveButton = itemView.findViewById(R.id.recipe_save_button);

        repository = new AppDataRepo(itemView.getContext());
    }

    /**
     * Binds each row in the view holder to a record in the recipes database and sets text in row to that respective database record.
     * Also checks the database to see if the user has previously saved the recipe. If they have, the drawable icon will be set to the tick icon, otherwise it will be set to the save icon.
     * <p>
     * Additionally, in the background (doInBackground), a task will be performed to provide the list of missing ingredients to the UI element missingIngredients.
     * It will first get the list of recipes by name and determine the ingredients that the user had already selected, and then call the recipe database for the ingredients with the getMissingIngredientsByName method in AppRepo.
     * From this, a list of ingredients will be returned and constructed in a string separated by commas. It will then check if the size of missing ingredients is greater then zero, If it is, it will in red print out the missing ingredients
     * in the missingIngredients arraylist element.
     * <p>
     * Otherwise the missingIngredients UI element will advise the user they have enough ingredients.
     **/
    @SuppressLint("StaticFieldLeak")
    void bindRow(final Recipe recipe, final Context context) {
        recipeName.setText(recipe.getRecipeName());
        if (recipe.isSaved())
            recipeSaveButton.setImageResource(R.drawable.tick_icon);
        else
            recipeSaveButton.setImageResource(R.drawable.save_icon);


        recipeSaveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        repository.saveRecipe(recipe.getRecipeId());
                        return null;
                    }
                }.execute();
                recipeSaveButton.setImageResource(R.drawable.tick_icon);
            }
        });


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                missingIngredients = new ArrayList<>(repository.getMissingIngredientsByName(recipe.getRecipeName(), 0));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                StringBuilder missing = new StringBuilder();
                for (String string : missingIngredients)
                    missing.append(string).append(", ");
                if (!missing.toString().equals(""))
                    missing = new StringBuilder(missing.substring(0, missing.length() - 2));

                if (missingIngredients.size() > 0) {
                    recipeMissingIngredients.setTextColor(Color.RED);
                    recipeMissingIngredients.setText(String.format("You need: %s for this recipe", missing.toString()));
                } else {
                    recipeMissingIngredients.setTextColor(Color.parseColor("#006400"));
                    recipeMissingIngredients.setText(context.getString(R.string.enoughIngredients));
                }

            }
        }.execute();
    }

}