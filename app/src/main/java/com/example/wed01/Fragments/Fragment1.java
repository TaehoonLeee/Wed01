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
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.wed01.Arduino.ArduinoDelete;
import com.example.wed01.Arduino.ArduinoRegister;
import com.example.wed01.AsyncHttp;
import com.example.wed01.Fragments.bottomFragments.BottomSheetTempSetDialog;
import com.example.wed01.MainActivityB;
import com.example.wed01.NotificationWork;
import com.example.wed01.R;

import org.json.JSONArray;

import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;

public class Fragment1 extends Fragment{
    private ViewGroup viewGroup;

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

        currentTempText = (TextView) viewGroup.findViewById(R.id.currentTemp);
        hopeTemp = (TextView) viewGroup.findViewById(R.id.hopeTemp);
        heatingText = (TextView) viewGroup.findViewById(R.id.heatingText);
        heatingText.setVisibility(View.INVISIBLE);
        waterTemp = (TextView) viewGroup.findViewById(R.id.waterTemp);
        thumbDrawable = getResources().getDrawable(R.drawable.point_green);
        circular_reveal_content = (FrameLayout) viewGroup.findViewById(R.id.circular_reveal_content);
        circular_reveal_content.setVisibility(View.INVISIBLE);

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

        thread = new TempThread();
//        thread.setDaemon(true);
        thread.start();

        return viewGroup;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_top_plus : registerArduino(); break;
            case R.id.menu_top_delete : deleteArduino(); break;
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
        else if (requestCode == 2) { // 사용하고 있는 기기를 삭제하고 다시 아두이노 기기를 선택하면 메인 액티비티 화면이 중복되는 것을 해
            if (resultCode == RESULT_FIRST_USER) {
                ((MainActivityB)getActivity()).finish();
            }
        }
    }

    private void registerArduino() {
        Intent intent = new Intent(getActivity(), ArduinoRegister.class);
        Bundle bundle = new Bundle();

        bundle.putBoolean("isNew", false);
        bundle.putString("USERID", userID);
        intent.putExtras(bundle);

        startActivityForResult(intent, 1);
    }

    private void deleteArduino() {
        Intent intent = new Intent(getActivity(), ArduinoDelete.class);
        Bundle bundle = new Bundle();

        bundle.putString("ARDUINOID", arduinoID);
        bundle.putString("USERID", userID);
        intent.putExtras(bundle);

        startActivityForResult(intent, 2);
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
            currentTempText.setVisibility(View.INVISIBLE);
            waterTemp.setVisibility(View.INVISIBLE);
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(circular_reveal_content, centerX, centerY, 0, radius);
            revealAnimator.setDuration(300);
            circular_reveal_content.setVisibility(View.VISIBLE);
            heatingText.setVisibility(View.VISIBLE);
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


    class TempThread extends Thread {
        boolean state = true;
        @Override
        public void run() {
            while(state) {
                handler.sendEmptyMessage(0);

                try {
                    Thread.sleep(30000);
//                    if(Thread.interrupted()) {
//                        state = false;
//                        break;
//                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    state = false;
                } finally {
                    Log.d("TempThread", "Thread is dead");
                }
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){   // Message id 가 0 이면
                try {
                    AsyncHttp asyncHttp = new AsyncHttp("phone/data/" + arduinoID, new ContentValues(), "GET");
                    String result = asyncHttp.execute().get();
                    JSONArray jsonArray = new JSONArray(result);
                    String currentTemp = jsonArray.getJSONObject(jsonArray.length()-1).getString("humidity");
                    String humidity = jsonArray.getJSONObject(jsonArray.length()-1).getString("humi");

                    WorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWork.class).build();
                    WorkManager.getInstance().enqueue(workRequest);

                    Log.d("TempThread", "Thread is alive, temperature is " + currentTemp + ", humidity is " + humidity);
                    currentTempText.setText(currentTemp + "\u2103");
                    waterTemp.setText(humidity + "%");
                    changeTempIcon(Integer.parseInt(currentTemp));
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    };

    public static void showOffReveal() {
        currentTempText.setVisibility(View.VISIBLE);
        waterTemp.setVisibility(View.VISIBLE);
        circular_reveal_content.setVisibility(View.INVISIBLE);
        heatingText.setVisibility(View.INVISIBLE);

        String currentTemp = currentTempText.getText().toString().replace("℃", "");
        Log.d("Fragment1_CurrentTemp", currentTemp);
        changeTempIcon(Integer.parseInt(currentTemp));
    }

    private static void changeTempIcon(int temperature) {
        if(temperature < 21) {
            snowImage.setImageBitmap(null);
            snowImage.setImageResource(R.drawable.snow);
            fireImage.setVisibility(View.INVISIBLE);
            snowImage.setVisibility(View.VISIBLE);
        }
        else {
            fireImage.setImageBitmap(null);
            fireImage.setImageResource(R.drawable.fire);
            fireImage.setVisibility(View.VISIBLE);
            snowImage.setVisibility(View.INVISIBLE);
        }
    }

    private Bitmap fireMap, snowMap;
    private Drawable fireDrawable, snowDrawable;
    static ImageView fireImage, snowImage;
    static TextView currentTempText, hopeTemp, heatingText, waterTemp;
    private String arduinoID, userID;
    private Context thisContext;
    private SeekBar seekBar;
    private View thumbView;
    private Drawable thumbDrawable;
    static FrameLayout circular_reveal_content;
    private TempThread thread;
}