package com.stevecrossin.whatcanicook.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Recipe
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import java.util.*

/**
 * MyRecipes View Adapter, which holds an ArrayList of the contents of the database with saved recipes
 */
class MyRecipesViewAdapter
/**
 * Initialise the fields of each row in the viewholder - in this case the name of the recipe, and onClick listeners to detemine if the row was clicked
 * (which causes the UI to navigate to the recipe itself,  and the close element, which causes the recipe to be removed  - these are all per the defined methods in the SavedRecipes class.
 */
(private var recipes: ArrayList<Recipe>?, private val rowClickedListener: MyRecipesViewAdapter.rowClickedListener, private val removeClickedListener: MyRecipesViewAdapter.removeClickedListener) : RecyclerView.Adapter<MyRecipesViewHolder>() {
    private var context: Context? = null
    private val repo = AppDataRepo(context)

    /**
     * Initialise MyRecipesViewHolder with the Row View that is to be inflated. Uses saved_recipe_row as the layout for each row in the viewholder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyRecipesViewHolder {
        context = viewGroup.context
        return MyRecipesViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.saved_recipe_row, viewGroup, false))
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the select recipes are fetched and bound to the viewholder with the bindRow method. Then, an event listener is setup for each row is clicked.
     *
     *
     * Additionally, an onClick listener exists for the removeRecipe UI element. This UI element controls the removal of recipes. When this element is clicked, it gets the position of the row in the adapter and gets the recipe name.
     */
    override fun onBindViewHolder(recipeViewHolder: MyRecipesViewHolder, @SuppressLint("RecyclerView") i: Int) {
        val recipe = recipes!![i]
        recipeViewHolder.bindRow(recipe, recipeViewHolder.itemView.context)
        recipeViewHolder.itemView.setOnClickListener { rowClickedListener.onRowClicked(recipes!![i]) }
        recipeViewHolder.recipeRemoveButton.setOnClickListener { removeClickedListener.onRemoveClicked(recipes!![i], recipeViewHolder) }
    }

    /**
     * Task to remove the recipe. This is performed in the background via AsyncTask to either the unSaveRecipe method, or the removeRecipe method in AppDB, based on whether the recipe was already in the DB
     * but "saved", or a custom recipe that was added by the user. These operations will then perform the relevant database function, and then once completed, the recipe will be removed from the ViewHolder and the list will be updated without the removed item
     */

    @SuppressLint("StaticFieldLeak")
    fun removeRecipefromSaved(recipe: Recipe, recipeViewHolder: MyRecipesViewHolder) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                if (recipe.isSaved) {
                    repo.unSaveRecipe(recipe.recipeId)
                } else {
                    repo.removeRecipe(recipe.recipeId)
                }
                return null
            }

            override fun onPostExecute(aVoid: Void) {
                super.onPostExecute(aVoid)
                recipes!!.removeAt(recipeViewHolder.adapterPosition)
                notifyItemRemoved(recipeViewHolder.adapterPosition)
            }
        }.execute()
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of saved recipes that exist in the database
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
     * Interface that has the onRowClicked method to implement
     */
    interface rowClickedListener {
        fun onRowClicked(recipe: Recipe)
    }

    /**
     * Interface that has the onRemoveClicked method to implement
     */
    interface removeClickedListener {
        fun onRemoveClicked(recipe: Recipe, recipeViewHolder: MyRecipesViewHolder)
    }
}
