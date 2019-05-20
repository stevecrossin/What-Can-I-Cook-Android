package com.stevecrossin.whatcanicook.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.LogRecords
import java.util.*

/**
 * Logs Adapter, which holds an ArrayList of the contents of the logs database
 */
class LogsAdapter
/**
 * Creates a new instance of LogsAdapter, which contains the logs to be displayed in the adapter from the logDB
 */
(private var logDatabases: ArrayList<LogRecords>?) : RecyclerView.Adapter<LogsViewHolder>() {


    /**
     * Initialise the LogsView with the Row View that is to be inflated. Uses log_row as the layout for each row in the viewholder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): LogsViewHolder {

        return LogsViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.log_row, viewGroup, false))
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * Log records are fetched and bound to the viewholder with the bindRow method.
     */
    override fun onBindViewHolder(logsViewHolder: LogsViewHolder, i: Int) {
        logsViewHolder.bindRow(logDatabases!![i])
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of log entries that exist in the database
     */
    override fun getItemCount(): Int {
        return logDatabases!!.size
    }


    /**
     * Updates the list with new values, and then notifies DB that the data has been changed
     */
    fun updateLogs(logDatabases: ArrayList<LogRecords>) {
        this.logDatabases = logDatabases
        notifyDataSetChanged()
    }

}
