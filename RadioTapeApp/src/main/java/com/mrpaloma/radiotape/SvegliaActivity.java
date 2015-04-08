package com.mrpaloma.radiotape;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by MicheleMaccini on 04/04/2015.
 */
public class SvegliaActivity extends ActionBarActivity {

    private PendingIntent pendingIntent;

    private TimePicker timePicker;

    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sveglia);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        timePicker = (TimePicker) findViewById(R.id.timePickerSveglia);
        if (timePicker != null) {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }


        /*Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, 6);
        calendar.set(Calendar.YEAR, 2013);
        calendar.set(Calendar.DAY_OF_MONTH, 13);

        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 48);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM,Calendar.PM);

        Intent myIntent = new Intent(SvegliaActivity.this, AlarmManagerHelper.class);
        pendingIntent = PendingIntent.getBroadcast(SvegliaActivity.this, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            StartActivityMain();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void StartActivityMain() {

        // Avvio la nuova Activity
        Intent detailIntent = new Intent(this, MainActivity.class);
        //detailIntent.putExtra(PARAM_UNIQUECODE, "");

        startActivity(detailIntent);
        finish();

    }

    @Override
    public void onStart() {
        super.onStart();

        EasyTrackerCustom.StartAtivity(this);
        EasyTrackerCustom.AddScreen(this, "Sveglia");
    }

    @Override
    public void onStop() {
        super.onStop();

        EasyTrackerCustom.StopAtivity(this);
    }

}
