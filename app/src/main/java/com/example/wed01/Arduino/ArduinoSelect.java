package com.example.wed01.Arduino;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.wed01.AsyncHttp;
import com.example.wed01.MainActivityB;
import com.example.wed01.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArduinoSelect extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.arduino_select);

        spinner = (Spinner) findViewById(R.id.spinner);

        Intent prev = getIntent();

        Bundle bundle = prev.getExtras();

        userID = bundle.getString("USERID");
        Log.d("ardReg", userID);

        ArduinoSelectBtn = (Button) findViewById(R.id.selectArduino);

        spinnerArray = new ArrayList<>();

        getArduinoIDList();

        ArduinoSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String arduinoID = spinner.getSelectedItem().toString();

                Intent intent = new Intent(ArduinoSelect.this, MainActivityB.class);

                Bundle bundle = new Bundle();
                bundle.putString("ARDUINOID", arduinoID);
                bundle.putString("USERID", userID);
                intent.putExtras(bundle);

                startActivity(intent);
                finish();
            }
        });
    }

    private void getArduinoIDList() {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", userID);

            AsyncHttp asyncHttp = new AsyncHttp("phone/arduino", contentValues, "POST");
            String result = asyncHttp.execute().get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("ardList");

            if(jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String arduinoID = object.getString("ID");
                    spinnerArray.add(arduinoID);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
            else {
                Toast.makeText(getApplicationContext(), "현재 미등록된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    Spinner spinner;
    List<String> spinnerArray;
    Button ArduinoSelectBtn;
    String userID;
}
