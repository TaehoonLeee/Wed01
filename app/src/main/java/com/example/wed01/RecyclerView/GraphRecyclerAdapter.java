package com.example.wed01.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wed01.R;

import java.util.ArrayList;

public class GraphRecyclerAdapter extends RecyclerView.Adapter<GraphRecyclerViewHolder> {

    public GraphRecyclerAdapter(Context _context, ArrayList<GraphItem> _items) {
        this.context = _context;
        this.items = _items;
    }

    public GraphRecyclerAdapter(ArrayList<GraphItem> _items) {
        this.items = _items;
    }

    @NonNull
    @Override
    public GraphRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_graph, parent, false);

        return new GraphRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GraphRecyclerViewHolder holder, int position) {
        holder.temperature.setText(String.valueOf(items.get(position).getHumidity()) + " \u2103");
        holder.date.setText(items.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    Context context;
    ArrayList<GraphItem> items;
}
