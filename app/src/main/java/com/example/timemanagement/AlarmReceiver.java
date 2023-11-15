package com.example.timemanagement;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    final String CHANNEL_ID="201";
    MediaPlayer mediaPlayer;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("My Action")){
            NotificationManager notificationManager=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
                NotificationChannel channel=new NotificationChannel(CHANNEL_ID,"Channel 1",NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("This is Channel 1");
                notificationManager.createNotificationChannel(channel);
            }
            Intent notificationIntent = new Intent(context, WeekViewActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(WeekViewActivity.class);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder= new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.notifications_active)
                    .setContentTitle(intent.getStringExtra("Title"))
                    .setContentText(intent.getStringExtra("Desc"))
                    .setColor(Color.RED)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notificationManager.notify(getNotification(),builder.build());
        }
    }

    private int getNotification() {
        int time = (int)new Date().getTime();
        return time;
    }
}
