package com.example.wed01.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wed01.Fragments.bottomFragments.BottomSheetArduinoDialog;
import com.example.wed01.Fragments.bottomFragments.BottomSheetMenuDialog;
import com.example.wed01.MainActivity;
import com.example.wed01.MainActivityB;
import com.example.wed01.R;

public class Fragment4 extends Fragment {
    ViewGroup viewGroup;

    public static Fragment4 newInstance(String arduinoID) {
        Fragment4 fragment4 = new Fragment4();
        Bundle bundle = new Bundle();
        bundle.putString("ARDUINOID", arduinoID);
        fragment4.setArguments(bundle);

        return fragment4;
    }

    public static Fragment4 newInstance(String arduinoID, String userID) {
        Fragment4 fragment4 = new Fragment4();
        Bundle bundle = new Bundle();
        bundle.putString("ARDUINOID", arduinoID);
        bundle.putString("USERID", userID);
        fragment4.setArguments(bundle);

        return fragment4;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            arduinoID = getArguments().getString("ARDUINOID");
            Log.d("Fragment4", arduinoID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment4, container, false);

        arduinoIDText = (TextView) viewGroup.findViewById(R.id.arduinoIDText);
        arduinoIDText.setText(arduinoID);

        IDlinearlayout = (LinearLayout) viewGroup.findViewById(R.id.IDlinearlayout);
        IDlinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetArduinoDialog bottomSheetArduinoDialog = BottomSheetArduinoDialog.getInstance(arduinoID, Fragment1.currentTempText.getText().toString().trim(), Fragment1.waterTemp.getText().toString().trim());
                bottomSheetArduinoDialog.show(getActivity().getSupportFragmentManager(), "tag");
            }
        });

        menuImage = (ImageView) viewGroup.findViewById(R.id.menuImage);
        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetMenuDialog bottomSheetMenuDialog = BottomSheetMenuDialog.getInstance();
                bottomSheetMenuDialog.show(getActivity().getSupportFragmentManager(), "tag");
            }
        });

        return viewGroup;
    }

    private String arduinoID;
    TextView arduinoIDText;
    LinearLayout IDlinearlayout;
    ImageView menuImage;
}
