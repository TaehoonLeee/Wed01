package com.example.wed01.memberManagement;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wed01.AsyncHttp;
import com.example.wed01.MainActivity;
import com.example.wed01.MainActivityB;
import com.example.wed01.R;

import org.json.JSONObject;

public class login extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        editId = findViewById(R.id.editText_ID);
        editPw = findViewById(R.id.editText_PWD);

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
                Intent intent = new Intent(getApplicationContext(), MainActivityB.class);
                startActivity(intent);
            }
        });

        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
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
            AsyncHttp asyncHttp = new AsyncHttp("login", contentValues);
            String result = asyncHttp.execute().get();
            JSONObject object = new JSONObject(result);

            if(object.getInt("resultCode") == 200) {
                String msg = object.getString("msg");
                Toast.makeText(login.this, msg, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                String msg = object.getString("msg");
                Toast.makeText(login.this, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    EditText editId;
    EditText editPw;
    InputMethodManager inputMethodManager;
}
