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
import com.example.wed01.MainActivity;
import com.example.wed01.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArduinoRegister extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.arduino_register);

        ArduinoRegisterBtn = (Button) findViewById(R.id.registerArduino);

        spinnerArray = new ArrayList<>();
        spinnerArray.add("ID1");
        spinnerArray.add("ID2");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        ArduinoRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String arduinoID = spinner.getSelectedItem().toString();
                Intent intent = new Intent();
                intent.putExtra("arduinoID", arduinoID);
                finish();
            }
        });
    }

    public void getArduinoIDList() {
        try {
            AsyncHttp asyncHttp = new AsyncHttp("login", new ContentValues(), "GET");
            String result = asyncHttp.execute().get();
            JSONObject object = new JSONObject(result);

            if(object.getInt("resultCode") == 200) {
                String msg = object.getString("msg");
                Toast.makeText(ArduinoRegister.this, msg, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ArduinoRegister.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                String msg = object.getString("msg");
                Toast.makeText(ArduinoRegister.this, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    List<String> spinnerArray;
    Button ArduinoRegisterBtn;
}
