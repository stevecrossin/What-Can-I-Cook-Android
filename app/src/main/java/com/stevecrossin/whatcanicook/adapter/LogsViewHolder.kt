package com.stevecrossin.whatcanicook.adapter

import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View

import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.LogRecords

/**
 * ViewHolder, which dictates and sets the contents of the view
 */
internal class LogsViewHolder
/**
 * Initialise the fields of each row in the viewholder - in this case the log record
 */
(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val log: AppCompatTextView

    init {
        log = itemView.findViewById(R.id.log)
    }

    /**
     * Binds each row in the view holder to a record in the logs database database and sets text in row to that database record.
     */
    fun bindRow(logDatabase: LogRecords) {
        log.text = logDatabase.logs
    }
}
