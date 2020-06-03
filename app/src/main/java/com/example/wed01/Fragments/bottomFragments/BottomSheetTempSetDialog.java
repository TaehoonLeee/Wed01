package com.example.wed01.Fragments.bottomFragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wed01.AsyncHttp;
import com.example.wed01.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetTempSetDialog extends BottomSheetDialogFragment implements View.OnClickListener {


    public static BottomSheetTempSetDialog getInstance(String arduinoID, String Temp) {
        BottomSheetTempSetDialog dialog = new BottomSheetTempSetDialog();
        Bundle bundle = new Bundle();
        bundle.putString("ARDUINOID", arduinoID);
        bundle.putString("TEMP", Temp);
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            arduinoID = getArguments().getString("ARDUINOID");
            Temp = getArguments().getString("TEMP");
            Log.d("TempSet", arduinoID + ", " + Temp);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.arduino_set_temp, container, false);

        settingText = viewGroup.findViewById(R.id.setting);
        closeText = viewGroup.findViewById(R.id.close);

        settingText.setOnClickListener(this);
        closeText.setOnClickListener(this);

        return viewGroup;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.setting :
                Toast.makeText(getContext(), "setting", Toast.LENGTH_SHORT).show(); break;
            case R.id.close :
                Toast.makeText(getContext(), "close", Toast.LENGTH_SHORT).show(); break;
        }
    }

    private void tempSet() {
        int temp = Integer.parseInt(Temp);
        int ardID = Integer.parseInt(arduinoID);

        ContentValues contentValues = new ContentValues();
//        contentValues.put("ardID", ardID);
        contentValues.put("ardID", 1);
        contentValues.put("humidity", temp);

        Log.d("MainActivity", String.valueOf(temp));

        try {
            AsyncHttp asyncHttp = new AsyncHttp("phone/data", contentValues, "POST");
            asyncHttp.execute();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialog;
    }

    private String Temp, arduinoID;
    private ViewGroup viewGroup;
    private TextView closeText;
    private TextView settingText;
}