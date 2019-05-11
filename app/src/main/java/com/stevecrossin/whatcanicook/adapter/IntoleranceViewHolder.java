package com.stevecrossin.whatcanicook.adapter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

class IntoleranceViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "IngredientViewHolder";
    private AppCompatTextView intoleranceName;
    private AppCompatCheckBox intoleranceCheckBox;
    private AppDataRepo repository;

    IntoleranceViewHolder(@NonNull View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
        intoleranceName = itemView.findViewById(R.id.intolerance_name);
        intoleranceCheckBox = itemView.findViewById(R.id.intolerance_checkBox);
        repository = new AppDataRepo(itemView.getContext());
    }

    void bindRow(Intolerance intolerance) {
        intoleranceName.setText(intolerance.getIntoleranceName());
        if (intolerance.isIntoleranceSelected())
            intoleranceCheckBox.setChecked(true);
        else
            intoleranceCheckBox.setChecked(false);

        intoleranceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: " + intoleranceName.getText());
                intoleranceSelected(isChecked, intoleranceName.getText().toString());
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void intoleranceSelected(final boolean isSelected, final String intoleranceName) {
        /*
        This method is responsible for taking action when an intolerance is updated in the Intolerance activity
        When a user clicks on the on/off slider to mark it as active/inactive, in a background task, this information will be passed to the Users.updateUser method
        which will take appropriate action to update the user database.
        The intolerances database will also be updated to mark the relevant intolerance as active/inactive.
        The Intolerance activity UI will also be refreshed so that the intolerance clicked is made active/inactive.
        */

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<Intolerance> list = new ArrayList<>(repository.getIntoleranceByName(intoleranceName));
                if (isSelected) {
                    repository.excludeIntolerance(intoleranceName);
                    repository.addTolerance(intoleranceName);
                    for (Intolerance intolerance : list) {
                        repository.excludeIngredient(intolerance.getIngredientName());
                        repository.excludeRecipe(intolerance.getIngredientName());
                        Log.d(TAG, "Exclude ingredient: " + intolerance.getIngredientName());
                    }
                } else {
                    repository.includeIntolerance(intoleranceName);
                    repository.removeTolerance(intoleranceName);
                    for (Intolerance intolerance : list) {
                        repository.includeIngredient(intolerance.getIngredientName());
                        repository.includeRecipe(intolerance.getIngredientName());
                        Log.d(TAG, "Include ingredient: " + intolerance.getIngredientName());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();

    }
}