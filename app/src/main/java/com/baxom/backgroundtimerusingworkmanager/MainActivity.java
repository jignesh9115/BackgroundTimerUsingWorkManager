package com.baxom.backgroundtimerusingworkmanager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    long startTime = 0;

    SharedPreferences preferences;
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("pref", MODE_PRIVATE);

        button = findViewById(R.id.btn);
        textView = findViewById(R.id.textView);

        button.setOnClickListener(v -> {
            /*startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
            isRunning = true;*/

            final PeriodicWorkRequest periodicWorkRequest1 = new PeriodicWorkRequest
                    .Builder(TimerWorkManager.class, 10, TimeUnit.MINUTES)
                    .setInitialDelay(15, TimeUnit.SECONDS)
                    .addTag("TimerJob")
                    .build();
            WorkManager workManager = WorkManager.getInstance(this);
            workManager.enqueue(periodicWorkRequest1);
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        isRunning = preferences.getBoolean("isRunning", isRunning);

        if (isRunning) {
            startTime = preferences.getLong("startTime", startTime);
            timerHandler.postDelayed(timerRunnable, 0);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isRunning", isRunning);
        editor.putLong("startTime", startTime);
        editor.apply();
    }


    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @SuppressLint("DefaultLocale")
        @Override
        public void run() {

            Log.d("MainActivity", "run: " + startTime);

            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int hours = (int) TimeUnit.MILLISECONDS.toHours(millis);
            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(millis);
            //int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(millis);
            //int minutes = seconds / 60;
            seconds = seconds % 60;

            textView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

}


