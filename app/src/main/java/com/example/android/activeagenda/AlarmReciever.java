package com.example.android.activeagenda;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Julie on 12/4/2016.
 */

public class AlarmReciever extends BroadcastReceiver {
    private DBHelper dbHelper;
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent ) {
        dbHelper = new DBHelper(context);
        Date curDate = new Date();
        if (intent != null) {
            Bundle b = intent.getExtras();
            if (b != null)
                curDate = (Date)(intent.getExtras().getSerializable("CUR_DATE"));
        }
        createNotification(context, curDate);
    }

    public void createNotification(Context context, Date curDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);
        c.add(Calendar.DATE, 1);
        Date nextDay = c.getTime();
        int numTasks = dbHelper.getAllTasks(nextDay).size();

        Intent notifyIntent = new Intent(context, DayViewActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notifyIntent, 0);

        NotificationCompat.Builder notificBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(numTasks + " Tasks to complete for tomorrow")
                .setContentText("Click for more details")
                .setTicker("Goto ActiveAgenda")
                .setSmallIcon(R.drawable.checklist_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_icon))
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificBuilder.build());
    }
}
