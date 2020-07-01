package com.example.wed01.Fragments.bottomFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wed01.Arduino.ArduinoRegister;
import com.example.wed01.MainActivityB;
import com.example.wed01.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static android.app.Activity.RESULT_OK;

public class BottomSheetMenuDialog extends BottomSheetDialogFragment implements View.OnClickListener {


    public static BottomSheetMenuDialog getInstance() { return new BottomSheetMenuDialog(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.bottom_sheet_menu, container, false);

        plusText = viewGroup.findViewById(R.id.bottom_sheet_plus);
        infoText = viewGroup.findViewById(R.id.bottom_sheet_info);
        logOutText = viewGroup.findViewById(R.id.bottom_sheet_logout);

        plusText.setOnClickListener(this);
        infoText.setOnClickListener(this);
        logOutText.setOnClickListener(this);

        return viewGroup;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bottom_sheet_plus :
                registerArduino(); break;
            case R.id.bottom_sheet_info :
                Toast.makeText(getContext(), "info", Toast.LENGTH_SHORT).show(); break;
            case R.id.bottom_sheet_logout :
                Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show(); break;
        }
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String arduinoID = data.getStringExtra("arduinoID");
                MainActivityB.setArduinoId(arduinoID);
            }
        }
    }

    public void registerArduino() {
        String userID = MainActivityB.getUserID();

        Intent intent = new Intent(getActivity(), ArduinoRegister.class);
        Bundle bundle = new Bundle();

        bundle.putBoolean("isNew", false);
        bundle.putString("USERID", userID);
        intent.putExtras(bundle);

        startActivityForResult(intent, 1);
    }

    private ViewGroup viewGroup;
    private TextView plusText;
    private TextView infoText;
    private TextView logOutText;
}
