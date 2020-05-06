package com.example.wed01.memberManagement;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wed01.AsyncHttp;
import com.example.wed01.R;

import org.json.JSONObject;

public class register extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        editId = findViewById(R.id.etID);
        editPassword = findViewById(R.id.etPassword);
        editEmail = findViewById(R.id.etEmail);
        editPasswordConfirm = findViewById(R.id.etPasswordConfirm);

        btnDone = findViewById(R.id.btnDone);
        btnDone.setPaintFlags(btnDone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popUpMsg();
            }
        });

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setPaintFlags(btnCancel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) { finish(); }
        });
    }

    public void linearOnClick(View v) {
        inputMethodManager.hideSoftInputFromWindow(editPassword.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(editEmail.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(editPasswordConfirm.getWindowToken(), 0);
    }

    public void popUpMsg() {
        if(editEmail.getText().toString().length()==0) {
            Toast.makeText(getApplicationContext(),"Email을 입력해 주세요",Toast.LENGTH_SHORT).show();
        }
        else if(editPassword.getText().toString().length()==0) {
            Toast.makeText(getApplicationContext(),"패스워드를 입력해 주세요",Toast.LENGTH_SHORT).show();
        }
        else if(! (editPassword.getText().toString().equals(editPasswordConfirm.getText().toString()))) {
            Toast.makeText(getApplicationContext(), "패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
//        else if(editEmail.getText().toString().length()==0){
//            Toast.makeText(getApplicationContext(),"이메일을 입력해 주세요",Toast.LENGTH_SHORT).show();
//        }
//        else if(!Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches()){
//            Toast.makeText(getApplicationContext(),"이메일 형식이 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
//        }
        else {
            createUser();
        }
    }

    private void createUser() {
        String ID = editEmail.getText().toString();
        String PW = editPassword.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", ID);
        contentValues.put("PW", PW);

        try {
            AsyncHttp asyncHttp = new AsyncHttp("register", contentValues);
            String result = asyncHttp.execute().get();
            JSONObject object = new JSONObject(result);

            if(object.getInt("resultCode") == 200) {
                String msg = object.getString("msg");
                Toast.makeText(register.this, msg, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
                finish();
            }
            else {
                String msg = object.getString("msg");
                Toast.makeText(register.this, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) { e.printStackTrace(); }

    }

    private EditText editPassword, editPasswordConfirm, editEmail;
    private Button btnDone, btnCancel;
    InputMethodManager inputMethodManager;
}
