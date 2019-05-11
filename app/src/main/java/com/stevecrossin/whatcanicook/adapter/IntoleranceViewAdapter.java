package com.stevecrossin.whatcanicook.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Intolerance;

import java.util.ArrayList;
import java.util.List;

public class IntoleranceViewAdapter extends RecyclerView.Adapter<IntoleranceViewHolder> {
    private List<Intolerance> intoleranceList;
    private IntoleranceViewAdapter.rowClickedListener rowClickedListener;


    public IntoleranceViewAdapter(ArrayList<Intolerance> intoleranceList, IntoleranceViewAdapter.rowClickedListener rowClickedListener) {
        this.intoleranceList = intoleranceList;
        this.rowClickedListener = rowClickedListener;
    }

    @NonNull
    @Override
    public IntoleranceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new IntoleranceViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.intolerance_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final IntoleranceViewHolder intoleranceViewHolder, int i) {
        intoleranceViewHolder.bindRow(intoleranceList.get(i));
        intoleranceViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickedListener.onRowClicked(intoleranceList.get(intoleranceViewHolder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return intoleranceList.size();
    }

    public void updateIntolerances(List<Intolerance> intolerances) {
        this.intoleranceList = intolerances;
        notifyDataSetChanged();
    }

    public interface rowClickedListener {
        void onRowClicked(Intolerance into);
    }
}