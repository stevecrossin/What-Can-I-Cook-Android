package com.stevecrossin.whatcanicook.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.LogRecords;

import java.util.ArrayList;

/**
 * Logs Adapter, based off Recycler View. This holds an ArrayList of the contents of the log database.
 * I learned how to Implement and use RecyclerView were learned from the following resources:
 * https://developer.android.com/reference/android/support/v7/widget/RecyclerView - RecyclerView resource from Android
 * https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0 - Android Room with a View
 * https://www.youtube.com/watch?v=9ZCK5BOU6wk&index=29- YouTube - RecyclerView - through to video 32
 */
public class LogsAdapter extends RecyclerView.Adapter<LogsViewHolder> {
    private ArrayList<LogRecords> logDatabases;

    public LogsAdapter(ArrayList<LogRecords> logs) {
        this.logDatabases = logs;
    }


    @NonNull
    @Override
    public LogsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /**Initialise ContactViewHolder with the Row View to be inflated**/
        return new LogsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.log_row, viewGroup, false));
    }

    /**
     * this method is called to update each row in the recyclerView. @contactViewHolder is for referencing the view on the row.
     *
     * @position tells us the row position in the RecyclerView
     **/
    @Override
    public void onBindViewHolder(@NonNull LogsViewHolder logsViewHolder, final int i) {
        logsViewHolder.bindRow(logDatabases.get(i));

    }

    /**
     * set the size of this adapter. This controls the number of items shown on the recyclerView
     **/
    @Override
    public int getItemCount() {
        return logDatabases.size();
    }


    /**
     * Updates logs db when ever an update occurs
     **/
    public void updateLogs(ArrayList<LogRecords> logDatabases) {
        this.logDatabases = logDatabases;
        notifyDataSetChanged();
    }

}
