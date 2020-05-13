package com.example.wed01;

import android.content.ContentValues;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTemp = (TextView) findViewById(R.id.TextView);
        hopeTemp = (TextView) findViewById(R.id.hopeTemp);
        chart = (ValueLineChart) findViewById(R.id.chart);
        DataReceiveButton = (Button) findViewById(R.id.button);
        DataSendButton = (Button) findViewById(R.id.DataSendBtn);

        chart.clearChart();

        seekBar = (SeekBar) findViewById(R.id.SeekBar);

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

        DataReceiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chart.clearChart();
                receiveRtData();
            }
        });

        DataSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendArduinoData();
            }
        });
    }

    private void receiveRtData() {
        try {
            AsyncHttp asyncHttp = new AsyncHttp("phone/data", new ContentValues(), "POST");
            String result = asyncHttp.execute().get();
            JSONArray jsonArray = new JSONArray(result);

            ValueLineSeries series = new ValueLineSeries();
            series.setColor(0xff56b7f1);

            if (jsonArray.length() >= 10) {
                for (int i = jsonArray.length() - 10; i <= jsonArray.length() - 1; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String time = object.getString("time");
                    float humidity = BigDecimal.valueOf(object.getDouble("humidity")).floatValue();
                    Log.d("MainActivity", String.valueOf(humidity));
                    series.addPoint(new ValueLinePoint(time, humidity)); // key값 변경
                }
            }
            else {
                for(int i = 0; i < jsonArray.length()-1; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String time = object.getString("time");
                    float humidity = BigDecimal.valueOf(object.getDouble("humidity")).floatValue();
                    Log.d("MainActivity", String.valueOf(humidity));
                    series.addPoint(new ValueLinePoint(time, humidity)); // key값 변경
                }
            }

            chart.addSeries(series);
            chart.startAnimation();

            currentTemp.setText("현재 온도 : " + jsonArray.getJSONObject(jsonArray.length()-1).getString("humidity"));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void sendArduinoData() {
        int humidity = seekBar.getProgress();

        ContentValues contentValues = new ContentValues();
        contentValues.put("humidity", humidity);

        Log.d("MainActivity", String.valueOf(humidity));

        try {
            AsyncHttp asyncHttp = new AsyncHttp("arduino/data", contentValues, "POST");
            asyncHttp.execute();
        } catch (Exception e) { e.printStackTrace(); }
    }

    ValueLineChart chart;
    TextView currentTemp, hopeTemp;
    Button DataReceiveButton, DataSendButton;
    SeekBar seekBar;
}