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

public class CategoryIngredientsAutocompleteAdapter extends ArrayAdapter<Ingredient> {

    private List<Ingredient> ingredients;
    private Context context;
    private int resourceId;
    private List<Ingredient> filteredList,tempList;


    public CategoryIngredientsAutocompleteAdapter(@NonNull Context context, @NonNull List<Ingredient> ingredients) {
        super(context, R.layout.pantry_autocomplete_item, ingredients);
        this.ingredients = ingredients;
        this.context = context;
        this.resourceId = R.layout.pantry_autocomplete_item;
        filteredList = new ArrayList<Ingredient>();
        tempList = new ArrayList<>(ingredients);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        TextView view = (TextView) inflater.inflate(resourceId, parent, false);
        view.setText(ingredients.get(position).getIngredientName());
        return view;
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Nullable
    @Override
    public Ingredient getItem(int position) {
        return ingredients.get(position);
    }

    @Override
    public Filter getFilter() {

        return nameFilter;
    }

    private Filter nameFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Ingredient) resultValue).getIngredientName();
            return str;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Ingredient> filteredList = (List<Ingredient>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Ingredient ingredient : filteredList) {
                    add(ingredient);
                }
                notifyDataSetChanged();
            }
        }

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


}
