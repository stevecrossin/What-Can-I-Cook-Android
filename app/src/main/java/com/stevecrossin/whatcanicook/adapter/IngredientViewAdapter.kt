package com.stevecrossin.whatcanicook.adapter


import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Ingredient
import java.util.*

/**
 * IngredientView Adapter, which holds an ArrayList of the contents of the ingredients database
 */
class IngredientViewAdapter
/**
 * Creates a new instance of IngredientView Adapter, which contains the ingredients to be displayed in the adapter
 */
(private var ingredients: ArrayList<Ingredient>?) : RecyclerView.Adapter<IngredientViewHolder>() {

    /**
     * Initialise IngredientViewHolder with the Row View that is to be inflated. Uses ingredient_row as the layout for each row in the viewholder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): IngredientViewHolder {
        return IngredientViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.ingredient_row, viewGroup, false))
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the Ingredients are fetched and bound to the viewholder with the bindRow method.
     */
    override fun onBindViewHolder(ingredientViewHolder: IngredientViewHolder, @SuppressLint("RecyclerView") i: Int) {
        ingredientViewHolder.bindRow(ingredients!![i])
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of ingredients that exist
     */
    override fun getItemCount(): Int {
        return ingredients!!.size
    }

    /**
     * Updates the list with new values, and then notifies DB that the data has been changed
     */
    fun updateIngredients(ingredients: ArrayList<Ingredient>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }
}
