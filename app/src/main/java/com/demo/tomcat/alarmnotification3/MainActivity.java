package com.demo.tomcat.alarmnotification3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

// http://www.betterbing.net/?p=205
// https://stackoverflow.com/questions/32697295/android-alarm-manager-with-broadcast-receiver

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String ALARM_TIMER = "com.demo.tomcat.alarmnotification3.ACTION_ALARM_TIMER";
    public static final String NOTIFICATION = "com.demo.tomcat.alarmnotification3.ACTION_NOTIFICATION";


    TimePicker alarmTimerPicker;
    ToggleButton alarmToggle;
    TextView alarmTextView;

    AlarmManager    alarmManager;
    PendingIntent   pendingIntent;
    AlarmReceiver   alarmReceiver;
    AlarmService    alarmService;

    private static boolean  registedFilter = false;

    public class AlarmReceiver extends BroadcastReceiver
    //private BroadcastReceiver alarmReceiver = new BroadcastReceiver()
    {
        Ringtone ringtone;

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String actionEvent = intent.getAction();
            Log.w(TAG, "onReceive(), action: " + actionEvent);
            //String actionEvent = intent.getAction();
            if (actionEvent.equalsIgnoreCase(ALARM_TIMER))
            {
                Log.w(TAG, "ACTION ALARM_TIMER, ");
                Uri alarmUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                if (alarmUri == null) {
                    //alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                }

                ringtone = RingtoneManager.getRingtone(context, alarmUri);
                ringtone.play();
                sendBroadcast(new Intent(ALARM_TIMER));
                //ComponentName comp = new ComponentName(context.getPackageName(), AlarmService.class.getName());
                //startWakefulService(context, (intent.setComponent(comp)));

            }
            else if (actionEvent.equalsIgnoreCase(NOTIFICATION))
            {
                Log.w(TAG, "ACTION NOTIFICATION, ");

            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            Log.w(TAG, "onServiceConnected(), ");
            alarmService = ((AlarmService.ServiceBinder) service).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            Log.w(TAG, "onServiceDisconnected(), ");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w(TAG, "onCreate(), ");

        initView();
        initControl();

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.w(TAG, "onStart(), mConnection: " + mConnection);

        if (!registedFilter)
        {
            alarmReceiver = new AlarmReceiver();
            this.registerReceiver(alarmReceiver, makeFilterRegister());
            registedFilter = true;
             Log.w(TAG, " register Reveiver: " + alarmReceiver.toString() );
        }

        //if (mConnection == null)
        //{
            Intent serviceIntent = new Intent(this, AlarmService.class);
            bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
        //}
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.w(TAG, "onStop(), ");

        if (registedFilter)
        {
            this.unregisterReceiver(alarmReceiver);
            registedFilter = false;
        }
    }

    public void onToggleClicked(View view)
    {
        Log.w(TAG, "onToggleClicked(), ");
        if (((ToggleButton)view).isChecked() && pendingIntent == null)
        {
            Log.d(TAG, "Alarm On");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimerPicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimerPicker.getCurrentMinute());
            Intent newIntent = new Intent(ALARM_TIMER);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                    0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            this.sendBroadcast(newIntent);
            //sendBroadcast(new Intent(ALARM_TIMER));
        }
        else
        {
            alarmManager.cancel(pendingIntent);
            pendingIntent = null;
            setAlarmText("");
            Log.d(TAG, "Alarm Off");
        }
    }


    //------------------------- user function ---------------------------//
    private void initView()
    {
        Log.w(TAG, " initView(), ");
        alarmTimerPicker = findViewById(R.id.alarmTimerPicker);
        alarmToggle = findViewById(R.id.alarmToggle);
        alarmTextView = findViewById(R.id.alarmText);
    }

    private void initControl()
    {
        Log.w(TAG, " initControl(), ");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    }

    public void setAlarmText(String alarmText)
    {
        Log.w(TAG, " setAlarmText(), ");
        alarmTextView.setText(alarmText);
    }

    private static IntentFilter makeFilterRegister()
    {
        Log.w(TAG, " makeFilterRegister(), ");
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ALARM_TIMER);
        filter.addAction(NOTIFICATION);
        return filter;



    }


}
