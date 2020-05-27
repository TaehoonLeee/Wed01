package com.example.wed01.memberManagement;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wed01.Arduino.ArduinoRegister;
import com.example.wed01.AsyncHttp;
import com.example.wed01.MainActivityB;
import com.example.wed01.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class login extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        editId = findViewById(R.id.editText_ID);
        editPw = findViewById(R.id.editText_PWD);
        imageView = findViewById(R.id.imageView);

        imageView.setColorFilter(Color.parseColor("#4e4e4e"), PorterDuff.Mode.SRC_IN);

        Button btn_reg = (Button) findViewById(R.id.btn_register);
        btn_reg.setPaintFlags(btn_reg.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), register.class);
                startActivity(intent);
            }
        });

        Button btn_find = (Button) findViewById(R.id.btn_find);
        btn_find.setPaintFlags(btn_find.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), find.class);
                startActivity(intent);
            }
        });

        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
//                Intent intent = new Intent(login.this, MainActivityB.class);
//                startActivity(intent);
//                finish();
            }
        });
    }

    public void linearOnClick(View v) {
        inputMethodManager.hideSoftInputFromWindow(editId.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(editPw.getWindowToken(), 0);
    }

    private void Login() {
        String userId = editId.getText().toString().trim();
        String password = editPw.getText().toString().trim();
//        String userEmail = "";

        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", userId);
        contentValues.put("PW", password);
//        contentValues.put("EMAIL", userEmail);

        try {
            AsyncHttp asyncHttp = new AsyncHttp("user/login", contentValues, "POST");
            String result = asyncHttp.execute().get();
            JSONObject object = new JSONObject(result);

            if(object.getInt("resultCode") == 200) {
                String msg = object.getString("msg");
                Toast.makeText(login.this, msg, Toast.LENGTH_SHORT).show();

                JSONArray jsonArray = object.getJSONArray("arduino");

                if( jsonArray.length() != 0) {
                    Intent intent = new Intent(login.this, MainActivityB.class);

                    Bundle bundle = new Bundle();

                    bundle.putString("ARDUINOID", arduinoID);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(login.this, "등록된 기기가 없습니다. 기기를 등록해주세요.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(login.this, ArduinoRegister.class);

                    Bundle bundle = new Bundle();

                    bundle.putString("ID", userId);
                    bundle.putBoolean("isNew", true);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            }
            else {
                String msg = object.getString("msg");
                Toast.makeText(login.this, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    String arduinoID;
    ImageView imageView;
    EditText editId;
    EditText editPw;
    InputMethodManager inputMethodManager;
}
