package com.stevecrossin.whatcanicook.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoComplete Adapter for the Pantry autocomplete., which controls the population and function of the autocomplete textview.
 */
public class PantryAutocompleteAdapter extends ArrayAdapter<Ingredient> {

    private List<Ingredient> ingredients;
    private Context context;
    private int resourceId;
    private List<Ingredient> filteredList, tempList;

    /**
     * Method that defines how to filter the ingredients list, based on the user input. Creates a new instance of filter, then
     * takes their character inputs and then converts that to a string, and then will get all the ingredient names
     * based on that text input.
     * <p>
     * Once this has been completed, results will be published in the AutoComplete Adapter and are filtered based on the results entered
     * E.g. if user types "chicken" in the field, all ingredients with "chicken" will be displayed, all without it will be hidden.
     */
    private Filter nameFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Ingredient) resultValue).getIngredientName();
        }

        /**
         * As per above filter method, this publishes the results of the filter that was performed. If there are any results, the ingredient will be added to the
         * filtered list, otherwise the list will be cleared (zero results displayed)
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Ingredient> filteredList = (List<Ingredient>) results.values;
            if (results.count > 0) {
                clear();
                for (Ingredient ingredient : filteredList) {
                    add(ingredient);
                }
                notifyDataSetChanged();
            }
        }

        /**
         * Starts an asynchronous filtering operation. Calling this method cancels all previous non-executed filtering requests and posts a new
         * filtering request that will be executed later. The constraint in this method is the text entered - if the constraint isnt equal to null, the list will be cleared.
         * If it equals null (no constraint exists) then ingredients will be added to the filtered list. This list will then become "results" with the count known. These filtered results will
         * then be returned.
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                filteredList.clear();
                for (Ingredient ingredient : tempList) {
                    if (ingredient.getIngredientName().contains(constraint)) {
                        filteredList.add(ingredient);
                    }
                }
                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
            }
            return filterResults;
        }
    };

    /**
     * Creates the instance of the AutoComplete adapter, and declares the variables and UI elements to the adapter
     */
    public PantryAutocompleteAdapter(@NonNull Context context, @NonNull List<Ingredient> ingredients) {
        super(context, R.layout.pantry_autocomplete_item, ingredients);
        this.ingredients = ingredients;
        this.context = context;
        this.resourceId = R.layout.pantry_autocomplete_item;
        filteredList = new ArrayList<>();
        tempList = new ArrayList<>(ingredients);
    }

    /**
     * Layout inflater
     * Inflates the layout and reuse the view if it has already been inflated then
     * Sets the text of each row in the layout based on the position of elements in the list, gets the ingredient name, and then returns that view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(resourceId, parent, false);
        }
        ((TextView) convertView).setText(ingredients.get(position).getIngredientName());

        return convertView;
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of ingredients that exist
     */
    @Override
    public int getCount() {
        return ingredients.size();
    }

    /**
     * Gets the items in the adapter, and returns the ingredients into their relevant position into that adapter
     ***/
    @Nullable
    @Override
    public Ingredient getItem(int position) {
        return ingredients.get(position);
    }

    /**
     * Gets an instance of the ingredient filter
     */
    @NonNull
    @Override
    public Filter getFilter() {

        return nameFilter;
    }


}
