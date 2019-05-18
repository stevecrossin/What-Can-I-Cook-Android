package com.stevecrossin.whatcanicook.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.LogRecords;

import java.util.ArrayList;

/**
 * Logs Adapter, which holds an ArrayList of the contents of the logs database
 */
public class LogsAdapter extends RecyclerView.Adapter<LogsViewHolder> {
    private ArrayList<LogRecords> logDatabases;

    /**
     * Creates a new instance of LogsAdapter, which contains the logs to be displayed in the adapter from the logDB
     */
    public LogsAdapter(ArrayList<LogRecords> logs) {
        this.logDatabases = logs;
    }


    /**
     * Initialise the LogsView with the Row View that is to be inflated. Uses log_row as the layout for each row in the viewholder
     */
    @NonNull
    @Override
    public LogsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new LogsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.log_row, viewGroup, false));
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * Log records are fetched and bound to the viewholder with the bindRow method.
     */
    @Override
    public void onBindViewHolder(@NonNull LogsViewHolder logsViewHolder, final int i) {
        logsViewHolder.bindRow(logDatabases.get(i));
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of log entries that exist in the database
     */
    @Override
    public int getItemCount() {
        return logDatabases.size();
    }


    /**
     * Updates the list with new values, and then notifies DB that the data has been changed
     */
    public void updateLogs(ArrayList<LogRecords> logDatabases) {
        this.logDatabases = logDatabases;
        notifyDataSetChanged();
    }

}
