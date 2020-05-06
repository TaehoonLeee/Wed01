package com.example.wed01.memberManagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wed01.R;

public class find extends AppCompatActivity {

    EditText edtId;
    EditText edtEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);

//        edtId = findViewById(R.id.etID);
        edtEmail = findViewById(R.id.etEmail);

        Button btn_cancel = (Button) findViewById(R.id.btncancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_find = (Button) findViewById(R.id.btnfind);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpMsg();
            }
        });
    }

    private void find() {

    }

    private void popUpMsg() {
        if(edtId.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(),"ID를 입력해 주세요",Toast.LENGTH_SHORT).show();
        }
        else if(edtEmail.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(),"이메일을 입력해 주세요",Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()){
            Toast.makeText(getApplicationContext(),"이메일 형식이 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
        }
        else {
            find();
            finish();
        }
    }
}
