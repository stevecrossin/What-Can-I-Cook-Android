package com.stevecrossin.whatcanicook.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Intolerance;

import java.util.ArrayList;
import java.util.List;

public class IntoleranceViewAdapter extends RecyclerView.Adapter<IntoleranceViewHolder> {
    private List<Intolerance> intoleranceList;

    /**
     * Creates a new instance of Intolerance Adapter, which contains the elements, being the dietary requirements to be displayed in the adapter and the rowclicklistener
     */
    public IntoleranceViewAdapter(ArrayList<Intolerance> intoleranceList) {
        this.intoleranceList = intoleranceList;
    }

    /**
     * Initialise IntoleranceViewHolder with the Row View that is to be inflated. Uses intolerance_row as the layout for each row in the viewholder
     */
    @NonNull
    @Override
    public IntoleranceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new IntoleranceViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.intolerance_row, viewGroup, false));
    }

    /**
     * Override method for the RecyclerView, which is called to display the data at the specified position.
     * First, the intolerances are fetched and bound to the viewholder with the bindRow method.
     */
    @Override
    public void onBindViewHolder(@NonNull final IntoleranceViewHolder intoleranceViewHolder, int i) {
        intoleranceViewHolder.bindRow(intoleranceList.get(i));
    }

    /**
     * Sets the size of the adapter which contains the number of items that will be shown in the RecyclerView.
     * This is based on the size (number) of intolerances that exist
     */
    @Override
    public int getItemCount() {
        return intoleranceList.size();
    }

    /**
     * Updates the list with new values, and then notifies DB that the data has been changed
     */
    public void updateIntolerances(List<Intolerance> intolerances) {
        this.intoleranceList = intolerances;
        notifyDataSetChanged();
    }

    /**
     * Interface that has the onRowClicked method to implement
     */
    public interface rowClickedListener {
        void onRowClicked(Intolerance into);
    }
}