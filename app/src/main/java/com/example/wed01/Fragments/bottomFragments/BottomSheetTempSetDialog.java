package com.example.wed01.Fragments.bottomFragments;

import android.content.ContentValues;
import android.content.DialogInterface;
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
import com.example.wed01.Fragments.Fragment1;
import com.example.wed01.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

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
                tempSet(); break;
            case R.id.close :
                dismiss(); break;
        }
    }

    private void tempSet() {
        if(arduinoID == null || arduinoID.trim().isEmpty()) {
            Toast.makeText(getContext(), "현재 등록된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            int temp = Integer.parseInt(Temp);

            ContentValues contentValues = new ContentValues();
//        contentValues.put("ardID", ardID);
            contentValues.put("ardID", arduinoID);
            contentValues.put("humidity", temp);

            Log.d("TempSetFunc", String.valueOf(temp));

            try {
                AsyncHttp asyncHttp = new AsyncHttp("phone/temp/" + arduinoID, contentValues, "POST");
                String result = asyncHttp.execute().get();
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getInt("resultCode") == 200) {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                } else {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Fragment1.showOffReveal();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Fragment1.showOffReveal();
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