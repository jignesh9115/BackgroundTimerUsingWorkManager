package com.baxom.backgroundtimerusingworkmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Created by Jignesh Chauhan on 12-09-2022
 */
public class TimerWorkManager extends Worker {

    public TimerWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        sendNotification();
        return Result.success();
    }

    void sendNotification() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Intent intent1 = new Intent(getApplicationContext(), StopMigraineNotificationReceiver.class);
        intent1.putExtra("stop_action", "Stop Button Working");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(),
                0, intent1, PendingIntent.FLAG_IMMUTABLE);


        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1",
                    "android",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("WorkManger");
            mNotificationManager.createNotificationChannel(channel);
        }

        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.bigText("Hello, Jignesh We hope you are feeling better. Your migraine episode is still being tracked. Please let us know if you want to stop it.");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setAutoCancel(true); // clear notification after click
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.addAction(R.drawable.ic_baseline_stop_24, "STOP", pendingIntent1);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText("Hello, Jignesh We hope you are feeling better. Your migraine episode is still being tracked. Please let us know if you want to stop it."));

        mNotificationManager.notify(0, mBuilder.build());
    }

}
