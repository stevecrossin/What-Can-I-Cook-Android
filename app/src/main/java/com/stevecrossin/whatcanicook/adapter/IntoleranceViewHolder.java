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
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

class IntoleranceViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView intoleranceName;
    private AppCompatCheckBox intoleranceCheckBox;
    private AppDataRepo repository;

    /**
     * Initialise the fields of each row in the viewholder - in this case the name of the intolerance and the checkbox.
     * Also creates a new instance of the AppDataRepo. This view is also not recyclable to prevent scrolling issues
     */
    IntoleranceViewHolder(@NonNull View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
        intoleranceName = itemView.findViewById(R.id.intolerance_name);
        intoleranceCheckBox = itemView.findViewById(R.id.intolerance_checkBox);
        repository = new AppDataRepo(itemView.getContext());
    }

    /**
     * Binds each row in the view holder to a record in the intolerances database and sets text in row to that database record.
     * Checks the intolerance database to see if the intolerance is noted as "selected", and if so, sets the checkbox to "checked"
     * otherwise the intolerance is not checked. Also contains instance of checkchangedlistener - once the intolerance is selected/deselected
     * this text is taken and converted to a string
     **/
    void bindRow(Intolerance intolerance) {
        intoleranceName.setText(intolerance.getIntoleranceName());
        if (intolerance.isIntoleranceSelected())
            intoleranceCheckBox.setChecked(true);
        else
            intoleranceCheckBox.setChecked(false);

        intoleranceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, intoleranceName.getText().toString());
            }
        });
    }

    /**
     * This method is caled when an intolerance has been updated in the activity. This will perform an AsyncTask to call the database to change the value
     * of that intolerance to either selected (intoleranceSelected=1), or deselected (intoleranceSelected=0), based on the value of the string passed from bindrow() and whether the checkbox was ticked or unselected.
     * After this task is completed, the view will also be refreshed with this updated value (checked or not checked)
     */
    @SuppressLint("StaticFieldLeak")
    private void intoleranceSelected(final boolean isSelected, final String intoleranceName) {

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
                    }
                } else {
                    repository.includeIntolerance(intoleranceName);
                    repository.removeTolerance(intoleranceName);
                    for (Intolerance intolerance : list) {
                        repository.includeIngredient(intolerance.getIngredientName());
                        repository.includeRecipe(intolerance.getIngredientName());
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