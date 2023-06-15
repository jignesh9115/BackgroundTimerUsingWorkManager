package com.baxom.backgroundtimerusingworkmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.work.WorkManager;

/**
 * Created by Jignesh Chauhan on 13-09-2022
 */
public class StopMigraineNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Your Migraine Stopped", Toast.LENGTH_SHORT).show();
        WorkManager.getInstance(context).cancelAllWorkByTag("TimerJob");
    }
}
