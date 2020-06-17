package com.example.wed01.Fragments.bottomFragments;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wed01.AsyncHttp;
import com.example.wed01.MainActivityB;
import com.example.wed01.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BottomSheetArduinoDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    public static BottomSheetArduinoDialog getInstance(String arduinoID, String temperatrue, String humidity) {
        BottomSheetArduinoDialog bottomSheetArduinoDialog = new BottomSheetArduinoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("ARDUINOID", arduinoID);
        bundle.putString("TEMPERATURE", temperatrue);
        bundle.putString("HUMIDITY", humidity);

        bottomSheetArduinoDialog.setArguments(bundle);
        return bottomSheetArduinoDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.bottom_sheet_arduinoid, container, false);

        mainArduinoID = viewGroup.findViewById(R.id.arduinoID);
        tempTextView = viewGroup.findViewById(R.id.temprature);
        humidityTextView = viewGroup.findViewById(R.id.humidity);
        recyclerView = viewGroup.findViewById(R.id.ArduinoRecyclerview);
        mainArduinoSwitch = viewGroup.findViewById(R.id.arduinoSwitch);

        mainArduinoID.setText(getArguments().getString("ARDUINOID"));
        tempTextView.setText("현재 온도 : " + getArguments().getString("TEMPERATURE"));
        humidityTextView.setText("현재 습도 : " + getArguments().getString("HUMIDITY"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainArduinoSwitch.setChecked(true);
        getArduinoIDs();

//        plusText.setOnClickListener(this);
//        infoText.setOnClickListener(this);
//        logOutText.setOnClickListener(this);

        return viewGroup;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
//            case R.id.bottom_sheet_plus :
//                Toast.makeText(getContext(), "plus", Toast.LENGTH_SHORT).show(); break;
//            case R.id.bottom_sheet_info :
//                Toast.makeText(getContext(), "info", Toast.LENGTH_SHORT).show(); break;
//            case R.id.bottom_sheet_logout :
//                Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show(); break;
        }
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialog;
    }

    public void getArduinoIDs() {
        Log.d("BottomArduinoDialog", "test");
        items = new ArrayList<>();

        try {
            Log.d("BottomArduinoDialog", "test2");
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", MainActivityB.getUserID());
            Log.d("BottomSheetArduino", MainActivityB.getUserID());

            AsyncHttp asyncHttp = new AsyncHttp("phone/arduino", contentValues, "POST");
            String result = asyncHttp.execute().get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("ardList");
            Log.d("BottomArduinoDialog", "test3");

            if(jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String arduinoID = object.getString("ID");
                    ArduinoItem arduinoItem = new ArduinoItem(arduinoID);
                    items.add(arduinoItem);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        adapter = new ArduinoRecyclerAdapter((MainActivityB)getActivity(), items);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static void toggleSwitch() {
        mainArduinoSwitch.toggle();
    }

    private ViewGroup viewGroup;
    private TextView mainArduinoID, tempTextView, humidityTextView;
    static private Switch mainArduinoSwitch;
    private RecyclerView recyclerView;
    private ArrayList<ArduinoItem> items;
    private ArduinoRecyclerAdapter adapter;
}
