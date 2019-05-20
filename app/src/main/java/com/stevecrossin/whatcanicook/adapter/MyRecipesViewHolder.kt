package com.stevecrossin.whatcanicook.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View

import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Recipe

/**
 * ViewHolder, which dictates and sets the contents of the view
 */
class MyRecipesViewHolder
/**
 * Initialise the fields of each row in the viewholder - in this case the name of the recipe, the image, and the button to remove the recipe.
 */
internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val recipeName: AppCompatTextView
    private val recipeImage: AppCompatImageView
    internal var recipeRemoveButton: AppCompatImageView

    init {

        recipeName = itemView.findViewById(R.id.recipe_name)
        recipeImage = itemView.findViewById(R.id.recipe_img)
        recipeRemoveButton = itemView.findViewById(R.id.remove)
    }

    /**
     * Binds each row in the view holder to a record in the recipes database and sets text in row to that respective database record
     * Also sets the recipe image in that row based on the drawable resource for that recipe in the database, if it exists.
     */
    @SuppressLint("StaticFieldLeak")
    internal fun bindRow(recipe: Recipe, context: Context) {
        recipeName.text = recipe.recipeName
        val drawableResourceId = context.resources.getIdentifier(recipe.recipeImage, "drawable", context.packageName)
        recipeImage.setImageResource(drawableResourceId)

    }
}