package com.example.wed01.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wed01.R;

public class ArduinoRecyclerViewHolder extends RecyclerView.ViewHolder {

    public ArduinoRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.arduinoID = itemView.findViewById(R.id.arduinoID);
        this.arduinoSwitch = itemView.findViewById(R.id.arduinoSwitch);
    }

    public TextView arduinoID;
    public Switch arduinoSwitch;
}
