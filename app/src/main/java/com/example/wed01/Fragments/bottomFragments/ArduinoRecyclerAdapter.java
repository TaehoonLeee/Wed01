package com.example.wed01.Fragments.bottomFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wed01.MainActivityB;
import com.example.wed01.R;

import java.util.ArrayList;

public class ArduinoRecyclerAdapter extends RecyclerView.Adapter<ArduinoRecyclerViewHolder> {

    public ArduinoRecyclerAdapter(Context _context, ArrayList<ArduinoItem> _items) {
        this.context = _context;
        this.items = _items;
    }

    @NonNull
    @Override
    public ArduinoRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_arduino, parent, false);

        return new ArduinoRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArduinoRecyclerViewHolder holder, int position) {
        holder.arduinoID.setText(items.get(position).getArduinoID());
        holder.arduinoSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetArduinoDialog.toggleSwitch();
                Intent intent = new Intent(context, MainActivityB.class);
                Bundle bundle = new Bundle();

                bundle.putString("USERID", MainActivityB.getUserID());
                bundle.putString("ARDUINOID", items.get(position).getArduinoID());

                intent.putExtras(bundle);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    Context context;
    ArrayList<ArduinoItem> items;
}
