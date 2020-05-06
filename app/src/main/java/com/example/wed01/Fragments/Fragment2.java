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

import com.example.wed01.AsyncHttp;
import com.example.wed01.R;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Fragment2 extends Fragment {
    ViewGroup viewGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        chart = (ValueLineChart) viewGroup.findViewById(R.id.chart);

        chart.clearChart();

        GraphThread thread = new GraphThread();
        thread.setDaemon(true);
        thread.start();

        return viewGroup;
    }

    private void receiveRtData() {
        try {
            AsyncHttp asyncHttp = new AsyncHttp("phone/data", new ContentValues());
            String result = asyncHttp.execute().get();
            JSONArray jsonArray = new JSONArray(result);

            ValueLineSeries series = new ValueLineSeries();
            series.setColor(0xff56b7f1);

            if (jsonArray.length() >= 10) {
                for (int i = jsonArray.length() - 10; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    time = object.getString("time");
                    humidity = BigDecimal.valueOf(object.getDouble("humidity")).floatValue();
                    Log.d("Fragment2", String.valueOf(humidity));
                    series.addPoint(new ValueLinePoint(time, humidity));
                }
            }
            else {
                for(int i = 0; i < jsonArray.length()-1; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    time = object.getString("time");
                    humidity = BigDecimal.valueOf(object.getDouble("humidity")).floatValue();
                    Log.d("Fragment2", String.valueOf(humidity));
                    series.addPoint(new ValueLinePoint(time, humidity));
                }
            }

            chart.addSeries(series);
            chart.startAnimation();
        } catch (Exception e) { e.printStackTrace(); }
    }

    class GraphThread extends Thread {
        @Override
        public void run() {
            while(true) {
                handler.sendEmptyMessage(0);

                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){   // Message id 가 0 이면
                receiveRtData();
            }
        }
    };

    private float humidity;
    private String time;
    private ValueLineChart chart;
}
