package com.example.wed01;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;

public class NotificationWork extends Worker {

    public NotificationWork(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        // Do the work here--in this case, upload the images.
        Log.d("NotificationWork", "period");
        if(Integer.parseInt(getCurrentTemp()) < 50 && Integer.parseInt(getCurrentTemp()) > 30) {
            createNotification();
        }
//        createNotification();
        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }

    private void createNotification() {
        Intent intent = new Intent(this.getApplicationContext(), MainActivityB.class);

        Bundle bundle = new Bundle();
        bundle.putString("ARDUINOID", MainActivityB.arduinoId);
        bundle.putString("USERID", MainActivityB.userID);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");
        builder.setSmallIcon(R.drawable.noticon);
        builder.setContentTitle("알람");
        builder.setContentText("온도 비정상");
        builder.setAutoCancel(true);
        builder.setColor(Color.BLACK);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }

    private String getCurrentTemp() {
        String arduinoID = MainActivityB.getArduinoId();
        String currentTemp = "";

        try {
            AsyncHttp asyncHttp = new AsyncHttp("phone/data/" + arduinoID, new ContentValues(), "GET");
            String result = asyncHttp.execute().get();
            JSONArray jsonArray = new JSONArray(result);
            currentTemp = jsonArray.getJSONObject(jsonArray.length()-1).getString("humidity");

            Log.d("NotificationWork", "WorkManager is alive, temperature is " + currentTemp);
        } catch (Exception e) { e.printStackTrace(); }

        return currentTemp;
    }
}