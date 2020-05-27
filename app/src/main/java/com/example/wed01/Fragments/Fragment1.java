package com.example.wed01.Fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wed01.Arduino.ArduinoRegister;
import com.example.wed01.AsyncHttp;
import com.example.wed01.R;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuGravity;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class Fragment1 extends Fragment{
    ViewGroup viewGroup;


    public static Fragment1 newInstance(String arduinoID) {
        Fragment1 fragment1 = new Fragment1();
        Bundle bundle = new Bundle();
        bundle.putString("ARDUINOID", arduinoID);
        fragment1.setArguments(bundle);

        return fragment1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            arduinoID = getArguments().getString("ARDUINOID");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);

        setHasOptionsMenu(true);

        currentTemp = (TextView) viewGroup.findViewById(R.id.TextView);
        hopeTemp = (TextView) viewGroup.findViewById(R.id.hopeTemp);
        DataSendButton = (Button) viewGroup.findViewById(R.id.DataSendBtn);
        ArduinoRegisterBtn = (Button) viewGroup.findViewById(R.id.ArduinoRegister);
        powerImage = (ImageView) viewGroup.findViewById(R.id.powerImage);

        powerDrawable = getResources().getDrawable(R.drawable.power);
        map = ((BitmapDrawable)powerDrawable).getBitmap();
        powerImage.setImageBitmap(map);

        croller = (Croller) viewGroup.findViewById(R.id.croller);
        initCroller();

//        initContextMenu();
        initPowerMenu();

        croller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                hopeTemp.setText("온도 조절 : " + progress);
                changeBitmap(progress);
            }
        });

        DataSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendArduinoData();
            }
        });

        TempThread thread = new TempThread();
        thread.setDaemon(true);
        thread.start();

        ArduinoRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNew = false;
                Intent intent = new Intent(getActivity(), ArduinoRegister.class);
                Bundle bundle = new Bundle();

                bundle.putBoolean("isNew", isNew);
                intent.putExtras(bundle);

                startActivityForResult(intent, 1);
            }
        });

        return viewGroup;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_top,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();

        switch (curId) {
            case R.id.top_menu : contextMenuDialogFragment.show(getChildFragmentManager(), ContextMenuDialogFragment.TAG);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                arduinoID = data.getStringExtra("arduinoID");
            }
        }
    }

    private void sendArduinoData() {
        int humidity = croller.getProgress();

        ContentValues contentValues = new ContentValues();
        contentValues.put("humidity", humidity);

        Log.d("MainActivity", String.valueOf(humidity));

        try {
            AsyncHttp asyncHttp = new AsyncHttp("arduino/data", contentValues, "POST");
            asyncHttp.execute();
        } catch (Exception e) { e.printStackTrace(); }
    }

    class TempThread extends Thread {
        @Override
        public void run() {
            while(true) {
                handler.sendEmptyMessage(0);

                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){   // Message id 가 0 이면
                try {
                    AsyncHttp asyncHttp = new AsyncHttp("phone/data/1", new ContentValues(), "GET");
                    String result = asyncHttp.execute().get();
                    JSONArray jsonArray = new JSONArray(result);


                    currentTemp.setText(jsonArray.getJSONObject(jsonArray.length() - 1).getString("humidity"));
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    };

    private void initCroller() {
        croller.setIndicatorWidth(10);
        croller.setBackCircleColor(Color.TRANSPARENT);
        croller.setMainCircleColor(Color.TRANSPARENT);
        croller.setMax(100);
        croller.setStartOffset(45);
        croller.setIsContinuous(true);
        croller.setLabelColor(Color.TRANSPARENT);
        croller.setProgressPrimaryColor(Color.RED);
        croller.setIndicatorColor(Color.TRANSPARENT);
        croller.setProgressSecondaryColor(Color.parseColor("#EEEEEE"));
        croller.setProgressPrimaryStrokeWidth(10);
    }

    private void changeBitmap(int progress) {
        Bitmap tmpMap = map.copy(Bitmap.Config.ARGB_8888, true);

        int side = map.getWidth()-1;

        double progressToMaping = (double) (side/100d) * progress;
        int row = (int) progressToMaping;

        Log.d("row", String.valueOf(row));

        for(int i = 0; i < row; i++) {
            for(int j = 0; j < side; j++) {
                if(tmpMap.getPixel(j,side-i) == 0) tmpMap.setPixel(j, side-i, Color.TRANSPARENT);
                else tmpMap.setPixel(j,side-i,Color.RED);
            }
        }

        powerImage.setImageBitmap(tmpMap);
    }

    private void initContextMenu() {
        MenuObject close = new MenuObject("Close");
        close.setResourceValue(R.drawable.close);

        MenuObject plus = new MenuObject("Add Heater");
        plus.setResourceValue(R.drawable.plus);

        MenuObject delete = new MenuObject("Delete Heater");
        delete.setResourceValue(R.drawable.delete);

        List<MenuObject> menuObjects = new ArrayList<>();
        menuObjects.add(close);
        menuObjects.add(plus);
        menuObjects.add(delete);

        int actionBarHeight = 0;
        final TypedArray styledAttributes = Objects.requireNonNull(getContext()).getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize }
        );
        actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        MenuParams menuParams = new MenuParams(actionBarHeight, menuObjects, 0, 100, 200, false, true, false, MenuGravity.END);

        contextMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    private void initPowerMenu() {
        PowerMenuItem close = new PowerMenuItem("Close", R.drawable.close);
        PowerMenuItem plus = new PowerMenuItem("Add Heater", R.drawable.plus);
        PowerMenuItem delete = new PowerMenuItem("Delete Heater", R.drawable.delete);

        List<PowerMenuItem> powerMenuItems = new ArrayList<>();
        powerMenuItems.add(close);
        powerMenuItems.add(plus);
        powerMenuItems.add(delete);

        powerMenu = new PowerMenu.Builder(getContext())
                .addItemList(powerMenuItems)
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                .setTextGravity(Gravity.CENTER)
                .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();

        powerMenu.showAsDropDown(viewGroup);
    }

    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            powerMenu.setSelectedPosition(position); // change selected item
            powerMenu.dismiss();
        }
    };

    PowerMenu powerMenu;
    ContextMenuDialogFragment contextMenuDialogFragment;
    Croller croller;
    Bitmap map;
    Drawable powerDrawable;
    ImageView powerImage;
    TextView currentTemp;
    TextView hopeTemp;
    Button DataSendButton, ArduinoRegisterBtn;
    String arduinoID;
}
