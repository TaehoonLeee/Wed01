package com.example.wed01.Arduino;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wed01.AsyncHttp;
import com.example.wed01.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArduinoDelete extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.arduino_delete);

        spinner = (Spinner) findViewById(R.id.spinner);

        Intent prev = getIntent();

        Bundle bundle = prev.getExtras();

        isNew = bundle.getBoolean("isNew");
        userID = bundle.getString("USERID");
        arduinoID = bundle.getString("ARDUINOID");

        ArduinoDeleteBtn = (Button) findViewById(R.id.deleteArduino);

        spinnerArray = new ArrayList<>();

        getArduinoIDList();

        ArduinoDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArduino("");
            }
        });
    }

    public void getArduinoIDList() {
        try {
            AsyncHttp asyncHttp = new AsyncHttp("arduino/unregistered", new ContentValues(), "GET");
            String result = asyncHttp.execute().get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("ardList");

            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String arduinoID = object.getString("ID");
                    spinnerArray.add(arduinoID);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "현재 미등록된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteArduino(String arduinoID) {

    }

    Spinner spinner;
    boolean isNew = false;
    List<String> spinnerArray;
    String arduinoID, userID;
    Button ArduinoDeleteBtn;
}