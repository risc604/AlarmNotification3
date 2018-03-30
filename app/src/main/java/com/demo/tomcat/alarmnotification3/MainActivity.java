package com.demo.tomcat.alarmnotification3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private final String ALARM_TIMER = this.getPackageName() + ".ALARM_TIMER_ACTION";


    TimePicker alarmTimerPicker;
    ToggleButton alarmToggle;
    TextView alarmTextView;

    AlarmManager    alarmManager;
    PendingIntent   pendingIntent;

    public class AlarmReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String actionEvent = intent.getAction();
            if (actionEvent.equalsIgnoreCase(ALARM_TIMER))
            {

            }
            else if (actionEvent.equalsIgnoreCase(""))
            {

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initControl();
        makeFilter();
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
            Intent newIntent = new Intent(this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                    0, newIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
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
        alarmTextView.setText(alarmText);
    }

    public void makeFilter()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ALARM_TIMER);

    }


}
