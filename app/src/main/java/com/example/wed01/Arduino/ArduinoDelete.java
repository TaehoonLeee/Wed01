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

import com.example.wed01.AsyncHttp;
import com.example.wed01.MainActivity;
import com.example.wed01.MainActivityB;
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
        Log.d("ardDelete", userID + ", " + arduinoID);

        ArduinoDeleteBtn = (Button) findViewById(R.id.deleteArduino);

        spinnerArray = new ArrayList<>();

        getArduinoIDList();

        ArduinoDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String arduinoID = spinner.getSelectedItem().toString();
                deleteArduino(arduinoID);
                if(arduinoID.equals(MainActivityB.getArduinoId())) {
                    Toast.makeText(getApplicationContext(), "새롭게 사용할 아두이노 기기를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ArduinoDelete.this, ArduinoSelect.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("USERID", MainActivityB.getUserID());
                    intent.putExtras(bundle);

                    startActivity(intent);
                    setResult(RESULT_FIRST_USER, intent);
                    finish();
                }
                else {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    public void getArduinoIDList() {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", MainActivityB.getUserID());

            AsyncHttp asyncHttp = new AsyncHttp("phone/arduino", contentValues, "POST");
            String result = asyncHttp.execute().get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("ardList");
            Log.d("BottomArduinoDialog", "test3");

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
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteArduino(String arduinoID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", userID);
        contentValues.put("ARDUINOID", arduinoID);

        try {
            AsyncHttp asyncHttp = new AsyncHttp("phone/disconnect", contentValues, "POST");
            String result = asyncHttp.execute().get();
            JSONObject jsonObject = new JSONObject(result);

            if(jsonObject.getInt("resultCode") == 200) {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
            else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    Spinner spinner;
    boolean isNew = false;
    List<String> spinnerArray;
    String arduinoID, userID;
    Button ArduinoDeleteBtn;
}