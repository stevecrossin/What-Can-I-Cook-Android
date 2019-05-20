package com.stevecrossin.whatcanicook.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Ingredient
import java.util.*

/**
 * AutoComplete Adapter for the Category Ingredients autocomplete, which controls the population and function of the autocomplete textview.
 */
class CategoryIngredientsAutocompleteAdapter
/**
 * Creates the instance of the AutoComplete adapter, and declares the variables and UI elements to the adapter
 */
(private val context: Context, private val ingredients: List<Ingredient>) : ArrayAdapter<Ingredient>(context, R.layout.pantry_autocomplete_item, ingredients) {
    private val resourceId: Int = R.layout.pantry_autocomplete_item
    private val filteredList: MutableList<Ingredient>
    private val tempList: List<Ingredient>

    /**
     * Method that defines how to filter the ingredients list, based on the user input. Creates a new instance of filter, then
     * takes their character inputs and then converts that to a string, and then will get all the ingredient names
     * based on that text input.
     *
     *
     * Once this has been completed, results will be published in the AutoComplete Adapter and are filtered based on the results entered
     * E.g. if user types "chicken" in the field, all ingredients with "chicken" will be displayed, all without it will be hidden.
     */
    private val nameFilter = object : Filter() {

        override fun convertResultToString(resultValue: Any): CharSequence? {
            return (resultValue as Ingredient).ingredientName
        }

        /**
         * As per above filter method, this publishes the results of the filter that was performed. If there are any results, the ingredient will be added to the
         * filtered list, otherwise the list will be cleared (zero results displayed)
         */
        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            val filteredList = results.values as List<Ingredient>
            if (results.count > 0) {
                clear()
                for (ingredient in filteredList) {
                    add(ingredient)
                }
                notifyDataSetChanged()
            }
        }

        /**
         * Starts an asynchronous filtering operation. Calling this method cancels all previous non-executed filtering requests and posts a new
         * filtering request that will be executed later. The constraint in this method is the text entered - if the constraint isnt equal to null, the list will be cleared.
         * If it equals null (no constraint exists) then ingredients will be added to the filtered list. This list will then become "results" with the count known. These filtered results will
         * then be returned.
         */
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            val filterResults = Filter.FilterResults()
            if (constraint != null) {
                filteredList.clear()
                for (ingredient in tempList) {
                    if (ingredient.ingredientName!!.contains(constraint)) {
                        filteredList.add(ingredient)
                    }
                }
                filterResults.values = filteredList
                filterResults.count = filteredList.size
            }
            return filterResults
        }
    }

    init {
        filteredList = ArrayList()
        tempList = ArrayList(ingredients)
    }

    /**
     * Layout inflater
     * Inflates the layout and reuse the view if it has already been inflated then
     * Sets the text of each row in the layout based on the position of elements in the list, gets the ingredient name, and then returns that view
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val inflater = (context as Activity).layoutInflater
        if (convertView == null) {
            convertView = inflater.inflate(resourceId, parent, false)
        }
        (convertView as TextView).text = ingredients[position].ingredientName

        return convertView
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of categories that exist
     */
    override fun getCount(): Int {
        return ingredients.size
    }

    /**
     * Gets the items in the adapter, and returns the ingredients into their relevant position into that adapter
     */
    override fun getItem(position: Int): Ingredient? {
        return ingredients[position]
    }

    /**
     * Gets an instance of the ingredient filter
     */
    override fun getFilter(): Filter {

        return nameFilter
    }


}