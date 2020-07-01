package com.example.wed01.RecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wed01.R;

public class GraphRecyclerViewHolder extends RecyclerView.ViewHolder {

    public GraphRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.temperature = itemView.findViewById(R.id.temprature);
        this.date = itemView.findViewById(R.id.date);
    }

    public TextView temperature;
    public TextView date;
}
