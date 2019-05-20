package com.stevecrossin.whatcanicook.adapter

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View

import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Ingredient

/**
 * ViewHolder, which dictates and sets the contents of the view
 */
class CategoryViewHolder
/**
 * Initialise the fields of each row in the viewholder - in this case the name of the category, and the icon for that category.
 */
(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * Getter methods - gets category name and categoryimage and returns the result
     */
    val categoryName: AppCompatTextView
    val categoryImage: AppCompatImageView

    init {
        categoryName = itemView.findViewById(R.id.category_name)
        categoryImage = itemView.findViewById(R.id.category_img)
    }

    /**
     * Binds each row in the view holder to a record in the ingredients database and sets text in row to that database record.
     * Sets the image for that category based on the drawable resource for that category as per the database
     */
    fun bindRow(category: Ingredient, context: Context) {
        categoryName.text = category.ingredientCategory
        val drawableResourceId = context.resources.getIdentifier(category.categoryIconName, "drawable", context.packageName)
        categoryImage.setImageResource(drawableResourceId)

    }
}