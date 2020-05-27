package com.example.wed01;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.wed01.Fragments.Fragment1;
import com.example.wed01.Fragments.Fragment2;
import com.example.wed01.Fragments.Fragment3;

public class MainActivityB extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainb);
        bottomNavigationView = findViewById(R.id.navigationView);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        Intent prev = getIntent();

        Bundle bundle = prev.getExtras();

        arduinoId = bundle.getString("ARDUINOID");

        Log.d("MainActivityB", arduinoId);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment1).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.tab1: {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        Fragment fragment = Fragment1.newInstance(arduinoId);
                        fragmentTransaction.replace(R.id.frameLayout, fragment);
                        fragmentTransaction.commit();

                        return true;
                    }

                    case R.id.tab2: {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        Fragment fragment = Fragment2.newInstance(arduinoId);
                        fragmentTransaction.replace(R.id.frameLayout, fragment);
                        fragmentTransaction.commit();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment2).commitAllowingStateLoss();

                        return true;
                    }

                    case R.id.tab3: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment3).commitAllowingStateLoss();

                        return true;
                    }

                    default: return false;
                }
            }
        });
    }

    String arduinoId;
    BottomNavigationView bottomNavigationView;
    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
}
