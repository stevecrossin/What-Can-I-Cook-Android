package com.stevecrossin.whatcanicook.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Recipe
import java.util.*

/**
 * Recipes View Adapter, which holds an ArrayList of the contents of the recipes database
 */
class RecipeViewAdapter
/**
 * Creates a new instance of RecipeViewAdapter, which contains the recipes to be displayed in the adapter and the rowclicklistener
 */
(private var recipes: ArrayList<Recipe>?, private val rowClickedListener: RecipeViewAdapter.rowClickedListener) : RecyclerView.Adapter<RecipeViewHolder>() {

    /**
     * Initialise RecipeViewHolder with the Row View that is to be inflated. Uses recipe_row as the layout for each row in the viewholder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecipeViewHolder {
        return RecipeViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.recipe_row, viewGroup, false))
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the recipes are fetched and bound to the viewholder with the bindRow method. Then, an event listener is setup for each row when clicked.
     */
    override fun onBindViewHolder(recipeViewHolder: RecipeViewHolder, @SuppressLint("RecyclerView") i: Int) {
        recipeViewHolder.bindRow(recipes!![i], recipeViewHolder.itemView.context)
        recipeViewHolder.itemView.setOnClickListener { rowClickedListener.onRowClicked(recipes!![i]) }
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of recipes that exist
     */
    override fun getItemCount(): Int {
        return recipes!!.size
    }

    /**
     * Updates the list with new values, and then notifies DB that the data has been changed
     */
    fun updateRecipes(recipes: ArrayList<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    /**
     * Override method that gets the ID of each element in the adapter.
     */
    override fun getItemId(position: Int): Long {
        val recipe = recipes!![position]
        return recipe.recipeId.toLong()
    }

    /**
     * Interface that has the onRowClicked method to implement
     */
    interface rowClickedListener {
        fun onRowClicked(recipe: Recipe)
    }
}
