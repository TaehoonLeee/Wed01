package com.example.wed01.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wed01.R;

public class Fragment3 extends Fragment {
    ViewGroup viewGroup;

    public static Fragment3 newInstance(String arduinoID) {
        Fragment3 fragment3 = new Fragment3();
        Bundle bundle = new Bundle();
        bundle.putString("ARDUINOID", arduinoID);
        fragment3.setArguments(bundle);

        return fragment3;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            arduinoID = getArguments().getString("ARDUINOID");
            Log.d("Fragment3", arduinoID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment3, container, false);

        arduinoIDText = (TextView) viewGroup.findViewById(R.id.arduinoIDText);
        arduinoIDText.setText(arduinoID);

        IDlinearlayout = (LinearLayout) viewGroup.findViewById(R.id.IDlinearlayout);
        IDlinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        menuImage = (ImageView) viewGroup.findViewById(R.id.menuImage);
        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return viewGroup;
    }

    TextView arduinoIDText;
    LinearLayout IDlinearlayout;
    ImageView menuImage;
    private String arduinoID;
}