package com.stevecrossin.whatcanicook.adapter

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Ingredient
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import java.util.*

/**
 * ViewHolder, which dictates and sets the contents of the view
 */
internal class IngredientViewHolder
/**
 * Initialise the fields of each row in the viewholder - in this case the name of the ingredient and the checkbox.
 * Also creates a new instance of the AppDataRepo. This view is also not recyclable to prevent scrolling issues
 */
(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ingredientName: AppCompatTextView
    private val ingredientCheckBox: AppCompatCheckBox
    private val repository: AppDataRepo

    init {
        this.setIsRecyclable(false)
        ingredientName = itemView.findViewById(R.id.ingredient_name)
        ingredientCheckBox = itemView.findViewById(R.id.ingredient_checkBox)
        repository = AppDataRepo(itemView.context)
    }

    /**
     * Binds each row in the view holder to a record in the ingredients database and sets text in row to that database record.
     * Checks the ingredients database to see if the ingredient is noted as "selected", and if so, sets the checkbox to "checked"
     * otherwise the ingredient is not checked. Also contains instance of checkchangedlistener - once the ingredient is selected/deselected
     * this text is taken and converted to a string
     */
    fun bindRow(ingredient: Ingredient) {
        ingredientName.text = ingredient.ingredientName
        ingredientCheckBox.isChecked = ingredient.isIngredientSelected
        ingredientCheckBox.setOnCheckedChangeListener { buttonView, isChecked -> ingredientSelected(isChecked, ingredientName.text.toString()) }
    }

    /**
     * This method is caled when an ingredient has been selected. This will perform an AsyncTask to call the database to change the value
     * of that ingredient to either selected (ingredientSelected=1), or deselected (ingredientSelected=0), based on the value of the string passed from bindrow() and whether the checkbox was ticked or unselected.
     */
    @SuppressLint("StaticFieldLeak")
    private fun ingredientSelected(isSelected: Boolean, ingredientName: String) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                val list = ArrayList(repository.getIngredientsByName(ingredientName))
                if (isSelected) {
                    for (ingredient in list) {
                        repository.selectIngredient(ingredient.ingredientName)
                    }
                } else {
                    for (ingredient in list) {
                        repository.deselectIngredient(ingredient.ingredientName)
                    }
                }
                return null
            }
        }.execute()

    }
}
