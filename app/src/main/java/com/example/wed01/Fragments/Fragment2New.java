package com.example.wed01.Fragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wed01.AsyncHttp;
import com.example.wed01.RecyclerView.DividerItemDecoration;
import com.example.wed01.R;
import com.example.wed01.RecyclerView.GraphItem;
import com.example.wed01.RecyclerView.GraphRecyclerAdapter;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Fragment2New extends Fragment {
    ViewGroup viewGroup;

    public static Fragment2New newInstance(String arduinoID) {
        Fragment2New fragment2 = new Fragment2New();
        Bundle bundle = new Bundle();
        bundle.putString("ARDUINOID", arduinoID);
        fragment2.setArguments(bundle);

        return fragment2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            arduinoID = getArguments().getString("ARDUINOID");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment2_new, container, false);

        Toolbar toolbar = (Toolbar) viewGroup.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        if(activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) viewGroup.findViewById(R.id.collapsing_toolbar);
//        ctl.setTitle("Graph");

        chart = (ValueLineChart) viewGroup.findViewById(R.id.chart);
        chart.clearChart();

        recyclerView = (RecyclerView) viewGroup.findViewById(R.id.GraphRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.recycler_divider));

        thread = new GraphThread();
        thread.setDaemon(true);
        thread.start();

        return viewGroup;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }

    private void receiveRtData() {
        items = new ArrayList<>();

        try {
            AsyncHttp asyncHttp = new AsyncHttp("phone/data/" + arduinoID, new ContentValues(), "GET");
            String result = asyncHttp.execute().get();
            JSONArray jsonArray = new JSONArray(result);

            ValueLineSeries series = new ValueLineSeries();
            series.setColor(0xff56b7f1);

            if (jsonArray.length() >= 10) {
                for (int i = jsonArray.length() - 10; i <= jsonArray.length()-1; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    time = object.getString("time");
                    String date = time.substring(0, 10);
                    String newTime = time.substring(11, 16);
                    String newHour = String.valueOf(Integer.parseInt(newTime.substring(0, 2)) + 9);
                    String newMinute = newTime.substring(newTime.length()-3, newTime.length());

                    newTime = newHour+newMinute;

                    humidity = BigDecimal.valueOf(object.getDouble("humidity")).floatValue();
                    Log.d("Fragment2", String.valueOf(humidity));
                    series.addPoint(new ValueLinePoint(newTime, humidity));

                    GraphItem item = new GraphItem(humidity, newTime);
                    items.add(item);
                }
            }
            else {
                for(int i = 0; i <= jsonArray.length()-1; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    time = object.getString("time");
                    String newTime = time.substring(11, 16);
                    String newHour = String.valueOf(Integer.parseInt(newTime.substring(0, 2)) + 9);
                    String newMinute = newTime.substring(newTime.length()-3, newTime.length());
                    newTime = newHour+newMinute;

                    humidity = BigDecimal.valueOf(object.getDouble("humidity")).floatValue();
                    Log.d("Fragment2", String.valueOf(humidity));
                    series.addPoint(new ValueLinePoint(newTime, humidity));

                    GraphItem item = new GraphItem(humidity, newTime);
                    items.add(item);
                }
            }

            chart.addSeries(series);
            chart.startAnimation();

            adapter = new GraphRecyclerAdapter(items);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) { e.printStackTrace(); }
    }

    class GraphThread extends Thread {
        boolean state = true;
        @Override
        public void run() {
            while(state) {
                handler.sendEmptyMessage(0);

                try {
                    Thread.sleep(30000);
                    if(Thread.interrupted()) {
                        state = false;
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    state = false;
                } finally {
                    Log.d("TempThread", "Thread is dead");
                }
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){   // Message id 가 0 이면
                chart.clearChart();
                receiveRtData();
            }
        }
    };

    private String arduinoID;
    private float humidity;
    private String time;
    private ValueLineChart chart;
    GraphThread thread;
    private RecyclerView recyclerView;
    private ArrayList<GraphItem> items;
    private GraphRecyclerAdapter adapter;
}
