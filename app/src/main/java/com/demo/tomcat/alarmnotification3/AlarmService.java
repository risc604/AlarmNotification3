package com.demo.tomcat.alarmnotification3;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmService extends Service
{
    private static final String TAG = AlarmService.class.getSimpleName();
    private final IBinder binder = new ServiceBinder();
    NotificationManager     alarmNotificationManager;

    public AlarmService()
    {
        Log.w(TAG, "AlarmService, constructor. ");
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        Log.w(TAG, "onBind(), ");

        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        sendNotification("Wake Up! Wake Up!");
        return super.onStartCommand(intent, flags, startId);
    }


    //---------------------- user function -----------------------//
    private void sendNotification(String msg)
    {
        Log.w(TAG, "sendNotification(), ");

        if (alarmNotificationManager == null) {
            alarmNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), 0);

            NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Alarm")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg);

            alarmNotificationBuilder.setContentIntent(contentIntent);
            alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
            Log.d(TAG, "Notification sent.");
        }
        else
        {
            alarmNotificationManager.cancelAll();
        }
    }



    //---------------------- inner class -----------------------//
    public class ServiceBinder extends Binder
    {
        AlarmService getService()
        {
            Log.w(TAG, "getAlarmService(), ");
            return AlarmService.this;
        }
    }
}
