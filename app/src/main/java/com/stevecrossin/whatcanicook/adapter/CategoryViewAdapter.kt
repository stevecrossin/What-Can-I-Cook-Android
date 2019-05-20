package com.stevecrossin.whatcanicook.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Ingredient

/**
 * CategoryView Adapter, which holds an List of the contents of the categories database
 */
class CategoryViewAdapter
/**
 * Creates a new instance of CategoryView Adapter, which contains the categories in the adapter
 */
(private var categories: List<Ingredient>?, rowClickedListener: CategoryViewAdapter.rowClickedListener) : RecyclerView.Adapter<CategoryViewHolder>() {
    private val rowClickedListener: rowClickedListener = rowClickedListener

    /**
     * Initialise CategoryViewHolder with the Row View that is to be inflated. Uses category_row as the layout for each row in the viewholder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.category_row, viewGroup, false))
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the Ingredient Categories is fetched and bound to the viewholder with the bindRow method. Then, an onClick listener is setup for each row,
     * which when clicked gets the ingredients for that category
     */
    override fun onBindViewHolder(ingredientViewHolder: CategoryViewHolder, @SuppressLint("RecyclerView") i: Int) {
        ingredientViewHolder.bindRow(categories!![i], ingredientViewHolder.itemView.context)
        ingredientViewHolder.itemView.setOnClickListener { rowClickedListener.onRowClicked(categories!![i].ingredientCategory) }
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of categories that exist
     */
    override fun getItemCount(): Int {
        return categories!!.size
    }

    /**
     * Updates the list with new values, and then notifies DB that the data has been changed
     */
    fun updateCategories(categories: List<Ingredient>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    /**
     * Interface that has the onRowClicked method to implement
     */
    interface rowClickedListener {
        fun onRowClicked(category: String?)
    }
}