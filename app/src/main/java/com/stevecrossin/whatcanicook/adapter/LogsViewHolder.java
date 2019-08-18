package com.stevecrossin.whatcanicook.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.LogRecords;

/**
 * ViewHolder, which dictates and sets the contents of the view
 */
class LogsViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView log;

    /**
     * Initialise the fields of each row in the viewholder - in this case the log record
     */
    LogsViewHolder(@NonNull View itemView) {
        super(itemView);
        log = itemView.findViewById(R.id.log);
    }

    /**
     * Binds each row in the view holder to a record in the logs database database and sets text in row to that database record.
     */
    void bindRow(LogRecords logDatabase) {
        log.setText(logDatabase.getLogs());
    }
}
