package com.example.wed01.Fragments;

import android.animation.Animator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.wed01.Arduino.ArduinoDelete;
import com.example.wed01.Arduino.ArduinoRegister;
import com.example.wed01.AsyncHttp;
import com.example.wed01.Fragments.bottomFragments.BottomSheetTempSetDialog;
import com.example.wed01.MainActivityB;
import com.example.wed01.R;

import org.json.JSONArray;

import static android.app.Activity.RESULT_OK;

public class Fragment1 extends Fragment{
    ViewGroup viewGroup;


    public static Fragment1 newInstance(String arduinoID, String userID) {
        Fragment1 fragment1 = new Fragment1();
        Bundle bundle = new Bundle();
        bundle.putString("ARDUINOID", arduinoID);
        bundle.putString("USERID", userID);
        fragment1.setArguments(bundle);

        return fragment1;
    }

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
            userID = getArguments().getString("USERID");
            MainActivityB.setArduinoId(arduinoID);
            MainActivityB.setUserID(userID);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);

        thisContext = container.getContext();

        thumbView = LayoutInflater.from(thisContext).inflate(R.layout.layout_seekbar_thumb, null, false);

        Toolbar toolbar = (Toolbar) viewGroup.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        currentTemp = (TextView) viewGroup.findViewById(R.id.currentTemp);
        hopeTemp = (TextView) viewGroup.findViewById(R.id.hopeTemp);
        thumbDrawable = getResources().getDrawable(R.drawable.point_green);
        circular_reveal_content = (FrameLayout) viewGroup.findViewById(R.id.circular_reveal_content);
        circular_reveal_content.setVisibility(View.INVISIBLE);

//        powerImage = (ImageView) viewGroup.findViewById(R.id.powerImage);
//
//        powerDrawable = getResources().getDrawable(R.drawable.power);
//        map = ((BitmapDrawable)powerDrawable).getBitmap();
//        powerImage.setImageBitmap(map);

        initMainImage();

        seekBar = (SeekBar) viewGroup.findViewById(R.id.SeekBar);
        seekBar.setThumb(getThumb(21));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setThumb(thumbDrawable);
                hopeTemp.setText(String.valueOf(progress+16));
                changeBitmap(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.setThumb(thumbDrawable);
                hopeTemp.setText(String.valueOf(seekBar.getProgress()+16));
                showReveal(true);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setThumb(getThumb(seekBar.getProgress() + 16));
                showReveal(false);
            }
        });

//        croller = (Croller) viewGroup.findViewById(R.id.croller);
//        initCroller();
//
//        croller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
//            @Override
//            public void onProgressChanged(int progress) {
//                hopeTemp.setText("온도 조절 : " + progress);
//                changeBitmap(progress);
//            }
//        });

//        DataSendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendArduinoData();
//            }
//        });

        TempThread thread = new TempThread();
        thread.setDaemon(true);
        thread.start();

        return viewGroup;
    }

    private void initMainImage() {
        fireImage = (ImageView) viewGroup.findViewById(R.id.fireImage);
        snowImage = (ImageView) viewGroup.findViewById(R.id.snowImage);
        snowImage.setVisibility(View.INVISIBLE);

        fireDrawable = getResources().getDrawable(R.drawable.fire);
        snowDrawable = getResources().getDrawable(R.drawable.snow);

        fireMap = ((BitmapDrawable)fireDrawable).getBitmap();
        snowMap = ((BitmapDrawable)snowDrawable).getBitmap();

        fireImage.setImageBitmap(fireMap);
        snowImage.setImageBitmap(snowMap);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_top_plus : registerArduino(); break;
            case R.id.menu_top_delete : break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                arduinoID = data.getStringExtra("arduinoID");
                MainActivityB.setArduinoId(arduinoID);
            }
        }
    }

    private void registerArduino() {
        boolean isNew = false;
        Intent intent = new Intent(getActivity(), ArduinoRegister.class);
        Bundle bundle = new Bundle();

        bundle.putBoolean("isNew", isNew);
        intent.putExtras(bundle);

        startActivityForResult(intent, 1);
    }

    private void deleteArduino() {
        Intent intent = new Intent(getActivity(), ArduinoDelete.class);
        Bundle bundle = new Bundle();

        bundle.putString("ARDUINOID", arduinoID);
        bundle.putString("USERID", userID);
    }

//    private void sendArduinoData() {
//        int humidity = croller.getProgress();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("ardID", 1);
//        contentValues.put("humidity", humidity);
//
//        Log.d("MainActivity", String.valueOf(humidity));
//
//        try {
//            AsyncHttp asyncHttp = new AsyncHttp("phone/data", contentValues, "POST");
//            asyncHttp.execute();
//        } catch (Exception e) { e.printStackTrace(); }
//    }

    private Drawable getThumb(int progress) {
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    private void showReveal(boolean isOpen) {
        int centerX = (int) circular_reveal_content.getX() + circular_reveal_content.getWidth() / 2;
        int centerY = (int) circular_reveal_content.getY() + circular_reveal_content.getHeight() / 2;

        int radius = (int) Math.hypot(circular_reveal_content.getWidth(), circular_reveal_content.getHeight());

        if (isOpen) {
            currentTemp.setVisibility(View.INVISIBLE);
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(circular_reveal_content, centerX, centerY, 0, radius);
            revealAnimator.setDuration(300);
            circular_reveal_content.setVisibility(View.VISIBLE);
            revealAnimator.start();
        }
        else {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(circular_reveal_content, centerX, centerY, radius, 0);

            revealAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    currentTemp.setVisibility(View.VISIBLE);
                    circular_reveal_content.setVisibility(View.INVISIBLE);
                    BottomSheetTempSetDialog bottomSheetMenuDialog = BottomSheetTempSetDialog.getInstance(arduinoID, String.valueOf(seekBar.getProgress()+16));
                    bottomSheetMenuDialog.show(getActivity().getSupportFragmentManager(), "tag");
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            revealAnimator.setDuration(300);

            revealAnimator.start();
        }
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

//    private void initCroller() {
//        croller.setIndicatorWidth(10);
//        croller.setBackCircleColor(Color.TRANSPARENT);
//        croller.setMainCircleColor(Color.TRANSPARENT);
//        croller.setMax(100);
//        croller.setStartOffset(45);
//        croller.setIsContinuous(true);
//        croller.setLabelColor(Color.TRANSPARENT);
//        croller.setProgressPrimaryColor(Color.RED);
//        croller.setIndicatorColor(Color.TRANSPARENT);
//        croller.setProgressSecondaryColor(Color.parseColor("#EEEEEE"));
//        croller.setProgressPrimaryStrokeWidth(10);
//    }
//
    private void changeBitmap(int progress) {
        ImageView imageView;
        Bitmap bitmap;

        if(progress <= 5) {
            snowImage.setVisibility(View.VISIBLE);
            fireImage.setVisibility(View.INVISIBLE);
            imageView = snowImage;
            bitmap = snowMap;
        }
        else {
            snowImage.setVisibility(View.INVISIBLE);
            fireImage.setVisibility(View.VISIBLE);
            imageView = fireImage;
            bitmap = fireMap;
        }

        Bitmap tmpMap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        int side = bitmap.getWidth()-1;

        double progressToMaping = (double) (side/5d) * (progress % 6);
        int row = (int) progressToMaping;

        for(int i = 0; i < row; i++) {
            for(int j = 0; j < side; j++) {
                if(tmpMap.getPixel(j,side-i) == 0) tmpMap.setPixel(j, side-i, Color.TRANSPARENT);
                else {
                    if(progress <= 5) tmpMap.setPixel(j,side-i,Color.BLUE);
                    else tmpMap.setPixel(j, side-i, Color.RED);
                }
            }
        }

        imageView.setImageBitmap(tmpMap);
    }

//    Croller croller;
    Bitmap fireMap, snowMap;
    Drawable fireDrawable, snowDrawable;
    ImageView fireImage, snowImage;
    TextView currentTemp, hopeTemp;
    String arduinoID, userID;
    Context thisContext;
    SeekBar seekBar;
    View thumbView;
    Drawable thumbDrawable;
    FrameLayout circular_reveal_content;
}
