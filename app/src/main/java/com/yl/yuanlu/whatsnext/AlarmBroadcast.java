package com.yl.yuanlu.whatsnext;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.yl.yuanlu.whatsnext.Model.Next;
import com.yl.yuanlu.whatsnext.Utils.ModelUtils;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by LUYUAN on 6/10/2017.
 */

public class AlarmBroadcast extends BroadcastReceiver {

    public static final String KEY_ALARM_NEXT = "alarm_next";

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        Next next = ModelUtils.toObject(intent.getStringExtra(KEY_ALARM_NEXT), new TypeToken<Next>(){});
        Log.i("Yuan DBG : ", "Alarm triggered : " + next.name);
        //Toast.makeText(context, next.name + " Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show();

        //Setup Notification
        android.support.v4.app.NotificationCompat.Builder mBuilder = getNotification(context, next);

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra(MainActivity.KEY_NOTIFICATION, true);
        resultIntent.putExtra(MainActivity.KEY_NOTIFICATION_NEXT, intent.getStringExtra(KEY_ALARM_NEXT));

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, next.alarmID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(next.alarmID, mBuilder.build());

        wl.release();
    }

    public void setAlarm(@NonNull Context context, @NonNull Next next) {
        if(next.done) return;
        if(next.remindDate == null) return;
        Calendar c = Calendar.getInstance();
        if(next.remindDate.compareTo(c.getTime()) <= 0) return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcast.class);
        intent.putExtra(KEY_ALARM_NEXT, ModelUtils.toString(next, new TypeToken<Next>(){}));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, next.alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, next.remindDate.getTime(), pendingIntent);
        Log.i("Yuan DBG : ", "Set Alarm : " + String.valueOf(next.alarmID));
    }

    public void cancelAlarm(@NonNull Context context, @NonNull Next next) {
        if(next.remindDate == null) return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, next.alarmID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        Log.i("Yuan DBG : ", "Cancel Alarm : " + String.valueOf(next.alarmID));
    }

    private android.support.v4.app.NotificationCompat.Builder getNotification(@NonNull Context context, @NonNull Next next) {
        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setContentTitle("What's Next")
                .setContentText(next.name);
        return mBuilder;
    }

}
