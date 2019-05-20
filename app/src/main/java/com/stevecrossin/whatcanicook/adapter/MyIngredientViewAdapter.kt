package com.stevecrossin.whatcanicook.adapter

import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Ingredient
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import java.util.*

/**
 * MyIngredientsView Adapter, which holds an ArrayList of the contents of the database
 */
class MyIngredientViewAdapter
/**
 * Creates a new instance of MyIngredientView Adapter, which contains the ingredients to be displayed in the adapter and the rowClickListener
 */
(private var ingredients: ArrayList<Ingredient>?, private val rowClickedListener: MyIngredientViewAdapter.rowClickedListener) : RecyclerView.Adapter<MyIngredientViewHolder>() {
    private var context: Context? = null

    /**
     * Initialise MyIngredientViewHolder with the Row View that is to be inflated. Uses selected_ingredient_row as the layout for each row in the viewholder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyIngredientViewHolder {
        context = viewGroup.context
        return MyIngredientViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.selected_ingredient_row, viewGroup, false))
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the select ingredients are fetched and bound to the viewholder with the bindRow method. Then, an event listener is setup for each row is clicked.
     *
     *
     * Additionally, an onClick listener exists for the closeImage UI element. This UI element controls the removal of ingredients.When this element is clicked, it gets the position of the row in the adapter and gets the ingredient name.
     *
     *
     * It will then pass this via an Async task to the deSelectIngredient method
     * in AppRepo which will in turn perform the deselection of the ingredient in the database. Once this is completed, the adapter will be notified of the removal, and the ingredient
     * will be removed from the ViewHolder and the list is updated without the removed item.
     */
    override fun onBindViewHolder(myIngredientViewHolder: MyIngredientViewHolder, i: Int) {
        val ingredient = ingredients!![i]
        myIngredientViewHolder.bindRow(ingredient)
        myIngredientViewHolder.itemView.setOnClickListener { rowClickedListener.onRowClicked(ingredient) }

        myIngredientViewHolder.closeImage.setOnClickListener {
            object : AsyncTask<Void, Void, Void>() {

                override fun doInBackground(vararg voids: Void): Void? {
                    val repo = AppDataRepo(context)
                    myIngredientViewHolder.adapterPosition
                    repo.deselectIngredient(ingredient.ingredientName)
                    return null
                }


                override fun onPostExecute(aVoid: Void) {
                    super.onPostExecute(aVoid)
                    ingredients!!.removeAt(myIngredientViewHolder.adapterPosition)
                    notifyItemRemoved(myIngredientViewHolder.adapterPosition)
                }
            }.execute()
        }

    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of selected ingredients that exist
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

    /**
     * Interface that has the onRowClicked method to implement
     */
    interface rowClickedListener {
        fun onRowClicked(ingredient: Ingredient)
    }
}
