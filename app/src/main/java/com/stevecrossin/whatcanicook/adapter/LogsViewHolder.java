package com.stevecrossin.whatcanicook.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.LogRecords;

/**
 * Implementation on below code was learned from https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder and other tutorials referenced in LogsAdapter.java
 **/
class LogsViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView log;

    LogsViewHolder(@NonNull View itemView) {
        super(itemView);
        /** Initialise the fields of the item view - in this case the log**/
        log = itemView.findViewById(R.id.log);
    }

    /**
     * Binds each row in the view holder to record in the log database and sets text in row to that database record.
     * @param logDatabase
     */
    void bindRow(LogRecords logDatabase) {
        log.setText(logDatabase.getLogs());
    }
}
