package com.example.wed01.Arduino;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.wed01.R;

import java.util.ArrayList;
import java.util.List;

public class ArduinoRegister extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.arduino_register);

        ArduinoRegisterBtn = (Button) findViewById(R.id.registerArduino);

        List<String> spinnerArray = new ArrayList<>();
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

    }

    Button ArduinoRegisterBtn;
}
