package com.stevecrossin.whatcanicook.adapter

import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Ingredient
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo

/**
 * ViewAdapter, which dictates and sets the contents of the view adapter
 */
class PantryRecycleViewAdapter
/**
 * Initialise the fields of each row in the viewholder - in this case the categories and list of ingredients
 */
(private val categories: MutableList<Ingredient>) : RecyclerView.Adapter<CategoryViewHolder>() {
    private var context: Context? = null

    /**
     * Initialise CategoryViewHolder with the Row View that is to be inflated. Uses pantry_list_item as the layout for each row in the viewholder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CategoryViewHolder {
        context = viewGroup.context
        return CategoryViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.pantry_list_item, viewGroup, false))
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the ingredients are fetched and bound to the viewholder with the bindRow method. Then, an event listener is setup for each row is clicked.
     *
     *
     * Additionally, an onClick listener exists for the categoryImage UI element, which in this case controls the removal of ingredients from pantry. When this element is clicked, it gets the position of the row in the adapter and gets the ingredient name.
     *
     *
     * It will then pass this via an Async task to the removeIngredientFromPantry method, and the savePantrytoUserDB method (as pantry ingredients are also stored in user DB), in AppRepo which will in turn perform
     * the removal and updates. Once this is completed, the adapter will be notified of the removal, and the ingredient
     * will be removed from the ViewHolder and the list is updated without the removed item.
     */
    override fun onBindViewHolder(viewHolder: CategoryViewHolder, i: Int) {
        val ingredient = categories[i]
        viewHolder.categoryName.text = ingredient.ingredientName
        viewHolder.categoryImage.setOnClickListener {
            object : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg voids: Void): Void? {
                    val repo = AppDataRepo(context)
                    repo.removeIngredientFromPantry(ingredient.ingredientID)
                    repo.savePantryToUserDB()
                    return null
                }

                override fun onPostExecute(aVoid: Void) {
                    super.onPostExecute(aVoid)
                    categories.removeAt(viewHolder.adapterPosition)
                    notifyItemRemoved(viewHolder.adapterPosition)
                }
            }.execute()
        }
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of ingredients that are in the pantry
     */
    override fun getItemCount(): Int {
        return categories.size
    }

    /**
     * Updates the list with new values, and then notifies DB that the data has been changed. This is called by loadPantry in Pantry class
     */
    fun updateCategories(categories: List<Ingredient>) {
        this.categories.addAll(categories)
        notifyDataSetChanged()
    }

    /**
     * Updates the list with new values, and then notifies DB that the data has been changed. This is called by onPostExecute in Pantry class
     */
    fun updateCategories(category: Ingredient) {
        this.categories.add(category)
        notifyDataSetChanged()
    }

}