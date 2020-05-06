package com.example.wed01.Fragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.wed01.AsyncHttp;
import com.example.wed01.R;

import org.json.JSONArray;

public class Fragment1 extends Fragment {
    ViewGroup viewGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);

        currentTemp = (TextView) viewGroup.findViewById(R.id.TextView);
        hopeTemp = (TextView) viewGroup.findViewById(R.id.hopeTemp);
        DataSendButton = (Button) viewGroup.findViewById(R.id.DataSendBtn);

        seekBar = (SeekBar) viewGroup.findViewById(R.id.SeekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                hopeTemp.setText("온도 조절 : " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        DataSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendArduinoData();
            }
        });

        TempThread thread = new TempThread();
        thread.setDaemon(true);
        thread.start();

        return viewGroup;
    }

    private void sendArduinoData() {
        int humidity = seekBar.getProgress();

        ContentValues contentValues = new ContentValues();
        contentValues.put("humidity", humidity);

        Log.d("MainActivity", String.valueOf(humidity));

        try {
            AsyncHttp asyncHttp = new AsyncHttp("arduino/data", contentValues);
            asyncHttp.execute();
        } catch (Exception e) { e.printStackTrace(); }
    }

    class TempThread extends Thread {
        @Override
        public void run() {
            while(true) {
                handler.sendEmptyMessage(0);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){   // Message id 가 0 이면
                try {
                    AsyncHttp asyncHttp = new AsyncHttp("phone/data", new ContentValues());
                    String result = asyncHttp.execute().get();
                    JSONArray jsonArray = new JSONArray(result);


                    currentTemp.setText(jsonArray.getJSONObject(jsonArray.length() - 1).getString("humidity"));
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    };

    TextView currentTemp;
    TextView hopeTemp;
    Button DataSendButton;
    SeekBar seekBar;
}
