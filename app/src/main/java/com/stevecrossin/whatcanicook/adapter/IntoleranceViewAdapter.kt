package com.stevecrossin.whatcanicook.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Intolerance
import java.util.*

/**
 * IntoleranceView Adapter, which holds an ArrayList of the contents of the intolerances database
 */
class IntoleranceViewAdapter
/**
 * Creates a new instance of Intolerance Adapter, which contains the elements, being the dietary requirements to be displayed in the adapter and the rowclicklistener
 */
(intoleranceList: ArrayList<Intolerance>) : RecyclerView.Adapter<IntoleranceViewHolder>() {
    private var intoleranceList: List<Intolerance>? = null

    init {
        this.intoleranceList = intoleranceList
    }

    /**
     * Initialise IntoleranceViewHolder with the Row View that is to be inflated. Uses intolerance_row as the layout for each row in the viewholder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): IntoleranceViewHolder {
        return IntoleranceViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.intolerance_row, viewGroup, false))
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the intolerances are fetched and bound to the viewholder with the bindRow method.
     */
    override fun onBindViewHolder(intoleranceViewHolder: IntoleranceViewHolder, i: Int) {
        intoleranceViewHolder.bindRow(intoleranceList!![i])
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of intolerances that exist
     */
    override fun getItemCount(): Int {
        return intoleranceList!!.size
    }

    /**
     * Updates the list with new values, and then notifies DB that the data has been changed
     */
    fun updateIntolerances(intolerances: List<Intolerance>) {
        this.intoleranceList = intolerances
        notifyDataSetChanged()
    }

    /**
     * Interface that has the onRowClicked method to implement
     */
    interface rowClickedListener {
        fun onRowClicked(into: Intolerance)
    }
}