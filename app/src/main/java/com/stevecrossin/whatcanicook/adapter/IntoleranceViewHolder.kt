package com.stevecrossin.whatcanicook.adapter

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Intolerance
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import java.util.*

/**
 * ViewHolder, which dictates and sets the contents of the view
 */
internal class IntoleranceViewHolder
/**
 * Initialise the fields of each row in the viewholder - in this case the name of the intolerance and the checkbox.
 * Also creates a new instance of the AppDataRepo. This view is also not recyclable to prevent scrolling issues
 */
(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val intoleranceName: AppCompatTextView
    private val intoleranceCheckBox: AppCompatCheckBox
    private val repository: AppDataRepo

    init {
        this.setIsRecyclable(false)
        intoleranceName = itemView.findViewById(R.id.intolerance_name)
        intoleranceCheckBox = itemView.findViewById(R.id.intolerance_checkBox)
        repository = AppDataRepo(itemView.context)
    }

    /**
     * Binds each row in the view holder to a record in the intolerances database and sets text in row to that database record.
     * Checks the intolerance database to see if the intolerance is noted as "selected", and if so, sets the checkbox to "checked"
     * otherwise the intolerance is not checked. Also contains instance of checkchangedlistener - once the intolerance is selected/deselected
     * this text is taken and converted to a string
     */
    fun bindRow(intolerance: Intolerance) {
        intoleranceName.text = intolerance.intoleranceName
        intoleranceCheckBox.isChecked = intolerance.isIntoleranceSelected

        intoleranceCheckBox.setOnCheckedChangeListener { buttonView, isChecked -> intoleranceSelected(isChecked, intoleranceName.text.toString()) }
    }

    /**
     * This method is caled when an intolerance has been updated in the activity. This will perform an AsyncTask to call the database to change the value
     * of that intolerance to either selected (intoleranceSelected=1), or deselected (intoleranceSelected=0), based on the value of the string passed from bindrow() and whether the checkbox was ticked or unselected.
     * After this task is completed, the view will also be refreshed with this updated value (checked or not checked)
     */
    @SuppressLint("StaticFieldLeak")
    private fun intoleranceSelected(isSelected: Boolean, intoleranceName: String) {

        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                val list = ArrayList(repository.getIntoleranceByName(intoleranceName))
                if (isSelected) {
                    repository.excludeIntolerance(intoleranceName)
                    repository.addTolerance(intoleranceName)
                    for (intolerance in list) {
                        repository.excludeIngredient(intolerance.ingredientName)
                        repository.excludeRecipe(intolerance.ingredientName)
                    }
                } else {
                    repository.includeIntolerance(intoleranceName)
                    repository.removeTolerance(intoleranceName)
                    for (intolerance in list) {
                        repository.includeIngredient(intolerance.ingredientName)
                        repository.includeRecipe(intolerance.ingredientName)
                    }
                }
                return null
            }

        }.execute()

    }
}